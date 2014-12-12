package com.cn.tfb.json.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cn.tfb.json.parser.Feature;
import com.cn.tfb.json.serializer.SerializerFeature;

@Retention(RetentionPolicy.RUNTIME)
@Target(
{ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
public @interface JSONField
{
	int ordinal() default 0;

	String name() default "";

	String format() default "";

	boolean serialize() default true;

	boolean deserialize() default true;

	SerializerFeature[] serialzeFeatures() default
	{};

	Feature[] parseFeatures() default
	{};
}
