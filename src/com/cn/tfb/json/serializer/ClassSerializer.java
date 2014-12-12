package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class ClassSerializer implements ObjectSerializer
{

	public final static ClassSerializer instance = new ClassSerializer();

	@SuppressWarnings("rawtypes")
	public void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{
		SerializeWriter out = serializer.getWriter();

		Class clazz = (Class) object;
		out.writeString(clazz.getName());
	}

}
