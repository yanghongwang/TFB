package com.cn.tfb.volley.util;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.cn.tfb.volley.AuthFailureError;
import com.cn.tfb.volley.Request;

public interface HttpStack
{

	public HttpResponse performRequest(Request<?> request,
			Map<String, String> additionalHeaders) throws IOException,
			AuthFailureError;

}
