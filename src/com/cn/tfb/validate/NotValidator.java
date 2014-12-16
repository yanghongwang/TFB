package com.cn.tfb.validate;

import android.widget.EditText;

public class NotValidator extends Validator
{
	private Validator validator;

	public NotValidator(String msg, Validator validator)
	{
		super(msg);
		this.validator= validator;
	}

	@Override
	public boolean isValid(EditText et)
	{
		return !validator.isValid(et);
	}

}
