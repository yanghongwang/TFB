package com.cn.tfb.validate;

import android.widget.EditText;

public class AndValidator extends MultiValidator
{
	public AndValidator(Validator... validators)
	{
		super(null, validators);
	}

	@Override
	public boolean isValid(EditText et)
	{
		for (Validator v : validators)
		{
			if (!v.isValid(et))
			{
				this.errMsg = v.getErrMsg();
				return false;
			}
		}
		return true;
	}
}
