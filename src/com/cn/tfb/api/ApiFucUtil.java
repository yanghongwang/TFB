package com.cn.tfb.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.cn.tfb.AppApplication;
import com.cn.tfb.config.Constant;
import com.cn.tfb.util.EncryptUtil;
import com.cn.tfb.util.VersionUtil;
import com.cn.tfb.volley.AuthFailureError;
import com.cn.tfb.volley.Request;
import com.cn.tfb.volley.Response;
import com.cn.tfb.volley.util.StringRequest;
import com.cn.tfb.xml.XmlUtil;

import android.annotation.SuppressLint;
import android.content.Context;

public class ApiFucUtil
{
	public static void login()
	{

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
				apiNameFunc, apiName));

		String msgBodyValue = XmlUtil.parseXmlToNodeStr(initBody());

		LinkedHashMap<String, String> param = new LinkedHashMap<String, String>();
		param.put("msgheader", msgHeaderValue);
		param.put("msgbody", msgBodyValue);
		String paramValue = XmlUtil.parseXmlToStr("operation_request", param);

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
	 * @return
	 */
	private static LinkedHashMap<String, String> initBody()
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
	 * @return
	 */
	private static LinkedHashMap<String, String> initHeader(String apiNameFunc,
			String apiName)
	{
		return initHeader(apiNameFunc, apiName, null, null, null);
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
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	private static LinkedHashMap<String, String> initHeader(String apiNameFunc,
			String apiName, String authorId, String agentId, String agentTypeId)
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

		LinkedHashMap<String, String> msgHeader = new LinkedHashMap<String, String>();
		msgHeader.put("req_token", "");
		msgHeader.put("req_time",
				new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
		msgHeader.put("au_token", "zHBBUseQrqzJlA7l/L911GYA");
		msgHeader.put("req_version",
				VersionUtil.getVersionName(AppApplication.getInstance()));
		msgHeader.put("req_appenv", "00");
		msgHeader.put("req_bkenv", "1");
		msgHeader.put("channelinfo", channelValue);
		return msgHeader;

	}

}
