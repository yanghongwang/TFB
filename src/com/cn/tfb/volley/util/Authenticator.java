package com.cn.tfb.volley.util;

import com.cn.tfb.volley.AuthFailureError;

public interface Authenticator
{

	public String getAuthToken() throws AuthFailureError;

	public void invalidateAuthToken(String authToken);
}
