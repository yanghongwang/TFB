package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;

public class DoubleSerializer implements ObjectSerializer
{

	public final static DoubleSerializer instance = new DoubleSerializer();

	private DecimalFormat decimalFormat = null;

	public DoubleSerializer()
	{

	}

	public DoubleSerializer(DecimalFormat decimalFormat)
	{
		this.decimalFormat = decimalFormat;
	}

	public DoubleSerializer(String decimalFormat)
	{
		this(new DecimalFormat(decimalFormat));
	}

	public void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{
		SerializeWriter out = serializer.getWriter();

		if (object == null)
		{
			if (serializer.isEnabled(SerializerFeature.WriteNullNumberAsZero))
			{
				out.write('0');
			}
			else
			{
				out.writeNull();
			}
			return;
		}

		double doubleValue = ((Double) object).doubleValue();

		if (Double.isNaN(doubleValue))
		{
			out.writeNull();
		}
		else if (Double.isInfinite(doubleValue))
		{
			out.writeNull();
		}
		else
		{
			String doubleText;
			if (decimalFormat == null)
			{
				doubleText = Double.toString(doubleValue);
				if (doubleText.endsWith(".0"))
				{
					doubleText = doubleText.substring(0,
							doubleText.length() - 2);
				}
			}
			else
			{
				doubleText = decimalFormat.format(doubleValue);
			}
			out.append(doubleText);

			if (serializer.isEnabled(SerializerFeature.WriteClassName))
			{
				out.write('D');
			}
		}
	}
}
