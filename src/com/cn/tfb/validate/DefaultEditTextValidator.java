package com.cn.tfb.validate;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.cn.tfb.R;

public class DefaultEditTextValidator implements EditTextValidator
{

	public DefaultEditTextValidator(EditText editText, AttributeSet attrs,
			Context context)
	{
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.FormEditText);
		emptyAllowed = typedArray.getBoolean(
				R.styleable.FormEditText_emptyAllowed, false);
		validType = ValidType.valueof(typedArray.getInt(
				R.styleable.FormEditText_validType, 11));
		errMsg = typedArray.getString(R.styleable.FormEditText_errMsg);
		classType = typedArray.getString(R.styleable.FormEditText_classType);
		customRegexp = typedArray
				.getString(R.styleable.FormEditText_customRegexp);
		emptyMsg = typedArray.getString(R.styleable.FormEditText_emptyMsg);
		customFormat = typedArray
				.getString(R.styleable.FormEditText_customFormat);
		typedArray.recycle();

		setEditText(editText);
		resetValidators(context);

	}

	@Override
	public void addValidator(Validator theValidator)
			throws IllegalArgumentException
	{
		if (theValidator == null) { throw new IllegalArgumentException(
				"theValidator argument should not be null"); }
		mValidator.enqueue(theValidator);
	}

	public String getClassType()
	{
		return classType;
	}

	public String getCustomRegexp()
	{
		return customRegexp;
	}

	public EditText getEditText()
	{
		return editText;
	}

	public String getErrMsg()
	{
		return errMsg;
	}

	public ValidType getValidType()
	{
		return validType;
	}

	@Override
	public TextWatcher getTextWatcher()
	{
		if (tw == null)
		{
			tw = new TextWatcher()
			{

				@Override
				public void afterTextChanged(Editable s)
				{
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after)
				{
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count)
				{
					if (s != null && s.length() > 0
							&& editText.getError() != null)
					{
						editText.setError(null);
					}
				}
			};
		}
		return tw;
	}

	@Override
	public boolean isEmptyAllowed()
	{
		return emptyAllowed;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void resetValidators(Context context)
	{
		defaultemptyMsg = context.getString(R.string.err_must_not_empty);
		setEmptyMsg(emptyMsg);

		mValidator = new AndValidator();
		Validator toAdd;

		switch (validType)
		{
			case REGEXP:
				toAdd = new PatternValidator(errMsg, ValidType.REGEXP,
						customRegexp);
				break;
			case DIGITS:
				toAdd = new PatternValidator(
						TextUtils.isEmpty(errMsg) ? context.getString(R.string.err_numeric_allowed)
								: errMsg, ValidType.DIGITS);
				break;

			case ALPHA:
				toAdd = new PatternValidator(
						TextUtils.isEmpty(errMsg) ? context.getString(R.string.err_are_not_allowed)
								: errMsg, ValidType.ALPHA);
				break;
			case ALPHANUMERIC:
				toAdd = new PatternValidator(
						TextUtils.isEmpty(errMsg) ? context.getString(R.string.err_contain_special_character)
								: errMsg, ValidType.ALPHANUMERIC);
				break;
			case EMAIL:
				toAdd = new PatternValidator(
						TextUtils.isEmpty(errMsg) ? context.getString(R.string.err_email_not_valid)
								: errMsg, ValidType.EMAIL);
				break;
			case CREDITCARD:
				toAdd = new PatternValidator(
						TextUtils.isEmpty(errMsg) ? context.getString(R.string.err_creditcard_not_valid)
								: errMsg, ValidType.CREDITCARD);
				break;
			case PHONE:
				toAdd = new PatternValidator(
						TextUtils.isEmpty(errMsg) ? context.getString(R.string.err_phone_not_valid)
								: errMsg, ValidType.PHONE);
				break;
			case DEMAIN:
				toAdd = new PatternValidator(
						TextUtils.isEmpty(errMsg) ? context.getString(R.string.err_domain_not_valid)
								: errMsg, ValidType.DEMAIN);
				break;
			case IP:
				toAdd = new PatternValidator(
						TextUtils.isEmpty(errMsg) ? context.getString(R.string.err_ip_not_valid)
								: errMsg, ValidType.IP);
				break;
			case URL:
				toAdd = new PatternValidator(
						TextUtils.isEmpty(errMsg) ? context.getString(R.string.err_url_not_valid)
								: errMsg, ValidType.URL);
				break;

			case DATE:
				toAdd = new PatternValidator(
						TextUtils.isEmpty(errMsg) ? context.getString(R.string.err_date_not_valid)
								: errMsg, ValidType.DATE, customFormat);
				break;
			case NOCHECK:
				toAdd = new PatternValidator("", ValidType.NOCHECK);
				break;
			case CUSTOM:

				if (classType == null) { throw new RuntimeException(
						"Trying to create a custom validator but no classType has been specified."); }
				if (TextUtils.isEmpty(errMsg)) { throw new RuntimeException(
						String.format(
								"Trying to create a custom validator (%s) but no error string specified.",
								classType)); }

				Class<? extends Validator> customValidatorClass;
				try
				{
					Class<?> loadedClass = this.getClass().getClassLoader()
							.loadClass(classType);

					if (!Validator.class.isAssignableFrom(loadedClass)) { throw new RuntimeException(
							String.format(
									"Custom validator (%s) does not extend %s",
									classType, Validator.class.getName())); }
					customValidatorClass = (Class<? extends Validator>) loadedClass;
				}
				catch (ClassNotFoundException e)
				{
					throw new RuntimeException(String.format(
							"Unable to load class for custom validator (%s).",
							classType));
				}

				try
				{
					toAdd = customValidatorClass.getConstructor(String.class)
							.newInstance(errMsg);
				}
				catch (Exception e)
				{
					throw new RuntimeException(
							String.format(
									"Unable to construct custom validator (%s) with argument: %s",
									classType, errMsg));
				}

				break;
			case PWD:
				toAdd = new PatternValidator(
						TextUtils.isEmpty(errMsg) ? context.getString(R.string.err_must_not_empty)
								: errMsg, ValidType.PWD);
				break;
			case VERIFY:
				toAdd = new PatternValidator(
						TextUtils.isEmpty(errMsg) ? context.getString(R.string.err_must_not_empty)
								: errMsg, ValidType.VERIFY);
				break;
			case OTHER:
				toAdd = new PatternValidator(
						TextUtils.isEmpty(errMsg) ? context.getString(R.string.err_must_not_empty)
								: errMsg, ValidType.OTHER);
				break;
			case COMPARE:
				toAdd = new SameValueValidator(editText,
						TextUtils.isEmpty(errMsg) ? context
								.getString(R.string.err_must_not_empty)
								: errMsg);
			default:
				toAdd = new PatternValidator(
						TextUtils.isEmpty(errMsg) ? context.getString(R.string.err_must_not_empty)
								: errMsg, ValidType.EMPTY);
				break;
		}

		MultiValidator tmpValidator;
		if (!emptyAllowed)
		{
			tmpValidator = new AndValidator();
			tmpValidator.enqueue(new PatternValidator(emptyMsgActual,
					ValidType.EMPTY));
			tmpValidator.enqueue(toAdd);
		}
		else
		{
			tmpValidator = new OrValidator(toAdd.getErrMsg(), new NotValidator(
					null, new PatternValidator(null, ValidType.EMPTY)), toAdd);
		}

		addValidator(tmpValidator);
	}

	public void setClassType(String classType, String errMsg, Context context)
	{
		validType = ValidType.CUSTOM;
		this.classType = classType;
		this.errMsg = errMsg;
		resetValidators(context);
	}

	public void setCustomRegexp(String customRegexp, Context context)
	{
		validType = ValidType.REGEXP;
		this.customRegexp = customRegexp;
		resetValidators(context);
	}

	public void setEditText(EditText editText)
	{
		if (this.editText != null)
		{
			this.editText.removeTextChangedListener(getTextWatcher());
		}
		this.editText = editText;
		editText.addTextChangedListener(getTextWatcher());
	}

	public void setEmptyAllowed(boolean emptyAllowed, Context context)
	{
		this.emptyAllowed = emptyAllowed;
		resetValidators(context);
	}

	public void setEmptyMsg(String emptyMsg)
	{
		if (!TextUtils.isEmpty(emptyMsg))
		{
			emptyMsgActual = emptyMsg;
		}
		else
		{
			emptyMsgActual = defaultemptyMsg;
		}
	}

	public void setErrMsg(String errMsg, Context context)
	{
		this.errMsg = errMsg;
		resetValidators(context);
	}

	public void setValidType(ValidType validType, Context context)
	{
		this.validType = validType;
		resetValidators(context);
	}

	@Override
	public boolean validity()
	{
		return validity(true);
	}

	@Override
	public boolean validity(boolean showUIError)
	{
		boolean isValid = mValidator.isValid(editText);
		if (!isValid && showUIError)
		{
			showUIError();
		}
		return isValid;
	}

	@Override
	public void showUIError()
	{
		if (mValidator.hasErrMsg())
		{
			editText.setError(mValidator.getErrMsg());
		}
	}

	private TextWatcher tw;

	private String defaultemptyMsg;

	protected MultiValidator mValidator;

	protected String errMsg;

	protected boolean emptyAllowed;

	protected EditText editText;

	protected ValidType validType;

	protected String classType;

	protected String customRegexp;

	protected String customFormat;

	protected String emptyMsgActual;

	protected String emptyMsg;

}
