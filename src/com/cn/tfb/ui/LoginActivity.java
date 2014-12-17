package com.cn.tfb.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.tfb.AppApplication;
import com.cn.tfb.R;
import com.cn.tfb.api.ApiFucUtil;
import com.cn.tfb.config.Constant;
import com.cn.tfb.config.IConfig;
import com.cn.tfb.config.PreferenceConfig;
import com.cn.tfb.entity.LoginRespBody;
import com.cn.tfb.entity.RespBody;
import com.cn.tfb.entity.RespHeader;
import com.cn.tfb.entity.RespRetInfo;
import com.cn.tfb.entity.ResponseEntity;
import com.cn.tfb.event.type.BaseEvent;
import com.cn.tfb.ioc.InjectView;
import com.cn.tfb.log.Logger;
import com.cn.tfb.util.EncryptUtil;
import com.cn.tfb.util.InputValidateUtil;
import com.cn.tfb.util.PhoneInfoUtil;
import com.cn.tfb.validate.FormEditText;
import com.cn.tfb.volley.Response;
import com.cn.tfb.volley.VolleyError;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.umeng.analytics.MobclickAgent;

public class LoginActivity extends BaseActivity implements OnClickListener
{
	private final String TAG = "LoginActivity";
	private static final String RESULTCODE = "0";
	private LoginActivity mActivity;
	@InjectView(id = R.id.rl_back)
	private RelativeLayout rl_back;
	@InjectView(id = R.id.et_phone)
	private FormEditText et_phone;
	@InjectView(id = R.id.et_pwd)
	private FormEditText et_pwd;
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
		boolean isValidate = true;
		FormEditText[] formEditTexts =
		{
				et_phone, et_pwd
		};
		for (FormEditText formEditText : formEditTexts)
		{
			isValidate = formEditText.validity() && isValidate;
		}
		if (!isValidate)
		{
			if (null == phone || "".equals(phone))
			{
				shakeAnimation(et_phone);
			}
			if (!InputValidateUtil.checkPhoneNumber(phone))
			{
				shakeAnimation(et_phone);
			}
			if (null == pwd || "".equals(pwd))
			{
				shakeAnimation(et_pwd);
			}
		}
		return isValidate;
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_login:
				if (checkInput())
				{
					login();
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

	private void login()
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
					xStream.addDefaultImplementation(LoginRespBody.class,
							RespBody.class);
					xStream.alias("operation_response", ResponseEntity.class);
					xStream.alias("msgheader", RespHeader.class);
					xStream.alias("retinfo", RespRetInfo.class);
					xStream.alias("msgbody", LoginRespBody.class);
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
					else
					{
						btn_gusture_login.setVisibility(View.VISIBLE);
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
		ApiFucUtil.login(AppApplication.getInstance(), phone, pwd, listener,
				errorListener);
	}
}
