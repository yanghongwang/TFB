package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

public interface JSONSerializable
{
	void write(JSONSerializer serializer, Object fieldName, Type fieldType)
			throws IOException;
}
