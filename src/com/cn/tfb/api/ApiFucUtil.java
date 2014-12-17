package com.cn.tfb.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;

import com.cn.tfb.AppApplication;
import com.cn.tfb.config.ActivityConstant;
import com.cn.tfb.config.Constant;
import com.cn.tfb.config.IConfig;
import com.cn.tfb.config.PreferenceConfig;
import com.cn.tfb.log.Logger;
import com.cn.tfb.util.EncryptUtil;
import com.cn.tfb.util.PhoneInfoUtil;
import com.cn.tfb.util.VersionUtil;
import com.cn.tfb.volley.AuthFailureError;
import com.cn.tfb.volley.Request;
import com.cn.tfb.volley.Response;
import com.cn.tfb.volley.util.StringRequest;
import com.cn.tfb.xml.XmlUtil;

public class ApiFucUtil
{
	private static String TAG = "ApiFucUtil";

	/***
	 * 登录
	 * 
	 * @param context
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param listener
	 * @param errorListener
	 */
	public static void login(Context context, String userName, String password,
			Response.Listener<String> listener,
			Response.ErrorListener errorListener)
	{
		String url = Constant.RUQUESTURL;
		String apiNameFunc = "login";
		String apiName = "ApiAuthorInfoV2";
		final int index = EncryptUtil.createRandomkeySort();

		String msgHeaderValue = XmlUtil.parseXmlToNodeStr(initHeader(
				apiNameFunc, apiName, index));

		String msgBodyValue = XmlUtil.parseXmlToNodeStr(initLoginBody(userName,
				password));

		LinkedHashMap<String, String> param = new LinkedHashMap<String, String>();
		param.put("msgheader", msgHeaderValue);
		param.put("msgbody", msgBodyValue);
		String paramValue = XmlUtil.parseXmlToStr("operation_request", param);
		Logger.d(TAG, "请求报文--->" + paramValue);
		final String requestStr = EncryptUtil.encrypt(paramValue, index);

		StringRequest request = new StringRequest(Request.Method.POST, url,
				listener, errorListener)
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError
			{
				Map<String, String> params = new HashMap<String, String>();
				params.put(String.valueOf(index), requestStr);
				return params;
			}
		};
		AppApplication.getInstance().addToRequestQueue(request);
	}

	/**
	 * 检测更新
	 * 
	 * @param context
	 * @param listener
	 * @param errorListener
	 */
	public static void checkUpdate(Context context,
			Response.Listener<String> listener,
			Response.ErrorListener errorListener)
	{
		String url = Constant.RUQUESTURL;
		String apiNameFunc = "checkAppVersion";
		String apiName = "ApiAppInfo";
		final int index = EncryptUtil.createRandomkeySort();

		String msgHeaderValue = XmlUtil.parseXmlToNodeStr(initHeader(
				apiNameFunc, apiName, index));

		String msgBodyValue = XmlUtil.parseXmlToNodeStr(initCheckUpdateBody());

		LinkedHashMap<String, String> param = new LinkedHashMap<String, String>();
		param.put("msgheader", msgHeaderValue);
		param.put("msgbody", msgBodyValue);
		String paramValue = XmlUtil.parseXmlToStr("operation_request", param);
		Logger.d(TAG, "请求报文--->" + paramValue);
		final String requestStr = EncryptUtil.encrypt(paramValue, index);

		StringRequest request = new StringRequest(Request.Method.POST, url,
				listener, errorListener)
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError
			{
				Map<String, String> params = new HashMap<String, String>();
				params.put(String.valueOf(index), requestStr);
				return params;
			}
		};
		AppApplication.getInstance().addToRequestQueue(request);
	}

	/**
	 * 初始化请求体
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * 
	 * @return
	 */
	private static LinkedHashMap<String, String> initLoginBody(String userName,
			String password)
	{
		LinkedHashMap<String, String> msgBody = new LinkedHashMap<String, String>();
		msgBody.put("mobile", userName);
		msgBody.put("gesturepasswd", "");
		msgBody.put("paypasswd", password);
		return msgBody;
	}

	/**
	 * 初始化请求体
	 * 
	 * @return
	 */
	private static LinkedHashMap<String, String> initCheckUpdateBody()
	{
		LinkedHashMap<String, String> msgBody = new LinkedHashMap<String, String>();
		msgBody.put("apptype", Constant.APPTYPE);
		msgBody.put("appversion",
				VersionUtil.getVersionName(AppApplication.getInstance()));
		return msgBody;
	}

	/**
	 * 初始化请求头
	 * 
	 * @param apiNameFunc
	 *            方法名
	 * @param apiName
	 *            接口名
	 * @param index
	 *            随机数
	 * @return
	 */
	private static LinkedHashMap<String, String> initHeader(String apiNameFunc,
			String apiName, int index)
	{
		return initHeader(apiNameFunc, apiName, null, null, null, index);
	}

	/**
	 * 初始化请求头
	 * 
	 * @param apiNameFunc
	 *            方法名
	 * @param apiName
	 *            接口名
	 * @param authorId
	 *            操作员ID
	 * @param agentId
	 *            代理商ID
	 * @param agentTypeId
	 *            代理商类型
	 * @param index
	 *            随机数
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	private static LinkedHashMap<String, String> initHeader(String apiNameFunc,
			String apiName, String authorId, String agentId,
			String agentTypeId, int index)
	{
		LinkedHashMap<String, String> channelInfo = new LinkedHashMap<String, String>();
		channelInfo.put("api_name_func", apiNameFunc);
		channelInfo.put("api_name", apiName);
		if (null != authorId)
		{
			channelInfo.put("authorid", authorId);
		}
		if (null != agentId)
		{
			channelInfo.put("agentid", agentId);
		}
		if (null != agentTypeId)
		{
			channelInfo.put("agenttypeid", agentTypeId);
		}

		String channelValue = XmlUtil.parseXmlToNodeStr(channelInfo);
		String time = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		LinkedHashMap<String, String> msgHeader = new LinkedHashMap<String, String>();
		msgHeader.put("req_token", getReqToken(time, index));
		msgHeader.put("req_time", time);
		msgHeader.put("au_token", "zHBBUseQrqzJlA7l/L911GYA");
		msgHeader.put("req_version",
				VersionUtil.getVersionName(AppApplication.getInstance()));
		msgHeader.put("req_appenv", "00");
		msgHeader.put("req_bkenv", "1");
		msgHeader.put("channelinfo", channelValue);
		return msgHeader;

	}

	/**
	 * 获取动态码
	 * 
	 * @param time
	 * @param index
	 * @return
	 */
	private static String getReqToken(String time, int index)
	{
		IConfig config = PreferenceConfig.getPreferenceConfig(AppApplication
				.getInstance());
		config.loadConfig();
		String reqToken = config.getString(Constant.REQTOKEN, "");
		String userName = config.getString(ActivityConstant.USERNAME, "");
		String pwd = config.getString(ActivityConstant.USERPWD, "");
		if ("".equals(reqToken))
		{
			StringBuffer sb = new StringBuffer();
			sb.append(PhoneInfoUtil.getMacAddress());
			sb.append("@@");
			sb.append(time);
			sb.append("@@");
			sb.append(userName);
			sb.append("@@");
			sb.append(pwd);
			String str = EncryptUtil.encrypt(sb.toString(), index);
			reqToken = index + str;
		}
		return reqToken;
	}

}
