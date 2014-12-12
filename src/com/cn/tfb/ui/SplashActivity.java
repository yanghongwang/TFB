package com.cn.tfb.ui;

import java.util.Timer;
import java.util.TimerTask;

import com.cn.tfb.R;
import com.cn.tfb.config.Constant;
import com.cn.tfb.event.type.BaseEvent;
import com.cn.tfb.event.type.SubmitEvent;
import com.cn.tfb.ioc.InjectResource;
import com.cn.tfb.log.Logger;
import com.cn.tfb.util.ShoutCutUtil;
import com.umeng.analytics.MobclickAgent;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class SplashActivity extends BaseActivity
{
	private static final String TAG = "SplashActivity";
	@InjectResource(id = R.string.app_name)
	private String mAppName;
	private static SplashActivity mInstance;
	@InjectResource(id = R.drawable.easy_pay_logo)
	private Drawable logoDrawable;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mInstance = this;
		createShoutcut();
		MobclickAgent.updateOnlineConfig(mInstance);
		new Timer().schedule(new TimerTask()
		{

			@Override
			public void run()
			{
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

	@Override
	protected void onResume()
	{
		super.onResume();
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(mInstance);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(mInstance);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		Constant.eventBus.register(mInstance);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		Constant.eventBus.unregister(mInstance);
	}

	private void createShoutcut()
	{
		if (!ShoutCutUtil.hasShortcut(mInstance, mAppName))
		{
			int logo_id = getResources().getIdentifier("easy_pay_logo",
					"drawable", "com.cn.tfb");
			ShoutCutUtil.creatShortCut(mInstance, mAppName, logo_id);
		}
	}
}
