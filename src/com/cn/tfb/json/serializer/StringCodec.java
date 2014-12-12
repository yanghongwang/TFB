package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

import com.cn.tfb.json.parser.DefaultJSONParser;
import com.cn.tfb.json.parser.JSONLexer;
import com.cn.tfb.json.parser.JSONToken;
import com.cn.tfb.json.parser.deserializer.ObjectDeserializer;

public class StringCodec implements ObjectSerializer, ObjectDeserializer
{

	public static StringCodec instance = new StringCodec();

	public void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{
		write(serializer, (String) object);
	}

	public void write(JSONSerializer serializer, String value)
	{
		SerializeWriter out = serializer.getWriter();

		if (value == null)
		{
			if (out.isEnabled(SerializerFeature.WriteNullStringAsEmpty))
			{
				out.writeString("");
			}
			else
			{
				out.writeNull();
			}
			return;
		}

		out.writeString(value);
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
		if (lexer.token() == JSONToken.LITERAL_STRING)
		{
			String val = lexer.stringVal();
			lexer.nextToken(JSONToken.COMMA);
			return (T) val;
		}

		if (lexer.token() == JSONToken.LITERAL_INT)
		{
			String val = lexer.numberString();
			lexer.nextToken(JSONToken.COMMA);
			return (T) val;
		}

		Object value = parser.parse();

		if (value == null) { return null; }

		return (T) value.toString();
	}

	public int getFastMatchToken()
	{
		return JSONToken.LITERAL_STRING;
	}
}
