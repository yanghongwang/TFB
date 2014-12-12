package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

public interface ObjectSerializer
{

	void write(JSONSerializer serializer, Object object, Object fieldName,
			Type fieldType) throws IOException;
}
