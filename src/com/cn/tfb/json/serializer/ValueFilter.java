package com.cn.tfb.json.serializer;

public interface ValueFilter extends SerializeFilter
{

	Object process(Object object, String name, Object value);
}
