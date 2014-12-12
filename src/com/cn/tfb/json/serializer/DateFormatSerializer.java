package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import com.cn.tfb.json.JSON;

public class DateFormatSerializer implements ObjectSerializer
{

	public final static DateFormatSerializer instance = new DateFormatSerializer();

	public void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{
		SerializeWriter out = serializer.getWriter();

		if (object == null)
		{
			out.writeNull();
			return;
		}

		String pattern = ((SimpleDateFormat) object).toPattern();

		if (out.isEnabled(SerializerFeature.WriteClassName))
		{
			if (object.getClass() != fieldType)
			{
				out.write('{');
				out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
				serializer.write(object.getClass().getName());
				out.writeFieldValue(',', "val", pattern);
				out.write('}');
				return;
			}
		}

		out.writeString(pattern);
	}
}
