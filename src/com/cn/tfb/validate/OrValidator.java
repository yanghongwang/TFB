package com.cn.tfb.validate;

import android.widget.EditText;

public class OrValidator extends MultiValidator
{

	public OrValidator(String message, Validator... validators)
	{
		super(message, validators);
	}

	@Override
	public boolean isValid(EditText et)
	{
		for (Validator v : validators)
		{
			if (v.isValid(et))
			{
				return true;
			}
		}
		return false;
	}

}
