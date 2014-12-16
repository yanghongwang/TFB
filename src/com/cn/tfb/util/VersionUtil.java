package com.cn.tfb.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class VersionUtil
{
	public static String getVersionName(Context context)
	{
		try
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			String version = packInfo.versionName;
			return version;
		}
		catch (Exception e)
		{
		}
		return "1.0";
	}

	public static String getVersionCode(Context context)
	{
		PackageManager packageManager = context.getPackageManager();
		try
		{
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int versionCode = packInfo.versionCode;
			return versionCode + "";
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return "1.0";
	}

}
