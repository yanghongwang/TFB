package com.cn.tfb.ui;

import com.cn.tfb.R;
import com.cn.tfb.config.Constant;
import com.cn.tfb.event.type.BaseEvent;
import com.cn.tfb.ioc.InjectView;
import com.cn.tfb.util.InputValidateUtil;
import com.cn.tfb.util.PhoneInfoUtil;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoginActivity extends BaseActivity implements OnClickListener
{
	private final String TAG = "LoginActivity";
	private LoginActivity mActivity;
	@InjectView(id = R.id.rl_back)
	private RelativeLayout rl_back;
	@InjectView(id = R.id.et_phone)
	private EditText et_phone;
	@InjectView(id = R.id.et_pwd)
	private EditText et_pwd;
	@InjectView(id = R.id.btn_login)
	private Button btn_login;
	@InjectView(id = R.id.btn_gusture_login)
	private Button btn_gusture_login;
	@InjectView(id = R.id.tv_forget)
	private TextView tv_forget;
	@InjectView(id = R.id.tv_register)
	private TextView tv_register;
	private String phone, pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mActivity = this;
		initLayout();
	}

	private void initLayout()
	{
		et_phone.setText(PhoneInfoUtil.getPhoneNumber());
		btn_login.setOnClickListener(this);
		btn_gusture_login.setOnClickListener(this);
		tv_forget.setOnClickListener(this);
		tv_register.setOnClickListener(this);
	}

	public void onEvent(BaseEvent event)
	{

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

	private void shakeAnimation(View view)
	{
		Animation animation = AnimationUtils.loadAnimation(mActivity,
				R.anim.shake_x);
		view.startAnimation(animation);
	}

	private boolean checkInput()
	{
		phone = et_phone.getText().toString();
		pwd = et_pwd.getText().toString();
		if (null == phone || "".equals(phone))
		{
			shakeAnimation(et_phone);
			return false;
		}
		if (!InputValidateUtil.checkPhoneNumber(phone))
		{
			shakeAnimation(et_phone);
			return false;
		}
		if (null == pwd || "".equals(pwd))
		{
			shakeAnimation(et_pwd);
			return false;
		}
		if (!InputValidateUtil.checkPwd(pwd))
		{
			shakeAnimation(et_pwd);
			return false;
		}

		return true;
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_login:
				if (checkInput())
				{
					
				}
				break;
			case R.id.btn_gusture_login:

				break;
			case R.id.tv_forget:

				break;
			case R.id.tv_register:

				break;
		}
	}
}
