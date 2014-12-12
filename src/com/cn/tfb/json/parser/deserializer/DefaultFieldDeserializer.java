package com.cn.tfb.json.parser.deserializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import com.cn.tfb.json.parser.DefaultJSONParser;
import com.cn.tfb.json.parser.DefaultJSONParser.ResolveTask;
import com.cn.tfb.json.parser.JSONToken;
import com.cn.tfb.json.parser.ParseContext;
import com.cn.tfb.json.parser.ParserConfig;
import com.cn.tfb.json.util.FieldInfo;

public class DefaultFieldDeserializer extends FieldDeserializer
{

	private ObjectDeserializer fieldValueDeserilizer;

	public DefaultFieldDeserializer(ParserConfig mapping, Class<?> clazz,
			FieldInfo fieldInfo)
	{
		super(clazz, fieldInfo);
	}

	@Override
	public void parseField(DefaultJSONParser parser, Object object,
			Type objectType, Map<String, Object> fieldValues)
	{
		if (fieldValueDeserilizer == null)
		{
			fieldValueDeserilizer = parser.getConfig().getDeserializer(
					fieldInfo);
		}

		if (objectType instanceof ParameterizedType)
		{
			ParseContext objContext = parser.getContext();
			objContext.setType(objectType);
		}

		Object value = fieldValueDeserilizer.deserialze(parser, getFieldType(),
				fieldInfo.getName());
		if (parser.getResolveStatus() == DefaultJSONParser.NeedToResolve)
		{
			ResolveTask task = parser.getLastResolveTask();
			task.setFieldDeserializer(this);
			task.setOwnerContext(parser.getContext());
			parser.setResolveStatus(DefaultJSONParser.NONE);
		}
		else
		{
			if (object == null)
			{
				fieldValues.put(fieldInfo.getName(), value);
			}
			else
			{
				setValue(object, value);
			}
		}
	}

	public int getFastMatchToken()
	{
		if (fieldValueDeserilizer != null) { return fieldValueDeserilizer
				.getFastMatchToken(); }

		return JSONToken.LITERAL_INT;
	}
}
