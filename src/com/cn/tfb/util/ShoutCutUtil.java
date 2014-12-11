package com.cn.tfb.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;

public class ShoutCutUtil
{
	/**
	 * ��ӿ�ݷ�ʽ
	 * */
	public static void creatShortCut(Activity activity, String shortcutName,
			int resourceId)
	{
		Intent intent = new Intent();
		intent.setClass(activity, activity.getClass());
		/* ����������Ϊ����ж��Ӧ�õ�ʱ��ͬʱɾ�������ݷ�ʽ */
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");

		Intent shortcutintent = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");

		// �������ظ�����
		shortcutintent.putExtra("duplicate", false);
		// ��Ҫ��ʵ������
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
		// ���ͼƬ
		Parcelable icon = Intent.ShortcutIconResource.fromContext(
				activity.getApplicationContext(), resourceId);
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		// ������ͼƬ�����еĳ��������
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		// ���͹㲥��OK
		activity.sendBroadcast(shortcutintent);
	}

	/**
	 * ɾ����ݷ�ʽ
	 * */
	public static void deleteShortCut(Activity activity, String shortcutName)
	{
		Intent shortcut = new Intent(
				"com.android.launcher.action.UNINSTALL_SHORTCUT");
		// ��ݷ�ʽ������
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
		/** �ĳ����·�ʽ�ܹ��ɹ�ɾ����������ɾ���ʹ�����Ҫ��Ӧ�����ҵ���ݷ�ʽ���ɹ�ɾ�� **/
		Intent intent = new Intent();
		intent.setClass(activity, activity.getClass());
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		activity.sendBroadcast(shortcut);
	}

	/**
	 * �ж��Ƿ���ڿ�ݷ�ʽ
	 * */
	public static boolean hasShortcut(Activity activity, String shortcutName)
	{
		String url = "";
		int systemversion = Integer.parseInt(android.os.Build.VERSION.SDK);
		if (systemversion < 8)
		{
			url = "content://com.android.launcher.settings/favorites?notify=true";
		}
		else
		{
			url = "content://com.android.launcher2.settings/favorites?notify=true";
		}
		ContentResolver resolver = activity.getContentResolver();
		Cursor cursor = resolver.query(Uri.parse(url), null, "title=?",
				new String[]
				{ shortcutName }, null);
		if (cursor != null && cursor.moveToFirst())
		{
			cursor.close();
			return true;
		}
		return false;
	}
}
