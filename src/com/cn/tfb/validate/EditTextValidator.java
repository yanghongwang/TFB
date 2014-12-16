package com.cn.tfb.validate;

import android.content.Context;
import android.text.TextWatcher;


public interface EditTextValidator
{

	public void addValidator(Validator theValidator)
			throws IllegalArgumentException;

	public TextWatcher getTextWatcher();

	public boolean isEmptyAllowed();

	public void resetValidators(Context context);

	public boolean validity();

	public boolean validity(boolean showUIError);

	public void showUIError();

	ValidType validType=ValidType.REGEXP;
	

	final int DOMAINNAME = 7;


	final int CUSTOM = 11;

	final int PERSONNAME = 12;

	final int PERSONFULLNAME = 13;


}
