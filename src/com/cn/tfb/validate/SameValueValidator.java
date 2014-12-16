package com.cn.tfb.validate;

import android.text.TextUtils;
import android.widget.EditText;

public class SameValueValidator extends Validator
{
	private final EditText otherEditText;

	public SameValueValidator(EditText otherEditText, String msg)
	{
		super(msg);
		this.otherEditText = otherEditText;
	}

	@Override
	public boolean isValid(EditText editText)
	{
		return TextUtils.equals(editText.getText(), otherEditText.getText());
	}
}
