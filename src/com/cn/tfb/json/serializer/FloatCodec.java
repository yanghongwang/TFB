package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

import com.cn.tfb.json.parser.DefaultJSONParser;
import com.cn.tfb.json.parser.JSONLexer;
import com.cn.tfb.json.parser.JSONToken;
import com.cn.tfb.json.parser.deserializer.ObjectDeserializer;
import com.cn.tfb.json.util.TypeUtils;

public class FloatCodec implements ObjectSerializer, ObjectDeserializer
{

	public static FloatCodec instance = new FloatCodec();

	public void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{
		SerializeWriter out = serializer.getWriter();

		if (object == null)
		{
			if (serializer.isEnabled(SerializerFeature.WriteNullNumberAsZero))
			{
				out.write('0');
			}
			else
			{
				out.writeNull();
			}
			return;
		}

		float floatValue = ((Float) object).floatValue();

		if (Float.isNaN(floatValue))
		{
			out.writeNull();
		}
		else if (Float.isInfinite(floatValue))
		{
			out.writeNull();
		}
		else
		{
			String floatText = Float.toString(floatValue);
			if (floatText.endsWith(".0"))
			{
				floatText = floatText.substring(0, floatText.length() - 2);
			}
			out.write(floatText);

			if (serializer.isEnabled(SerializerFeature.WriteClassName))
			{
				out.write('F');
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T deserialze(DefaultJSONParser parser, Type clazz,
			Object fieldName)
	{
		return (T) deserialze(parser);
	}

	@SuppressWarnings("unchecked")
	public static <T> T deserialze(DefaultJSONParser parser)
	{
		final JSONLexer lexer = parser.getLexer();
		if (lexer.token() == JSONToken.LITERAL_INT)
		{
			String val = lexer.numberString();
			lexer.nextToken(JSONToken.COMMA);
			return (T) Float.valueOf(Float.parseFloat(val));
		}

		if (lexer.token() == JSONToken.LITERAL_FLOAT)
		{
			float val = lexer.floatValue();
			lexer.nextToken(JSONToken.COMMA);
			return (T) Float.valueOf(val);
		}

		Object value = parser.parse();

		if (value == null) { return null; }

		return (T) TypeUtils.castToFloat(value);
	}

	public int getFastMatchToken()
	{
		return JSONToken.LITERAL_INT;
	}
}
