package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;

import com.cn.tfb.json.parser.DefaultJSONParser;
import com.cn.tfb.json.parser.JSONToken;
import com.cn.tfb.json.parser.deserializer.ObjectDeserializer;

public class URICodec implements ObjectSerializer, ObjectDeserializer
{

	public final static URICodec instance = new URICodec();

	public void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{
		if (object == null)
		{
			serializer.writeNull();
			return;
		}

		URI uri = (URI) object;
		serializer.write(uri.toString());
	}

	@SuppressWarnings("unchecked")
	public <T> T deserialze(DefaultJSONParser parser, Type clazz,
			Object fieldName)
	{

		String uri = (String) parser.parse();

		if (uri == null) { return null; }

		return (T) URI.create(uri);
	}

	public int getFastMatchToken()
	{
		return JSONToken.LITERAL_STRING;
	}
}
