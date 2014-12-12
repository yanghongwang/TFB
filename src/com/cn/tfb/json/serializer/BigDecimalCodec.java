package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

import com.cn.tfb.json.parser.DefaultJSONParser;
import com.cn.tfb.json.parser.JSONLexer;
import com.cn.tfb.json.parser.JSONToken;
import com.cn.tfb.json.parser.deserializer.ObjectDeserializer;
import com.cn.tfb.json.util.TypeUtils;

public class BigDecimalCodec implements ObjectSerializer, ObjectDeserializer
{

	public final static BigDecimalCodec instance = new BigDecimalCodec();

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

		BigDecimal val = (BigDecimal) object;
		out.write(val.toString());

		if (out.isEnabled(SerializerFeature.WriteClassName)
				&& fieldType != BigDecimal.class && val.scale() == 0)
		{
			out.write('.');
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
			long val = lexer.longValue();
			lexer.nextToken(JSONToken.COMMA);
			return (T) new BigDecimal(val);
		}

		if (lexer.token() == JSONToken.LITERAL_FLOAT)
		{
			BigDecimal val = lexer.decimalValue();
			lexer.nextToken(JSONToken.COMMA);
			return (T) val;
		}

		Object value = parser.parse();

		if (value == null) { return null; }

		return (T) TypeUtils.castToBigDecimal(value);
	}

	public int getFastMatchToken()
	{
		return JSONToken.LITERAL_INT;
	}
}
