package com.cn.tfb.util;

import java.util.regex.Pattern;

public class InputValidateUtil
{
	public static boolean checkPhoneNumber(String phoneNumber)
	{
		String[] mobilePhoneStarts =
		{
				"106|移动", "130|联通", "131|联通", "132|联通", "133|电信", "134|移动",
				"135|移动", "136|移动", "137|移动", "138|移动", "139|移动", "145|联通",
				"147|移动", "150|移动", "151|移动", "152|移动", "153|电信", "155|联通",
				"156|联通", "157|移动", "158|移动", "159|移动", "180|电信", "181|电信",
				"182|移动", "183|移动", "184|移动", "185|联通", "186|联通", "187|移动",
				"188|移动", "189|电信"
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
