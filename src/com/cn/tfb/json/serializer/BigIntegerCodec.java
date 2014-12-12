package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;

import com.cn.tfb.json.parser.DefaultJSONParser;
import com.cn.tfb.json.parser.JSONLexer;
import com.cn.tfb.json.parser.JSONToken;
import com.cn.tfb.json.parser.deserializer.ObjectDeserializer;
import com.cn.tfb.json.util.TypeUtils;

public class BigIntegerCodec implements ObjectSerializer, ObjectDeserializer
{

	public final static BigIntegerCodec instance = new BigIntegerCodec();

	public void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{
		SerializeWriter out = serializer.getWriter();

		if (object == null)
		{
			if (out.isEnabled(SerializerFeature.WriteNullNumberAsZero))
			{
				out.write('0');
			}
			else
			{
				out.writeNull();
			}
			return;
		}

		BigInteger val = (BigInteger) object;
		out.write(val.toString());
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
			return (T) new BigInteger(val);
		}

		Object value = parser.parse();

		if (value == null) { return null; }

		return (T) TypeUtils.castToBigInteger(value);
	}

	public int getFastMatchToken()
	{
		return JSONToken.LITERAL_INT;
	}
}
