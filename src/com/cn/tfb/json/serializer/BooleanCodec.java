package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

import com.cn.tfb.json.parser.DefaultJSONParser;
import com.cn.tfb.json.parser.JSONLexer;
import com.cn.tfb.json.parser.JSONToken;
import com.cn.tfb.json.parser.deserializer.ObjectDeserializer;
import com.cn.tfb.json.util.TypeUtils;

public class BooleanCodec implements ObjectSerializer, ObjectDeserializer
{

	public final static BooleanCodec instance = new BooleanCodec();

	public void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{
		SerializeWriter out = serializer.getWriter();

		Boolean value = (Boolean) object;
		if (value == null)
		{
			if (out.isEnabled(SerializerFeature.WriteNullBooleanAsFalse))
			{
				out.write("false");
			}
			else
			{
				out.writeNull();
			}
			return;
		}

		if (value.booleanValue())
		{
			out.write("true");
		}
		else
		{
			out.write("false");
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T deserialze(DefaultJSONParser parser, Type clazz,
			Object fieldName)
	{
		final JSONLexer lexer = parser.getLexer();

		Boolean boolObj;
		if (lexer.token() == JSONToken.TRUE)
		{
			lexer.nextToken(JSONToken.COMMA);
			boolObj = Boolean.TRUE;
		}
		else if (lexer.token() == JSONToken.FALSE)
		{
			lexer.nextToken(JSONToken.COMMA);
			boolObj = Boolean.FALSE;
		}
		else if (lexer.token() == JSONToken.LITERAL_INT)
		{
			int intValue = lexer.intValue();
			lexer.nextToken(JSONToken.COMMA);

			if (intValue == 1)
			{
				boolObj = Boolean.TRUE;
			}
			else
			{
				boolObj = Boolean.FALSE;
			}
		}
		else
		{
			Object value = parser.parse();

			if (value == null) { return null; }

			boolObj = TypeUtils.castToBoolean(value);
		}

		return (T) boolObj;
	}

	public int getFastMatchToken()
	{
		return JSONToken.TRUE;
	}
}
