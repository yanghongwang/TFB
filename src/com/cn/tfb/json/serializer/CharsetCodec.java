package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import com.cn.tfb.json.parser.DefaultJSONParser;
import com.cn.tfb.json.parser.JSONToken;
import com.cn.tfb.json.parser.deserializer.ObjectDeserializer;

public class CharsetCodec implements ObjectSerializer, ObjectDeserializer
{

	public final static CharsetCodec instance = new CharsetCodec();

	public void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{
		if (object == null)
		{
			serializer.writeNull();
			return;
		}

		Charset charset = (Charset) object;
		serializer.write(charset.toString());
	}

	@SuppressWarnings("unchecked")
	public <T> T deserialze(DefaultJSONParser parser, Type clazz,
			Object fieldName)
	{
		Object value = parser.parse();

		if (value == null) { return null; }

		String charset = (String) value;

		return (T) Charset.forName(charset);
	}

	public int getFastMatchToken()
	{
		return JSONToken.LITERAL_STRING;
	}
}
