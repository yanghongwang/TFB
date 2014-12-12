package com.cn.tfb;

import java.util.HashMap;
import java.util.Stack;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie2;

import com.cn.tfb.config.Constant;
import com.cn.tfb.config.IConfig;
import com.cn.tfb.config.PreferenceConfig;
import com.cn.tfb.config.PropertiesConfig;
import com.cn.tfb.dao.DaoMaster;
import com.cn.tfb.dao.DaoMaster.OpenHelper;
import com.cn.tfb.dao.DaoSession;
import com.cn.tfb.event.EventBus;
import com.cn.tfb.exception.NoSuchCommandException;
import com.cn.tfb.ioc.Injector;
import com.cn.tfb.log.Logger;
import com.cn.tfb.mvc.command.CommandExecutor;
import com.cn.tfb.mvc.command.ICommand;
import com.cn.tfb.mvc.command.IdentityCommand;
import com.cn.tfb.mvc.common.IResponseListener;
import com.cn.tfb.mvc.common.Request;
import com.cn.tfb.mvc.common.Response;
import com.cn.tfb.mvc.controller.ActivityStackInfo;
import com.cn.tfb.mvc.controller.NavigationDirection;
import com.cn.tfb.mvc.view.ILayoutLoader;
import com.cn.tfb.mvc.view.LayoutLoader;
import com.cn.tfb.ui.BaseActivity;
import com.cn.tfb.ui.SplashActivity;
import com.cn.tfb.util.NetWorkUtil.netType;
import com.cn.tfb.volley.RequestQueue;
import com.cn.tfb.volley.VolleyLog;
import com.cn.tfb.volley.util.HttpClientStack;
import com.cn.tfb.volley.util.Volley;
import com.esotericsoftware.kryo.Kryo;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

public class AppApplication extends Application implements OnClickListener,
		IResponseListener
{
	private static final String TAG = "AppApplication";
	private static AppApplication mInstance;
	public Context mCurrContext;
	public Handler mCrashHandler;
	private NetChangeObserver mNetChangeObserver;
	private boolean mNetworkAvailable = false;
	private BaseActivity mCurrActivity;
	private final static String IDENTITYCOMMAND = "IdentityCommand";
	private CommandExecutor mCommandExecutor;
	/** 配置器 为Preference */
	public final static int PREFERENCECONFIG = 0;
	/** 配置器 为PROPERTIESCONFIG */
	public final static int PROPERTIESCONFIG = 1;
	/** 配置器 */
	private IConfig mCurrentConfig;
	/** 获取布局文件ID加载器 */
	private ILayoutLoader mLayoutLoader;
	/** 加载类注入器 */
	private Injector mInjector;
	private Stack<ActivityStackInfo> activityStack = new Stack<ActivityStackInfo>();
	private BaseActivity currentActivity;

	private DefaultHttpClient mHttpClient;

	private NavigationDirection currentNavigationDirection;
	/** ElyseeAndroid 应用程序运行Activity管理器 */
	private AppManager mAppManager;
	private final HashMap<String, Class<? extends BaseActivity>> registeredActivities = new HashMap<String, Class<? extends BaseActivity>>();
	private RequestQueue requestQueue;
	// banner 更新地址
	public static String IMAGEVERSIONURL = "http://appdownload.ppmoney.com/image_version.txt";

	public static AppApplication getInstance()
	{
		if (null == mInstance)
		{
			mInstance = new AppApplication();
		}
		return mInstance;
	}

	private void getConfig()
	{
		if (Constant.isOnline)
		{
			Constant.RUQUESTURL = "http://app.ppmoney.com:8070/api/";
		}
		else
		{
			Constant.RUQUESTURL = "http://219.137.254.34:8082/api/";
		}
	}

	@Override
	public void onCreate()
	{
		onPreCreateApp();
		super.onCreate();
		doOnCreate();
		onAfterCreateApp();
		initAppManager();
		getConfig();
		registerActivity();
		initDao();
		createSnappyDb();
		checkVersion();
	}

	/**
	 * 创建键值对数据库
	 */
	private void createSnappyDb()
	{
		Kryo kryo = new Kryo();
		try
		{
			Constant.db = DBFactory.open(mInstance, Constant.DbFileName, kryo);
		}
		catch (SnappydbException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 初始化greendDao
	 */
	private void initDao()
	{
		getDaoMaster(mInstance);
		getDaoSession(mInstance);
	}

	private static DaoSession getDaoSession(Context context)
	{
		// 实例化daoSession
		if (Constant.daoSession == null)
		{
			if (Constant.daoMaster == null)
			{
				getDaoMaster(context);
			}
			Constant.daoSession = Constant.daoMaster.newSession();
		}
		return Constant.daoSession;
	}

	private static DaoMaster getDaoMaster(Context context)
	{
		// 实例化daoMaster
		if (Constant.daoMaster == null)
		{
			OpenHelper helper = new DaoMaster.DevOpenHelper(mInstance,
					Constant.DbName, null);
			Constant.daoMaster = new DaoMaster(helper.getWritableDatabase());
		}
		return Constant.daoMaster;
	}

	private void checkVersion()
	{
		try
		{
			Constant.localVersion = getPackageManager().getPackageInfo(
					getPackageName(), 0).versionCode;
			Constant.serverVersion = getServerVersion();
		}
		catch (NameNotFoundException ex)
		{

		}
	}

	private int getServerVersion()
	{
		return 1;
	}

	public RequestQueue getRequestQueue()
	{
		if (null == requestQueue)
		{
			requestQueue = Volley.newRequestQueue(mInstance);
		}
		return requestQueue;
	}

	public RequestQueue getRequestQueueCookie()
	{
		if (null == requestQueue)
		{
			mHttpClient = new DefaultHttpClient();
			requestQueue = Volley.newRequestQueue(mInstance,
					new HttpClientStack(mHttpClient));
		}
		return requestQueue;
	}

	public void setCookie()
	{
		CookieStore cookieStore = mHttpClient.getCookieStore();
		cookieStore.addCookie(new BasicClientCookie2("cookie", "pay"));
	}

	public <T> void addToRequestQueue(com.cn.tfb.volley.Request<T> req)
	{
		req.setTag(TAG);
		if (null != mHttpClient)
		{
			setCookie();
		}
		VolleyLog.d("添加请求到队列: %s", req.getUrl());
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag)
	{
		if (requestQueue != null)
		{
			requestQueue.cancelAll(tag);
		}
	}

	public <T> void addToRequestQueue(com.cn.tfb.volley.Request<T> req,
			String tag)
	{
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

		VolleyLog.d("添加请求到队列: %s", req.getUrl());
		if (null != mHttpClient)
		{
			setCookie();
		}
		getRequestQueue().add(req);
	}

	protected void onPreCreateApp()
	{

	}

	/**
	 * 注册视图
	 * 
	 * @author hzx 2014年5月19日
	 */
	private void registerActivity()
	{
		registerActivity(R.string.splashactivity, SplashActivity.class);
	}

	private void doOnCreate()
	{
		mInstance = this;
		// 注册App异常崩溃处理器
		mCommandExecutor = CommandExecutor.getInstance();
		mNetChangeObserver = new NetChangeObserver()
		{
			@Override
			public void onConnect(netType type)
			{
				super.onConnect(type);
				mInstance.onConnect(type);
			}

			@Override
			public void onDisConnect()
			{
				super.onDisConnect();
				mInstance.onDisConnect();

			}
		};
		NetworkStateReceiver.registerObserver(mNetChangeObserver);
		registerCommand(IDENTITYCOMMAND, IdentityCommand.class);
	}

	/**
	 * 网络连接连接时调用
	 */
	protected void onConnect(netType type)
	{
		mNetworkAvailable = true;
		if (mCurrActivity != null)
		{
			mCurrActivity.onConnect(type);
		}
	}

	/**
	 * 当前没有网络连接
	 */
	public void onDisConnect()
	{
		mNetworkAvailable = false;
		if (mCurrActivity != null)
		{
			mCurrActivity.onDisConnect();
		}
	}

	protected void onAfterCreateApp()
	{
		Constant.eventBus = EventBus.getDefault();
	}

	public void initAppManager()
	{

	}

	@Override
	public void onClick(View v)
	{

	}

	public IConfig getPreferenceConfig()
	{
		return getConfig(PREFERENCECONFIG);
	}

	public IConfig getPropertiesConfig()
	{
		return getConfig(PROPERTIESCONFIG);
	}

	public IConfig getConfig(int confingType)
	{
		if (confingType == PREFERENCECONFIG)
		{
			mCurrentConfig = PreferenceConfig.getPreferenceConfig(this);

		}
		else if (confingType == PROPERTIESCONFIG)
		{
			mCurrentConfig = PropertiesConfig.getPropertiesConfig(this);
		}
		else
		{
			mCurrentConfig = PropertiesConfig.getPropertiesConfig(this);
		}
		if (!mCurrentConfig.isLoadConfig())
		{
			mCurrentConfig.loadConfig();
		}
		return mCurrentConfig;
	}

	public IConfig getCurrentConfig()
	{
		if (mCurrentConfig == null)
		{
			getPreferenceConfig();
		}
		return mCurrentConfig;
	}

	public ILayoutLoader getLayoutLoader()
	{
		if (mLayoutLoader == null)
		{
			mLayoutLoader = LayoutLoader.getInstance(this);
		}
		return mLayoutLoader;
	}

	public void setLayoutLoader(ILayoutLoader layoutLoader)
	{
		this.mLayoutLoader = layoutLoader;
	}

	public Injector getInjector()
	{
		if (mInjector == null)
		{
			mInjector = Injector.getInstance();
		}
		return mInjector;
	}

	public void setInjector(Injector injector)
	{
		this.mInjector = injector;
	}

	public void onActivityCreating(BaseActivity activity)
	{
		if (activityStack.size() > 0)
		{
			ActivityStackInfo info = activityStack.peek();
			if (info != null)
			{
				Response response = info.getResponse();
				activity.preProcessData(response);
			}
		}
	}

	public void onActivityCreated(BaseActivity activity)
	{
		if (currentActivity != null)
		{
			currentActivity.finish();
		}
		currentActivity = activity;

		int size = activityStack.size();

		if (size > 0)
		{
			ActivityStackInfo info = activityStack.peek();
			if (info != null)
			{
				Response response = info.getResponse();
				activity.processData(response);

				if (size >= 2 && !info.isRecord())
				{
					activityStack.pop();
				}
			}
		}
	}

	public void doCommand(String commandKey, Request request,
			IResponseListener listener, boolean record, boolean resetStack)
	{
		if (listener != null)
		{
			try
			{
				CommandExecutor.getInstance().enqueueCommand(commandKey,
						request, listener);

			}
			catch (NoSuchCommandException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			Logger.i(mInstance, "go with cmdid=" + commandKey + ", record: "
					+ record + ",rs: " + resetStack + ", request: " + request);
			if (resetStack)
			{
				activityStack.clear();
			}

			currentNavigationDirection = NavigationDirection.Forward;

			ActivityStackInfo info = new ActivityStackInfo(commandKey, request,
					record, resetStack);
			activityStack.add(info);

			Object[] newTag =
			{ request.getTag(), listener };
			request.setTag(newTag);

			Logger.i(mInstance, "Enqueue-ing command");
			try
			{
				CommandExecutor.getInstance().enqueueCommand(commandKey,
						request, this);
			}
			catch (NoSuchCommandException e)
			{
				e.printStackTrace();
			}
			Logger.i(mInstance, "Enqueued command");

		}

	}

	public void back()
	{
		Logger.i(mInstance, "ActivityStack Size: " + activityStack.size());
		if (activityStack != null && activityStack.size() != 0)
		{
			if (activityStack.size() >= 2)
			{
				activityStack.pop();
			}

			currentNavigationDirection = NavigationDirection.Backward;
			ActivityStackInfo info = activityStack.peek();
			try
			{
				CommandExecutor.getInstance().enqueueCommand(
						info.getCommandKey(), info.getRequest(), this);
			}
			catch (NoSuchCommandException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void processResponse(Message msg)
	{
		Response response = (Response) msg.obj;
		ActivityStackInfo top = activityStack.peek();
		top.setResponse(response);
		if (response != null)
		{
			int targetActivityKeyResID = response.getActivityKeyResID();
			String targetActivityKey = "";
			if (targetActivityKeyResID != 0)
			{
				targetActivityKey = getString(targetActivityKeyResID);
			}
			if (targetActivityKey != null
					&& targetActivityKey.equalsIgnoreCase(""))
			{
				targetActivityKey = response.getActivityKey();
			}
			Object[] newTag = (Object[]) response.getTag();
			Object tag = newTag[0];
			response.setTag(tag);
			Class<? extends BaseActivity> cls = registeredActivities
					.get(targetActivityKey);
			Logger.i(mInstance,
					"Launching new activity // else, current Direction: "
							+ currentNavigationDirection);

			int asize = activityStack.size();
			Logger.i(mInstance, "Current Stack Size (before processing): "
					+ asize);

			switch (currentNavigationDirection)
			{
			case Forward:
				if (asize >= 2)
				{
					if (!top.isRecord())
					{
						activityStack.pop();
					}
				}
				break;
			case Backward:
				currentNavigationDirection = NavigationDirection.Forward;
				break;
			}
			Logger.i(mInstance, "Current Stack Size (after processing): "
					+ activityStack.size());

			if (cls != null)
			{
				Intent launcherIntent = new Intent(currentActivity, cls);
				currentActivity.startActivity(launcherIntent);
				currentActivity.finish();
				top.setActivityClass(cls);
			}

		}

	}

	public void registerActivity(int resID, Class<? extends BaseActivity> clz)
	{
		String activityKey = getString(resID);
		registeredActivities.put(activityKey, clz);
	}

	public void registerActivity(String activityKey,
			Class<? extends BaseActivity> clz)
	{
		registeredActivities.put(activityKey, clz);
	}

	public void unregisterActivity(int resID)
	{
		String activityKey = getString(resID);
		unregisterActivity(activityKey);
	}

	public void unregisterActivity(String activityKey)
	{
		registeredActivities.remove(activityKey);
	}

	public void registerCommand(int resID, Class<? extends ICommand> command)
	{

		String commandKey = getString(resID);
		registerCommand(commandKey, command);

	}

	public void registerCommand(String commandKey,
			Class<? extends ICommand> command)
	{
		if (command != null)
		{
			mCommandExecutor.registerCommand(commandKey, command);
		}
	}

	public void unregisterCommand(int resID)
	{
		String commandKey = getString(resID);
		unregisterCommand(commandKey);
	}

	public void unregisterCommand(String commandKey)
	{
		mCommandExecutor.unregisterCommand(commandKey);
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			processResponse(msg);
		}
	};

	private void handleResponse(Response response)
	{
		Message msg = new Message();
		msg.what = 0;
		msg.obj = response;
		handler.sendMessage(msg);
	}

	@Override
	public void onStart()
	{

	}

	@Override
	public void onSuccess(Response response)
	{
		handleResponse(response);
	}

	@Override
	public void onRuning(Response response)
	{

	}

	@Override
	public void onFailure(Response response)
	{
		handleResponse(response);
	}

	public AppManager getAppManager()
	{
		if (mAppManager == null)
		{
			mAppManager = AppManager.getAppManager();
		}
		return mAppManager;
	}

	/**
	 * 退出应用程序
	 * 
	 * @param isBackground
	 *            是否开开启后台运行,如果为true则为后台运行
	 */
	public void exitApp(Boolean isBackground)
	{
		mAppManager.AppExit(this, isBackground);
	}

	/**
	 * 获取当前网络状态，true为网络连接成功，否则网络连接失败
	 * 
	 * @return
	 */
	public Boolean isNetworkAvailable()
	{
		return mNetworkAvailable;
	}

	@Override
	public void onFinish()
	{

	}

}
