package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class JSONSerializableSerializer implements ObjectSerializer
{

	public static JSONSerializableSerializer instance = new JSONSerializableSerializer();

	public void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{
		JSONSerializable jsonSerializable = ((JSONSerializable) object);
		jsonSerializable.write(serializer, fieldName, fieldType);
	}
}
