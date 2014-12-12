package com.cn.tfb.ui;

import java.util.Timer;
import java.util.TimerTask;

import com.cn.tfb.R;
import com.cn.tfb.ioc.InjectResource;
import com.cn.tfb.util.ShoutCutUtil;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class SplashActivity extends BaseActivity
{
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
		new Timer().schedule(new TimerTask()
		{

			@Override
			public void run()
			{

			}
		}, 3 * 1000);
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
