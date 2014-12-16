package com.cn.tfb.validate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import android.text.TextUtils;
import android.widget.EditText;

public class PatternValidator extends Validator
{
	private Pattern pattern;
	private ValidType validType;
	private String[] formats;
	private String regexp;
	private EditText otherEditText;

	public PatternValidator(String msg, ValidType validType)
	{
		super(msg);
		this.validType = validType;
	}

	public PatternValidator(String msg, ValidType validType, EditText editText)
	{
		super(msg);
		this.validType = validType;
		this.otherEditText = editText;
	}

	public PatternValidator(String msg, ValidType validType, String format)
	{
		super(msg);
		this.validType = validType;
		if (validType == ValidType.REGEXP)
		{
			formats = TextUtils.isEmpty(format) ? new String[]
			{ "DefaultDate", "DefaultTime", "DefaultDateTime" } : format
					.split(";");
		}
		else
		{
			regexp = format;
		}
	}

	@Override
	public boolean isValid(EditText et)
	{
		boolean flag = false;
		switch (validType)
		{
			case REGEXP:
				pattern = Pattern.compile(regexp);
				break;
			case DIGITS:
				flag = TextUtils.isDigitsOnly(et.getText());
				break;
			case ALPHA:
				pattern = Pattern.compile("^[a-zA-Z \\./-]*$");
				flag = pattern.matcher(et.getText()).matches();
				break;
			case ALPHANUMERIC:
				pattern = Pattern.compile("^[a-zA-Z0-9 \\./-]*$");
				flag = pattern.matcher(et.getText()).matches();
				break;
			case EMAIL:
				pattern = Pattern
						.compile("^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$");
				flag = pattern.matcher(et.getText()).matches();
				break;
			case CREDITCARD:
				flag = validateCardNumber(et.getText().toString());
				break;
			case PHONE:
				pattern = Pattern.compile("^[1][3-8]+\\d{9}$");
				flag = pattern.matcher(et.getText()).matches();
				break;
			case DEMAIN:
				pattern = Pattern
						.compile("^[0-9a-zA-Z]+[0-9a-zA-Z\\.-]*\\.[a-zA-Z]{2,4}$");
				flag = pattern.matcher(et.getText()).matches();

				break;
			case IP:
				pattern = Pattern
						.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
				flag = pattern.matcher(et.getText()).matches();
				break;
			case URL:
				pattern = Pattern.compile("^[a-zA-z]+://[^\\s]*$");
				flag = pattern.matcher(et.getText()).matches();
				break;
			case DATE:
				if (TextUtils.isEmpty(et.getText())) flag = true;
				String value = et.getText().toString();
				for (String _format : formats)
				{
					DateFormat format;
					if ("DefaultDate".equalsIgnoreCase(_format))
					{
						format = DateFormat.getDateInstance();
					}
					else if ("DefaultTime".equalsIgnoreCase(_format))
					{
						format = DateFormat.getTimeInstance();
					}
					else if ("DefaultDateTime".equalsIgnoreCase(_format))
					{
						format = DateFormat.getDateTimeInstance();
					}
					else
					{
						format = new SimpleDateFormat(_format);
					}
					Date date = null;
					try
					{
						date = format.parse(value);
					}
					catch (ParseException e)
					{
						flag = false;
					}
					if (date != null)
					{
						flag = true;
					}
				}
				break;
			case NOCHECK:
				flag = true;
				break;
			case EMPTY:
				flag = !TextUtils.isEmpty(et.getText());
				break;
			case PWD:
				pattern = Pattern.compile("^(.){6,20}$");
				flag = pattern.matcher(et.getText()).matches();
				break;
			case VERIFY:
				pattern = Pattern.compile("^[0-9]{4}$");
				flag = pattern.matcher(et.getText()).matches();
				break;
			case OTHER:
				flag = TextUtils.equals(et.getText(), otherEditText.getText());
				break;
			default:
				flag = pattern.matcher(et.getText()).matches();
				break;
		}
		return flag;
	}

	public static boolean validateCardNumber(String cardNumber)
			throws NumberFormatException
	{
		int sum = 0, digit, addend = 0;
		boolean doubled = false;
		for (int i = cardNumber.length() - 1; i >= 0; i--)
		{
			digit = Integer.parseInt(cardNumber.substring(i, i + 1));
			if (doubled)
			{
				addend = digit * 2;
				if (addend > 9)
				{
					addend -= 9;
				}
			}
			else
			{
				addend = digit;
			}
			sum += addend;
			doubled = !doubled;
		}
		return (sum % 10) == 0;
	}
}
