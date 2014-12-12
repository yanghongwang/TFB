package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class ObjectArraySerializer implements ObjectSerializer
{

	public static final ObjectArraySerializer instance = new ObjectArraySerializer();

	public ObjectArraySerializer()
	{
	}

	public final void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{
		SerializeWriter out = serializer.getWriter();

		Object[] array = (Object[]) object;

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

		int size = array.length;

		int end = size - 1;

		if (end == -1)
		{
			out.append("[]");
			return;
		}

		SerialContext context = serializer.getContext();
		serializer.setContext(context, object, fieldName, 0);

		try
		{
			Class<?> preClazz = null;
			ObjectSerializer preWriter = null;
			out.append('[');

			if (out.isEnabled(SerializerFeature.PrettyFormat))
			{
				serializer.incrementIndent();
				serializer.println();
				for (int i = 0; i < size; ++i)
				{
					if (i != 0)
					{
						out.write(',');
						serializer.println();
					}
					serializer.write(array[i]);
				}
				serializer.decrementIdent();
				serializer.println();
				out.write(']');
				return;
			}

			for (int i = 0; i < end; ++i)
			{
				Object item = array[i];

				if (item == null)
				{
					out.append("null,");
				}
				else
				{
					if (serializer.containsReference(item))
					{
						serializer.writeReference(item);
					}
					else
					{
						Class<?> clazz = item.getClass();

						if (clazz == preClazz)
						{
							preWriter.write(serializer, item, null, null);
						}
						else
						{
							preClazz = clazz;
							preWriter = serializer.getObjectWriter(clazz);

							preWriter.write(serializer, item, null, null);
						}
					}
					out.append(',');
				}
			}

			Object item = array[end];

			if (item == null)
			{
				out.append("null]");
			}
			else
			{
				if (serializer.containsReference(item))
				{
					serializer.writeReference(item);
				}
				else
				{
					serializer.writeWithFieldName(item, end);
				}
				out.append(']');
			}
		}
		finally
		{
			serializer.setContext(context);
		}
	}
}
