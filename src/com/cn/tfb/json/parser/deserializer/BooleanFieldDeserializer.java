package com.cn.tfb.json.parser.deserializer;

import java.lang.reflect.Type;
import java.util.Map;

import com.cn.tfb.json.parser.DefaultJSONParser;
import com.cn.tfb.json.parser.JSONLexer;
import com.cn.tfb.json.parser.JSONToken;
import com.cn.tfb.json.parser.ParserConfig;
import com.cn.tfb.json.util.FieldInfo;
import com.cn.tfb.json.util.TypeUtils;

public class BooleanFieldDeserializer extends FieldDeserializer
{

	public BooleanFieldDeserializer(ParserConfig mapping, Class<?> clazz,
			FieldInfo fieldInfo)
	{
		super(clazz, fieldInfo);
	}

	@Override
	public void parseField(DefaultJSONParser parser, Object object,
			Type objectType, Map<String, Object> fieldValues)
	{
		Boolean value;

		final JSONLexer lexer = parser.getLexer();
		if (lexer.token() == JSONToken.TRUE)
		{
			lexer.nextToken(JSONToken.COMMA);
			if (object == null)
			{
				fieldValues.put(fieldInfo.getName(), Boolean.TRUE);
			}
			else
			{
				setValue(object, true);
			}
			return;
		}

		if (lexer.token() == JSONToken.LITERAL_INT)
		{
			int val = lexer.intValue();
			lexer.nextToken(JSONToken.COMMA);
			boolean booleanValue = val == 1;
			if (object == null)
			{
				fieldValues.put(fieldInfo.getName(), booleanValue);
			}
			else
			{
				setValue(object, booleanValue);
			}
			return;
		}

		if (lexer.token() == JSONToken.NULL)
		{
			value = null;
			lexer.nextToken(JSONToken.COMMA);

			if (getFieldClass() == boolean.class)
			{
				// skip
				return;
			}

			if (object != null)
			{
				setValue(object, null);
			}
			return;
		}

		if (lexer.token() == JSONToken.FALSE)
		{
			lexer.nextToken(JSONToken.COMMA);
			if (object == null)
			{
				fieldValues.put(fieldInfo.getName(), Boolean.FALSE);
			}
			else
			{
				setValue(object, false);
			}
			return;
		}

		Object obj = parser.parse();

		value = TypeUtils.castToBoolean(obj);

		if (value == null && getFieldClass() == boolean.class)
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
		return JSONToken.TRUE;
	}
}
