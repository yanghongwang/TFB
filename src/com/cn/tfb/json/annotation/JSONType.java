package com.cn.tfb.json.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cn.tfb.json.parser.Feature;
import com.cn.tfb.json.serializer.SerializerFeature;

@Retention(RetentionPolicy.RUNTIME)
@Target(
{ ElementType.TYPE })
public @interface JSONType
{

	boolean asm() default true;

	String[] orders() default
	{};

	String[] ignores() default
	{};

	SerializerFeature[] serialzeFeatures() default
	{};

	Feature[] parseFeatures() default
	{};

	boolean alphabetic() default true;

	Class<?> mappingTo() default Void.class;
}
