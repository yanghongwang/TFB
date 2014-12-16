package com.cn.tfb.validate;

public enum ValidType
{
	REGEXP(0), DIGITS(1), ALPHA(2), ALPHANUMERIC(3), EMAIL(4), CREDITCARD(5), PHONE(
			6), DEMAIN(7), IP(8), URL(9), DATE(10), NOCHECK(11), EMPTY(12), CUSTOM(
			13), PERSONE(14), PERSONFULL(15),PWD(16),VERIFY(17),OTHER(18);
	private int value = 0;

	private ValidType(int value)
	{
		this.value = value;
	}

	public static ValidType valueof(int value)
	{
		switch (value)
		{
			case 0:
				return REGEXP;
			case 1:
				return DIGITS;
			case 2:
				return ALPHA;
			case 3:
				return ALPHANUMERIC;
			case 4:
				return EMAIL;
			case 5:
				return CREDITCARD;
			case 6:
				return PHONE;
			case 7:
				return DEMAIN;
			case 8:
				return IP;
			case 9:
				return URL;
			case 10:
				return DATE;
			case 11:
				return NOCHECK;
			case 12:
				return EMPTY;
			case 13:
				return CUSTOM;
			case 14:
				return PERSONE;
			case 15:
				return PERSONFULL;
			case 16:
				return PWD;
			case 17:
				return VERIFY;
			case 18:
				return OTHER;
			default:
				return null;
		}
	}
	public int value()
	{
		return this.value;
	}
}
