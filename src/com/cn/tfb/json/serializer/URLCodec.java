package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

import com.cn.tfb.json.JSONException;
import com.cn.tfb.json.parser.DefaultJSONParser;
import com.cn.tfb.json.parser.JSONToken;
import com.cn.tfb.json.parser.deserializer.ObjectDeserializer;

public class URLCodec implements ObjectSerializer, ObjectDeserializer
{

	public final static URLCodec instance = new URLCodec();

	public void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{
		if (object == null)
		{
			serializer.writeNull();
			return;
		}

		serializer.write(object.toString());
	}

	@SuppressWarnings("unchecked")
	public <T> T deserialze(DefaultJSONParser parser, Type clazz,
			Object fieldName)
	{

		String url = (String) parser.parse();

		if (url == null) { return null; }

		try
		{
			return (T) new URL(url);
		}
		catch (MalformedURLException e)
		{
			throw new JSONException("create url error", e);
		}
	}

	public int getFastMatchToken()
	{
		return JSONToken.LITERAL_STRING;
	}
}
