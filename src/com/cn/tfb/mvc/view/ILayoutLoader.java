package com.cn.tfb.mvc.view;

import com.cn.tfb.exception.NoSuchNameLayoutException;

import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @author hzx 2014��4��21��
 * @version V1.0
 */
public interface ILayoutLoader
{
	public int getLayoutID(String resIDName) throws ClassNotFoundException,
			IllegalArgumentException, IllegalAccessException,
			NameNotFoundException, NoSuchNameLayoutException;
}
