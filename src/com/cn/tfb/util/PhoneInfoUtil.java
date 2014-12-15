package com.cn.tfb.util;

import android.content.Context;
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
	
}
