package com.cn.tfb.json.parser;

import java.io.Closeable;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

import com.cn.tfb.json.JSONArray;
import com.cn.tfb.json.JSONObject;
import com.cn.tfb.json.annotation.JSONType;
import com.cn.tfb.json.parser.deserializer.ArrayDeserializer;
import com.cn.tfb.json.parser.deserializer.ArrayListTypeFieldDeserializer;
import com.cn.tfb.json.parser.deserializer.BooleanFieldDeserializer;
import com.cn.tfb.json.parser.deserializer.CharArrayDeserializer;
import com.cn.tfb.json.parser.deserializer.ClassDerializer;
import com.cn.tfb.json.parser.deserializer.CollectionDeserializer;
import com.cn.tfb.json.parser.deserializer.DateDeserializer;
import com.cn.tfb.json.parser.deserializer.DateFormatDeserializer;
import com.cn.tfb.json.parser.deserializer.DefaultFieldDeserializer;
import com.cn.tfb.json.parser.deserializer.EnumDeserializer;
import com.cn.tfb.json.parser.deserializer.FieldDeserializer;
import com.cn.tfb.json.parser.deserializer.IntegerFieldDeserializer;
import com.cn.tfb.json.parser.deserializer.JSONArrayDeserializer;
import com.cn.tfb.json.parser.deserializer.JSONObjectDeserializer;
import com.cn.tfb.json.parser.deserializer.JavaBeanDeserializer;
import com.cn.tfb.json.parser.deserializer.JavaObjectDeserializer;
import com.cn.tfb.json.parser.deserializer.LongFieldDeserializer;
import com.cn.tfb.json.parser.deserializer.MapDeserializer;
import com.cn.tfb.json.parser.deserializer.NumberDeserializer;
import com.cn.tfb.json.parser.deserializer.ObjectDeserializer;
import com.cn.tfb.json.parser.deserializer.SqlDateDeserializer;
import com.cn.tfb.json.parser.deserializer.StackTraceElementDeserializer;
import com.cn.tfb.json.parser.deserializer.StringFieldDeserializer;
import com.cn.tfb.json.parser.deserializer.ThrowableDeserializer;
import com.cn.tfb.json.parser.deserializer.TimeDeserializer;
import com.cn.tfb.json.parser.deserializer.TimestampDeserializer;
import com.cn.tfb.json.serializer.BigDecimalCodec;
import com.cn.tfb.json.serializer.BigIntegerCodec;
import com.cn.tfb.json.serializer.BooleanCodec;
import com.cn.tfb.json.serializer.CalendarCodec;
import com.cn.tfb.json.serializer.CharacterCodec;
import com.cn.tfb.json.serializer.CharsetCodec;
import com.cn.tfb.json.serializer.CurrencyCodec;
import com.cn.tfb.json.serializer.FloatCodec;
import com.cn.tfb.json.serializer.InetAddressCodec;
import com.cn.tfb.json.serializer.InetSocketAddressCodec;
import com.cn.tfb.json.serializer.IntegerCodec;
import com.cn.tfb.json.serializer.LocaleCodec;
import com.cn.tfb.json.serializer.LongCodec;
import com.cn.tfb.json.serializer.PatternCodec;
import com.cn.tfb.json.serializer.StringCodec;
import com.cn.tfb.json.serializer.TimeZoneCodec;
import com.cn.tfb.json.serializer.URICodec;
import com.cn.tfb.json.serializer.URLCodec;
import com.cn.tfb.json.serializer.UUIDCodec;
import com.cn.tfb.json.util.FieldInfo;
import com.cn.tfb.json.util.IdentityHashMap;

public class ParserConfig
{

	public static ParserConfig getGlobalInstance()
	{
		return global;
	}

	private final Set<Class<?>> primitiveClasses = new HashSet<Class<?>>();

	private static ParserConfig global = new ParserConfig();

	private final IdentityHashMap<Type, ObjectDeserializer> derializers = new IdentityHashMap<Type, ObjectDeserializer>();

	protected final SymbolTable symbolTable = new SymbolTable();

	public ParserConfig()
	{
		primitiveClasses.add(boolean.class);
		primitiveClasses.add(Boolean.class);

		primitiveClasses.add(char.class);
		primitiveClasses.add(Character.class);

		primitiveClasses.add(byte.class);
		primitiveClasses.add(Byte.class);

		primitiveClasses.add(short.class);
		primitiveClasses.add(Short.class);

		primitiveClasses.add(int.class);
		primitiveClasses.add(Integer.class);

		primitiveClasses.add(long.class);
		primitiveClasses.add(Long.class);

		primitiveClasses.add(float.class);
		primitiveClasses.add(Float.class);

		primitiveClasses.add(double.class);
		primitiveClasses.add(Double.class);

		primitiveClasses.add(BigInteger.class);
		primitiveClasses.add(BigDecimal.class);

		primitiveClasses.add(String.class);
		primitiveClasses.add(java.util.Date.class);
		primitiveClasses.add(java.sql.Date.class);
		primitiveClasses.add(java.sql.Time.class);
		primitiveClasses.add(java.sql.Timestamp.class);

		derializers
				.put(SimpleDateFormat.class, DateFormatDeserializer.instance);
		derializers.put(java.sql.Timestamp.class,
				TimestampDeserializer.instance);
		derializers.put(java.sql.Date.class, SqlDateDeserializer.instance);
		derializers.put(java.sql.Time.class, TimeDeserializer.instance);
		derializers.put(java.util.Date.class, DateDeserializer.instance);
		derializers.put(Calendar.class, CalendarCodec.instance);

		derializers.put(JSONObject.class, JSONObjectDeserializer.instance);
		derializers.put(JSONArray.class, JSONArrayDeserializer.instance);

		derializers.put(Map.class, MapDeserializer.instance);
		derializers.put(HashMap.class, MapDeserializer.instance);
		derializers.put(LinkedHashMap.class, MapDeserializer.instance);
		derializers.put(TreeMap.class, MapDeserializer.instance);
		derializers.put(ConcurrentMap.class, MapDeserializer.instance);
		derializers.put(ConcurrentHashMap.class, MapDeserializer.instance);

		derializers.put(Collection.class, CollectionDeserializer.instance);
		derializers.put(List.class, CollectionDeserializer.instance);
		derializers.put(ArrayList.class, CollectionDeserializer.instance);

		derializers.put(Object.class, JavaObjectDeserializer.instance);
		derializers.put(String.class, StringCodec.instance);
		derializers.put(char.class, CharacterCodec.instance);
		derializers.put(Character.class, CharacterCodec.instance);
		derializers.put(byte.class, NumberDeserializer.instance);
		derializers.put(Byte.class, NumberDeserializer.instance);
		derializers.put(short.class, NumberDeserializer.instance);
		derializers.put(Short.class, NumberDeserializer.instance);
		derializers.put(int.class, IntegerCodec.instance);
		derializers.put(Integer.class, IntegerCodec.instance);
		derializers.put(long.class, LongCodec.instance);
		derializers.put(Long.class, LongCodec.instance);
		derializers.put(BigInteger.class, BigIntegerCodec.instance);
		derializers.put(BigDecimal.class, BigDecimalCodec.instance);
		derializers.put(float.class, FloatCodec.instance);
		derializers.put(Float.class, FloatCodec.instance);
		derializers.put(double.class, NumberDeserializer.instance);
		derializers.put(Double.class, NumberDeserializer.instance);
		derializers.put(boolean.class, BooleanCodec.instance);
		derializers.put(Boolean.class, BooleanCodec.instance);
		derializers.put(Class.class, ClassDerializer.instance);
		derializers.put(char[].class, CharArrayDeserializer.instance);

		derializers.put(UUID.class, UUIDCodec.instance);
		derializers.put(TimeZone.class, TimeZoneCodec.instance);
		derializers.put(Locale.class, LocaleCodec.instance);
		derializers.put(Currency.class, CurrencyCodec.instance);
		derializers.put(InetAddress.class, InetAddressCodec.instance);
		derializers.put(Inet4Address.class, InetAddressCodec.instance);
		derializers.put(Inet6Address.class, InetAddressCodec.instance);
		derializers.put(InetSocketAddress.class,
				InetSocketAddressCodec.instance);
		derializers.put(URI.class, URICodec.instance);
		derializers.put(URL.class, URLCodec.instance);
		derializers.put(Pattern.class, PatternCodec.instance);
		derializers.put(Charset.class, CharsetCodec.instance);
		derializers.put(Number.class, NumberDeserializer.instance);
		derializers.put(StackTraceElement.class,
				StackTraceElementDeserializer.instance);

		derializers.put(Serializable.class, JavaObjectDeserializer.instance);
		derializers.put(Cloneable.class, JavaObjectDeserializer.instance);
		derializers.put(Comparable.class, JavaObjectDeserializer.instance);
		derializers.put(Closeable.class, JavaObjectDeserializer.instance);

	}

	public SymbolTable getSymbolTable()
	{
		return symbolTable;
	}

	public IdentityHashMap<Type, ObjectDeserializer> getDerializers()
	{
		return derializers;
	}

	public ObjectDeserializer getDeserializer(Type type)
	{
		ObjectDeserializer derializer = this.derializers.get(type);
		if (derializer != null) { return derializer; }

		if (type instanceof Class<?>) { return getDeserializer((Class<?>) type,
				type); }

		if (type instanceof ParameterizedType)
		{
			Type rawType = ((ParameterizedType) type).getRawType();
			if (rawType instanceof Class<?>)
			{
				return getDeserializer((Class<?>) rawType, type);
			}
			else
			{
				return getDeserializer(rawType);
			}
		}

		return JavaObjectDeserializer.instance;
	}

	public ObjectDeserializer getDeserializer(Class<?> clazz, Type type)
	{
		ObjectDeserializer derializer = derializers.get(type);
		if (derializer != null) { return derializer; }

		if (type == null)
		{
			type = clazz;
		}

		derializer = derializers.get(type);
		if (derializer != null) { return derializer; }

		{
			JSONType annotation = clazz.getAnnotation(JSONType.class);
			if (annotation != null)
			{
				Class<?> mappingTo = annotation.mappingTo();
				if (mappingTo != Void.class) { return getDeserializer(
						mappingTo, mappingTo); }
			}
		}

		if (type instanceof WildcardType || type instanceof TypeVariable
				|| type instanceof ParameterizedType)
		{
			derializer = derializers.get(clazz);
		}

		if (derializer != null) { return derializer; }

		derializer = derializers.get(type);
		if (derializer != null) { return derializer; }

		if (clazz.isEnum())
		{
			derializer = new EnumDeserializer(clazz);
		}
		else if (clazz.isArray())
		{
			derializer = ArrayDeserializer.instance;
		}
		else if (clazz == Set.class || clazz == HashSet.class
				|| clazz == Collection.class || clazz == List.class
				|| clazz == ArrayList.class)
		{
			derializer = CollectionDeserializer.instance;
		}
		else if (Collection.class.isAssignableFrom(clazz))
		{
			derializer = CollectionDeserializer.instance;
		}
		else if (Map.class.isAssignableFrom(clazz))
		{
			derializer = MapDeserializer.instance;
		}
		else if (Throwable.class.isAssignableFrom(clazz))
		{
			derializer = new ThrowableDeserializer(this, clazz);
		}
		else
		{
			derializer = createJavaBeanDeserializer(clazz, type);
		}

		putDeserializer(type, derializer);

		return derializer;
	}

	public ObjectDeserializer createJavaBeanDeserializer(Class<?> clazz,
			Type type)
	{
		return new JavaBeanDeserializer(this, clazz, type);
	}

	public FieldDeserializer createFieldDeserializer(ParserConfig mapping,
			Class<?> clazz, FieldInfo fieldInfo)
	{
		Class<?> fieldClass = fieldInfo.getFieldClass();

		if (fieldClass == boolean.class || fieldClass == Boolean.class) { return new BooleanFieldDeserializer(
				mapping, clazz, fieldInfo); }

		if (fieldClass == int.class || fieldClass == Integer.class) { return new IntegerFieldDeserializer(
				mapping, clazz, fieldInfo); }

		if (fieldClass == long.class || fieldClass == Long.class) { return new LongFieldDeserializer(
				mapping, clazz, fieldInfo); }

		if (fieldClass == String.class) { return new StringFieldDeserializer(
				mapping, clazz, fieldInfo); }

		if (fieldClass == List.class || fieldClass == ArrayList.class) { return new ArrayListTypeFieldDeserializer(
				mapping, clazz, fieldInfo); }

		return new DefaultFieldDeserializer(mapping, clazz, fieldInfo);
	}

	public void putDeserializer(Type type, ObjectDeserializer deserializer)
	{
		derializers.put(type, deserializer);
	}

	public ObjectDeserializer getDeserializer(FieldInfo fieldInfo)
	{
		return getDeserializer(fieldInfo.getFieldClass(),
				fieldInfo.getFieldType());
	}

	public boolean isPrimitive(Class<?> clazz)
	{
		return primitiveClasses.contains(clazz);
	}

	public static Field getField(Class<?> clazz, String fieldName)
	{
		Field field = getField0(clazz, fieldName);
		if (field == null)
		{
			field = getField0(clazz, "_" + fieldName);
		}
		if (field == null)
		{
			field = getField0(clazz, "m_" + fieldName);
		}
		return field;
	}

	private static Field getField0(Class<?> clazz, String fieldName)
	{
		for (Field item : clazz.getDeclaredFields())
		{
			if (fieldName.equals(item.getName())) { return item; }
		}
		if (clazz.getSuperclass() != null
				&& clazz.getSuperclass() != Object.class) { return getField(
				clazz.getSuperclass(), fieldName); }

		return null;
	}

	public Map<String, FieldDeserializer> getFieldDeserializers(Class<?> clazz)
	{
		ObjectDeserializer deserizer = getDeserializer(clazz);

		if (deserizer instanceof JavaBeanDeserializer)
		{
			return ((JavaBeanDeserializer) deserizer).getFieldDeserializerMap();
		}
		else
		{
			return Collections.emptyMap();
		}
	}

}
