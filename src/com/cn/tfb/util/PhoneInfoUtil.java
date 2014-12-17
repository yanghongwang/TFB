package com.cn.tfb.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.cn.tfb.AppApplication;

public class PhoneInfoUtil
{
	public static String getPhoneNumber()
	{
		String phoneNumber = ((TelephonyManager) AppApplication.getInstance()
				.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
		if (null != phoneNumber && phoneNumber.startsWith("+86"))
		{
			phoneNumber = phoneNumber.substring(3);
		}
		return phoneNumber;
	}

	public static String getMacAddress()
	{
		WifiManager wifi = (WifiManager) AppApplication.getInstance()
				.getSystemService(Context.WIFI_SERVICE);

		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}
}
