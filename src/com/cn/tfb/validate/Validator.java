package com.cn.tfb.validate;

import android.widget.EditText;

public abstract class Validator
{
	protected String errMsg;

	public Validator(String msg)
	{
		errMsg = msg;
	}

	public abstract boolean isValid(EditText et);

	public boolean hasErrMsg()
	{
		return errMsg != null;
	}

	public String getErrMsg()
	{
		return errMsg;
	}
}
