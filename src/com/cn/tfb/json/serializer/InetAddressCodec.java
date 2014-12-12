package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.cn.tfb.json.JSONException;
import com.cn.tfb.json.parser.DefaultJSONParser;
import com.cn.tfb.json.parser.JSONToken;
import com.cn.tfb.json.parser.deserializer.ObjectDeserializer;

public class InetAddressCodec implements ObjectSerializer, ObjectDeserializer
{

	public static InetAddressCodec instance = new InetAddressCodec();

	public void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{
		if (object == null)
		{
			serializer.writeNull();
			return;
		}

		InetAddress address = (InetAddress) object;

		serializer.write(address.getHostAddress());
	}

	@SuppressWarnings("unchecked")
	public <T> T deserialze(DefaultJSONParser parser, Type clazz,
			Object fieldName)
	{

		String host = (String) parser.parse();

		if (host == null) { return null; }

		if (host.length() == 0) { return null; }

		try
		{
			return (T) InetAddress.getByName(host);
		}
		catch (UnknownHostException e)
		{
			throw new JSONException("deserialize error", e);
		}
	}

	public int getFastMatchToken()
	{
		return JSONToken.LITERAL_STRING;
	}
}
