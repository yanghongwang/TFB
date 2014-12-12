package com.cn.tfb.json.parser.deserializer;

import java.lang.reflect.Type;
import java.util.Map;

import com.cn.tfb.json.parser.DefaultJSONParser;
import com.cn.tfb.json.parser.JSONLexer;
import com.cn.tfb.json.parser.JSONToken;
import com.cn.tfb.json.parser.ParserConfig;
import com.cn.tfb.json.util.FieldInfo;
import com.cn.tfb.json.util.TypeUtils;

public class LongFieldDeserializer extends FieldDeserializer
{

	private final ObjectDeserializer fieldValueDeserilizer;

	public LongFieldDeserializer(ParserConfig mapping, Class<?> clazz,
			FieldInfo fieldInfo)
	{
		super(clazz, fieldInfo);

		fieldValueDeserilizer = mapping.getDeserializer(fieldInfo);
	}

	@Override
	public void parseField(DefaultJSONParser parser, Object object,
			Type objectType, Map<String, Object> fieldValues)
	{
		Long value;

		final JSONLexer lexer = parser.getLexer();
		if (lexer.token() == JSONToken.LITERAL_INT)
		{
			long val = lexer.longValue();
			lexer.nextToken(JSONToken.COMMA);
			if (object == null)
			{
				fieldValues.put(fieldInfo.getName(), val);
			}
			else
			{
				setValue(object, val);
			}
			return;
		}
		else if (lexer.token() == JSONToken.NULL)
		{
			value = null;
			lexer.nextToken(JSONToken.COMMA);

		}
		else
		{
			Object obj = parser.parse();

			value = TypeUtils.castToLong(obj);
		}

		if (value == null && getFieldClass() == long.class)
		{
			// skip
			return;
		}

		if (object == null)
		{
			fieldValues.put(fieldInfo.getName(), value);
		}
		else
		{
			setValue(object, value);
		}
	}

	public int getFastMatchToken()
	{
		return fieldValueDeserilizer.getFastMatchToken();
	}
}
