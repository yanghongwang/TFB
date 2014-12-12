package com.cn.tfb.json.serializer;

public interface PropertyFilter extends SerializeFilter
{
	boolean apply(Object object, String name, Object value);
}
