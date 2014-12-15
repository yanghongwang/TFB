package com.cn.tfb.util;

import java.util.regex.Pattern;

public class InputValidateUtil
{
	public static boolean checkPhoneNumber(String phoneNumber)
	{
		String[] mobilePhoneStarts =
		{
				"106|�ƶ�", "130|��ͨ", "131|��ͨ", "132|��ͨ", "133|����", "134|�ƶ�",
				"135|�ƶ�", "136|�ƶ�", "137|�ƶ�", "138|�ƶ�", "139|�ƶ�", "145|��ͨ",
				"147|�ƶ�", "150|�ƶ�", "151|�ƶ�", "152|�ƶ�", "153|����", "155|��ͨ",
				"156|��ͨ", "157|�ƶ�", "158|�ƶ�", "159|�ƶ�", "180|����", "181|����",
				"182|�ƶ�", "183|�ƶ�", "184|�ƶ�", "185|��ͨ", "186|��ͨ", "187|�ƶ�",
				"188|�ƶ�", "189|����"
		};

		String regex1MobilePhone = "^\\d{11}$";
		String regex2MobilePhone = "^(86)\\d{11}$";
		String regex3MobilePhone = "^(\\+86)\\d{11}$";

		if (Pattern.matches(regex1MobilePhone, phoneNumber))
		{
			for (String mobilePhoneStart : mobilePhoneStarts)
			{
				if (mobilePhoneStart.substring(0, 3).equalsIgnoreCase(
						phoneNumber.substring(0, 3))) { return true; }
			}
		}

		if (Pattern.matches(regex2MobilePhone, phoneNumber))
		{
			for (String mobilePhoneStart : mobilePhoneStarts)
			{
				if (mobilePhoneStart.substring(0, 3).equalsIgnoreCase(
						phoneNumber.substring(2, 5))) { return true; }
			}
		}

		if (Pattern.matches(regex3MobilePhone, phoneNumber))
		{
			for (String mobilePhoneStart : mobilePhoneStarts)
			{
				if (mobilePhoneStart.substring(0, 3).equalsIgnoreCase(
						phoneNumber.substring(3, 6))) { return true; }
			}
		}
		return false;
	}
	
	public static boolean checkPwd(String pwd)
	{
		return false;
	}
}
