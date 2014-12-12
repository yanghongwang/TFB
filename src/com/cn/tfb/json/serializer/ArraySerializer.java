package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class ArraySerializer implements ObjectSerializer
{

	private final Class<?> componentType;
	private final ObjectSerializer compObjectSerializer;

	public ArraySerializer(Class<?> componentType,
			ObjectSerializer compObjectSerializer)
	{
		this.componentType = componentType;
		this.compObjectSerializer = compObjectSerializer;
	}

	public final void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{
		SerializeWriter out = serializer.getWriter();

		if (object == null)
		{
			if (out.isEnabled(SerializerFeature.WriteNullListAsEmpty))
			{
				out.write("[]");
			}
			else
			{
				out.writeNull();
			}
			return;
		}

		Object[] array = (Object[]) object;
		int size = array.length;

		SerialContext context = serializer.getContext();
		serializer.setContext(context, object, fieldName, 0);

		try
		{
			out.append('[');
			for (int i = 0; i < size; ++i)
			{
				if (i != 0)
				{
					out.append(',');
				}
				Object item = array[i];

				if (item == null)
				{
					out.append("null");
				}
				else if (item.getClass() == componentType)
				{
					compObjectSerializer.write(serializer, item, i, null);
				}
				else
				{
					ObjectSerializer itemSerializer = serializer
							.getObjectWriter(item.getClass());
					itemSerializer.write(serializer, item, i, null);
				}
			}
			out.append(']');
		}
		finally
		{
			serializer.setContext(context);
		}
	}
}
