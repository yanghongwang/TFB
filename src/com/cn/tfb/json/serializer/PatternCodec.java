package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.regex.Pattern;

import com.cn.tfb.json.parser.DefaultJSONParser;
import com.cn.tfb.json.parser.JSONToken;
import com.cn.tfb.json.parser.deserializer.ObjectDeserializer;

public class PatternCodec implements ObjectSerializer, ObjectDeserializer
{

	public final static PatternCodec instance = new PatternCodec();

	public void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{
		if (object == null)
		{
			serializer.writeNull();
			return;
		}

		Pattern p = (Pattern) object;
		serializer.write(p.pattern());
	}

	@SuppressWarnings("unchecked")
	public <T> T deserialze(DefaultJSONParser parser, Type clazz,
			Object fieldName)
	{
		Object value = parser.parse();

		if (value == null) { return null; }

		String pattern = (String) value;

		return (T) Pattern.compile(pattern);
	}

	public int getFastMatchToken()
	{
		return JSONToken.LITERAL_STRING;
	}
}
