package com.cn.tfb.json.serializer;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

import com.cn.tfb.json.JSON;
import com.cn.tfb.json.util.IdentityHashMap;

public class SerializeConfig extends IdentityHashMap<Type, ObjectSerializer>
{

	private final static SerializeConfig globalInstance = new SerializeConfig();

	private String typeKey = JSON.DEFAULT_TYPE_KEY;

	public String getTypeKey()
	{
		return typeKey;
	}

	public void setTypeKey(String typeKey)
	{
		this.typeKey = typeKey;
	}

	public ObjectSerializer createJavaBeanSerializer(Class<?> clazz)
	{
		return new JavaBeanSerializer(clazz);
	}

	public final static SerializeConfig getGlobalInstance()
	{
		return globalInstance;
	}

	public SerializeConfig()
	{
		this(DEFAULT_TABLE_SIZE);
	}

	public SerializeConfig(int tableSize)
	{
		super(tableSize);

		put(Boolean.class, BooleanCodec.instance);
		put(Character.class, CharacterCodec.instance);
		put(Byte.class, IntegerCodec.instance);
		put(Short.class, IntegerCodec.instance);
		put(Integer.class, IntegerCodec.instance);
		put(Long.class, LongCodec.instance);
		put(Float.class, FloatCodec.instance);
		put(Double.class, DoubleSerializer.instance);
		put(BigDecimal.class, BigDecimalCodec.instance);
		put(BigInteger.class, BigIntegerCodec.instance);
		put(String.class, StringCodec.instance);
		put(byte[].class, ByteArraySerializer.instance);
		put(short[].class, ShortArraySerializer.instance);
		put(int[].class, IntArraySerializer.instance);
		put(long[].class, LongArraySerializer.instance);
		put(float[].class, FloatArraySerializer.instance);
		put(double[].class, DoubleArraySerializer.instance);
		put(boolean[].class, BooleanArraySerializer.instance);
		put(char[].class, CharArraySerializer.instance);
		put(Object[].class, ObjectArraySerializer.instance);
		put(Class.class, ClassSerializer.instance);

		put(SimpleDateFormat.class, DateFormatSerializer.instance);
		put(Locale.class, LocaleCodec.instance);
		put(Currency.class, CurrencyCodec.instance);
		put(TimeZone.class, TimeZoneCodec.instance);
		put(UUID.class, UUIDCodec.instance);
		put(InetAddress.class, InetAddressCodec.instance);
		put(Inet4Address.class, InetAddressCodec.instance);
		put(Inet6Address.class, InetAddressCodec.instance);
		put(InetSocketAddress.class, InetSocketAddressCodec.instance);
		put(URI.class, URICodec.instance);
		put(URL.class, URLCodec.instance);
		put(Pattern.class, PatternCodec.instance);
		put(Charset.class, CharsetCodec.instance);

	}

}
