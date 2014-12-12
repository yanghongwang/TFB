package com.cn.tfb.json.parser.deserializer;

import java.lang.reflect.Type;

import com.cn.tfb.json.parser.DefaultJSONParser;

public interface ObjectDeserializer
{
	<T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName);

	int getFastMatchToken();
}
