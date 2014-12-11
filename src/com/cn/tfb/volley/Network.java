package com.cn.tfb.volley;

public interface Network
{

	public NetworkResponse performRequest(Request<?> request)
			throws VolleyError;
}
