package com.cn.tfb.util;

import android.os.Build;

/**
 * 用于多版本兼容检测
 * 
 * @author hzx 2014年11月21日
 * @version V1.0
 */
public class AndroidVersionCheckUtils
{
	private AndroidVersionCheckUtils()
	{

	}

	/**
	 * 当前Android系统版本是否在（ Donut） Android 1.6或以上 2014年4月21日
	 * 
	 * @return
	 */
	public static boolean hasDonut()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT;
	}

	/**
	 * 当前Android系统版本是否在（ Eclair） Android 2.0或 以上 2014年4月21日
	 * 
	 * @return
	 */
	public static boolean hasEclair()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR;
	}

	/**
	 * 当前Android系统版本是否在（ Froyo） Android 2.2或 Android 2.2以上 2014年4月21日
	 * 
	 * @return
	 */
	public static boolean hasFroyo()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * 当前Android系统版本是否在（ Gingerbread） Android 2.3x或 Android 2.3x 以上 2014年4月21日
	 * 
	 * @return
	 */
	public static boolean hasGingerbread()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}
}
