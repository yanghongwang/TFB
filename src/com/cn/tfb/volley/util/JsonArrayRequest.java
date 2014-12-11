package com.cn.tfb.volley.util;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;

import com.cn.tfb.volley.NetworkResponse;
import com.cn.tfb.volley.ParseError;
import com.cn.tfb.volley.Response;
import com.cn.tfb.volley.Response.ErrorListener;
import com.cn.tfb.volley.Response.Listener;

public class JsonArrayRequest extends JsonRequest<JSONArray>
{

	public JsonArrayRequest(String url, Listener<JSONArray> listener,
			ErrorListener errorListener)
	{
		super(Method.GET, url, null, listener, errorListener);
	}

	/**
	 * 建返回的数据转换成JSON类型
	 */
	@Override
	protected Response<JSONArray> parseNetworkResponse(NetworkResponse response)
	{
		try
		{
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONArray(jsonString),
					HttpHeaderParser.parseCacheHeaders(response));
		}
		catch (UnsupportedEncodingException e)
		{
			return Response.error(new ParseError(e));
		}
		catch (JSONException je)
		{
			return Response.error(new ParseError(je));
		}
	}
}
