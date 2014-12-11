package com.cn.tfb.volley.util;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.cn.tfb.volley.NetworkResponse;
import com.cn.tfb.volley.ParseError;
import com.cn.tfb.volley.Response;
import com.cn.tfb.volley.Response.ErrorListener;
import com.cn.tfb.volley.Response.Listener;

public class JsonObjectRequest extends JsonRequest<JSONObject>
{

	public JsonObjectRequest(int method, String url, JSONObject jsonRequest,
			Listener<JSONObject> listener, ErrorListener errorListener)
	{
		super(method, url, (jsonRequest == null) ? null : jsonRequest
				.toString(), listener, errorListener);
	}

	public JsonObjectRequest(String url, JSONObject jsonRequest,
			Listener<JSONObject> listener, ErrorListener errorListener)
	{
		this(jsonRequest == null ? Method.GET : Method.POST, url, jsonRequest,
				listener, errorListener);
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response)
	{
		try
		{
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONObject(jsonString),
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
