package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public final class ListSerializer implements ObjectSerializer
{

	public static final ListSerializer instance = new ListSerializer();

	public final void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{

		boolean writeClassName = serializer
				.isEnabled(SerializerFeature.WriteClassName);

		SerializeWriter out = serializer.getWriter();

		Type elementType = null;
		if (writeClassName)
		{
			if (fieldType instanceof ParameterizedType)
			{
				ParameterizedType param = (ParameterizedType) fieldType;
				elementType = param.getActualTypeArguments()[0];
			}
		}

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

		List<?> list = (List<?>) object;

		if (list.size() == 0)
		{
			out.append("[]");
			return;
		}

		SerialContext context = serializer.getContext();
		serializer.setContext(context, object, fieldName, 0);

		ObjectSerializer itemSerializer = null;
		try
		{
			if (out.isEnabled(SerializerFeature.PrettyFormat))
			{
				out.append('[');
				serializer.incrementIndent();

				int i = 0;
				for (Object item : list)
				{
					if (i != 0)
					{
						out.append(',');
					}

					serializer.println();
					if (item != null)
					{
						if (serializer.containsReference(item))
						{
							serializer.writeReference(item);
						}
						else
						{
							itemSerializer = serializer.getObjectWriter(item
									.getClass());
							SerialContext itemContext = new SerialContext(
									context, object, fieldName, 0);
							serializer.setContext(itemContext);
							itemSerializer.write(serializer, item, i,
									elementType);
						}
					}
					else
					{
						serializer.getWriter().writeNull();
					}
					i++;
				}

				serializer.decrementIdent();
				serializer.println();
				out.append(']');
				return;
			}

			out.append('[');
			int i = 0;
			for (Object item : list)
			{
				if (i != 0)
				{
					out.append(',');
				}

				if (item == null)
				{
					out.append("null");
				}
				else
				{
					Class<?> clazz = item.getClass();

					if (clazz == Integer.class)
					{
						out.writeInt(((Integer) item).intValue());
					}
					else if (clazz == Long.class)
					{
						long val = ((Long) item).longValue();
						if (writeClassName)
						{
							out.writeLongAndChar(val, 'L');
						}
						else
						{
							out.writeLong(val);
						}
					}
					else
					{
						SerialContext itemContext = new SerialContext(context,
								object, fieldName, 0);
						serializer.setContext(itemContext);

						if (serializer.containsReference(item))
						{
							serializer.writeReference(item);
						}
						else
						{
							itemSerializer = serializer.getObjectWriter(item
									.getClass());
							itemSerializer.write(serializer, item, i,
									elementType);
						}
					}
				}
				i++;
			}
			out.append(']');
		}
		finally
		{
			serializer.setContext(context);
		}
	}

}