package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class EnumSerializer implements ObjectSerializer
{

	public final static EnumSerializer instance = new EnumSerializer();

	public void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{
		SerializeWriter out = serializer.getWriter();
		if (object == null)
		{
			serializer.getWriter().writeNull();
			return;
		}

		if (serializer.isEnabled(SerializerFeature.WriteEnumUsingToString))
		{
			Enum<?> e = (Enum<?>) object;
			serializer.write(e.toString());
		}
		else
		{
			Enum<?> e = (Enum<?>) object;
			out.writeInt(e.ordinal());
		}
	}
}
