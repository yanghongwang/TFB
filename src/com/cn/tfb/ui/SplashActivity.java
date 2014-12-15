package com.cn.tfb.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.cn.tfb.R;
import com.cn.tfb.config.ActivityConstant;
import com.cn.tfb.config.Constant;
import com.cn.tfb.config.IConfig;
import com.cn.tfb.config.PreferenceConfig;
import com.cn.tfb.event.type.BaseEvent;
import com.cn.tfb.event.type.SubmitEvent;
import com.cn.tfb.ioc.InjectResource;
import com.cn.tfb.log.Logger;
import com.cn.tfb.util.ShoutCutUtil;
import com.umeng.analytics.MobclickAgent;

public class SplashActivity extends BaseActivity
{
	private final String TAG = "SplashActivity";
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
