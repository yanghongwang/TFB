package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

import com.cn.tfb.json.parser.DefaultJSONParser;
import com.cn.tfb.json.parser.JSONToken;
import com.cn.tfb.json.parser.deserializer.DateDeserializer;
import com.cn.tfb.json.parser.deserializer.ObjectDeserializer;

public class CalendarCodec implements ObjectSerializer, ObjectDeserializer
{

	public final static CalendarCodec instance = new CalendarCodec();

	public void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{
		Calendar calendar = (Calendar) object;
		Date date = calendar.getTime();
		serializer.write(date);
	}

	@SuppressWarnings("unchecked")
	public <T> T deserialze(DefaultJSONParser parser, Type type,
			Object fieldName)
	{
		Object value = DateDeserializer.instance.deserialze(parser, type,
				fieldName);

		if (value instanceof Calendar) { return (T) value; }

		Date date = (Date) value;
		if (date == null) { return null; }

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return (T) calendar;
	}

	public int getFastMatchToken()
	{
		return JSONToken.LITERAL_INT;
	}
}
