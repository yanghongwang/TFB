package com.cn.tfb.util;

import android.os.Build;

/**
 * ���ڶ�汾���ݼ��
 * 
 * @author hzx 2014��11��21��
 * @version V1.0
 */
public class AndroidVersionCheckUtils
{
	private AndroidVersionCheckUtils()
	{

	}

	/**
	 * ��ǰAndroidϵͳ�汾�Ƿ��ڣ� Donut�� Android 1.6������ 2014��4��21��
	 * 
	 * @return
	 */
	public static boolean hasDonut()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT;
	}

	/**
	 * ��ǰAndroidϵͳ�汾�Ƿ��ڣ� Eclair�� Android 2.0�� ���� 2014��4��21��
	 * 
	 * @return
	 */
	public static boolean hasEclair()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR;
	}

	/**
	 * ��ǰAndroidϵͳ�汾�Ƿ��ڣ� Froyo�� Android 2.2�� Android 2.2���� 2014��4��21��
	 * 
	 * @return
	 */
	public static boolean hasFroyo()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * ��ǰAndroidϵͳ�汾�Ƿ��ڣ� Gingerbread�� Android 2.3x�� Android 2.3x ���� 2014��4��21��
	 * 
	 * @return
	 */
	public static boolean hasGingerbread()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}
}
