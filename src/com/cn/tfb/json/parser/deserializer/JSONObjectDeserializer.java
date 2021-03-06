package com.cn.tfb.json.parser.deserializer;

import java.lang.reflect.Type;

import com.cn.tfb.json.parser.DefaultJSONParser;
import com.cn.tfb.json.parser.JSONToken;

public class JSONObjectDeserializer implements ObjectDeserializer
{
	public final static JSONObjectDeserializer instance = new JSONObjectDeserializer();

	@SuppressWarnings("unchecked")
	public <T> T deserialze(DefaultJSONParser parser, Type clazz,
			Object fieldName)
	{
		return (T) parser.parseObject();
	}

	public int getFastMatchToken()
	{
		return JSONToken.LBRACE;
	}
}
