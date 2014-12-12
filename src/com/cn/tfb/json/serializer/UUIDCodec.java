package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.UUID;

import com.cn.tfb.json.parser.DefaultJSONParser;
import com.cn.tfb.json.parser.JSONToken;
import com.cn.tfb.json.parser.deserializer.ObjectDeserializer;

public class UUIDCodec implements ObjectSerializer, ObjectDeserializer
{

	public final static UUIDCodec instance = new UUIDCodec();

	public void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{
		if (object == null)
		{
			serializer.writeNull();
			return;
		}

		UUID uid = (UUID) object;
		serializer.write(uid.toString());
	}

	@SuppressWarnings("unchecked")
	public <T> T deserialze(DefaultJSONParser parser, Type clazz,
			Object fieldName)
	{

		String name = (String) parser.parse();

		if (name == null) { return null; }

		return (T) UUID.fromString(name);
	}

	public int getFastMatchToken()
	{
		return JSONToken.LITERAL_STRING;
	}
}
