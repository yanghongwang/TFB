package com.cn.tfb.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.cn.tfb.AppApplication;
import com.cn.tfb.R;
import com.cn.tfb.api.ApiFucUtil;
import com.cn.tfb.config.ActivityConstant;
import com.cn.tfb.config.Constant;
import com.cn.tfb.config.IConfig;
import com.cn.tfb.config.PreferenceConfig;
import com.cn.tfb.entity.CheckUpdateRespBody;
import com.cn.tfb.entity.RespBody;
import com.cn.tfb.entity.RespHeader;
import com.cn.tfb.entity.RespRetInfo;
import com.cn.tfb.entity.ResponseEntity;
import com.cn.tfb.event.type.BaseEvent;
import com.cn.tfb.event.type.SubmitEvent;
import com.cn.tfb.ioc.InjectResource;
import com.cn.tfb.log.Logger;
import com.cn.tfb.util.EncryptUtil;
import com.cn.tfb.util.ShoutCutUtil;
import com.cn.tfb.volley.Response;
import com.cn.tfb.volley.VolleyError;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.umeng.analytics.MobclickAgent;

public class SplashActivity extends BaseActivity
{
	private final String TAG = "SplashActivity";
	private static final String RESULTCODE = "0";
	@InjectResource(id = R.string.app_name)
	private String mAppName;
	private SplashActivity mActivity;
	@InjectResource(id = R.drawable.easy_pay_logo)
	private Drawable logoDrawable;
	private IConfig config;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mActivity = this;
		createShoutcut();
		checkUpdate();
		MobclickAgent.updateOnlineConfig(mActivity);
		new Timer().schedule(new TimerTask()
		{

			@Override
			public void run()
			{
				checkLocalStore();
				SubmitEvent event = new SubmitEvent(1);
				Constant.eventBus.post(event);
			}
		}, 3 * 1000);
	}

	private void checkUpdate()
	{
		Response.Listener<String> listener = new Response.Listener<String>()
		{

			@Override
			public void onResponse(String response)
			{
				XStream xStream = new XStream(new DomDriver());
				try
				{
					String ecryptString = response.substring(1,
							response.length());
					byte[] decrypt = ecryptString.getBytes();
					String indexByte = response.substring(0, 1);
					String result = EncryptUtil.decrypt(new String(decrypt),
							Integer.parseInt(indexByte));
					Logger.d(TAG, "响应报文--->" + result);
					xStream.addDefaultImplementation(CheckUpdateRespBody.class,
							RespBody.class);
					xStream.alias("operation_response", ResponseEntity.class);
					xStream.alias("msgheader", RespHeader.class);
					xStream.alias("retinfo", RespRetInfo.class);
					xStream.alias("msgbody", CheckUpdateRespBody.class);
					ResponseEntity checkUpdateResp = (ResponseEntity) xStream
							.fromXML(result);
					RespHeader respHeader = checkUpdateResp.getMsgheader();
					if (RESULTCODE.equals(respHeader.getRetinfo().getRettype()))
					{
						String reqToken = respHeader.getReq_token();
						IConfig config = PreferenceConfig
								.getPreferenceConfig(mActivity);
						config.loadConfig();
						config.setString(Constant.REQTOKEN, reqToken);
					}
				}
				catch (Exception ex)
				{
					Logger.e(TAG, "解析XML异常");
				}
			}
		};
		Response.ErrorListener errorListener = new Response.ErrorListener()
		{

			@Override
			public void onErrorResponse(VolleyError error)
			{
			}

		};
		ApiFucUtil.checkUpdate(AppApplication.getInstance(), listener,
				errorListener);
	}

	public void onEvent(BaseEvent event)
	{
		if (event instanceof SubmitEvent)
		{
			int id = ((SubmitEvent) event).getId();
			Logger.e(TAG, String.valueOf(id));
		}
	}

	private void checkLocalStore()
	{
		String userName = config.getString(ActivityConstant.USERNAME, "");
		String gesturePwd = config.getString(ActivityConstant.GESTUREPWD, "");
		Intent intent = new Intent();
		if (!"".contains(userName) && !"".equals(gesturePwd))
		{
			intent.setClass(mActivity, LockActivity.class);
		}
		else if (!"".equals(userName))
		{
			intent.setClass(mActivity, AccountActivity.class);
		}
		else
		{
			intent.setClass(mActivity, LoginActivity.class);
		}
		startActivity(intent);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(mActivity);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(mActivity);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		Constant.eventBus.register(mActivity);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		Constant.eventBus.unregister(mActivity);
	}

	private void createShoutcut()
	{
		config = PreferenceConfig.getPreferenceConfig(mActivity);
		config.loadConfig();
		boolean spalshCut = config.getBoolean(ActivityConstant.SPALSHCUTSHOUT,
				false);
		if (!ShoutCutUtil.hasShortcut(mActivity, mAppName))
		{
			if (!spalshCut)
			{
				String packageName = mActivity.getPackageName();
				int logo_id = getResources().getIdentifier("easy_pay_logo",
						"drawable", packageName);
				ShoutCutUtil.creatShortCut(mActivity, mAppName, logo_id);
				config.setBoolean(ActivityConstant.SPALSHCUTSHOUT, true);
			}
		}
	}
}
