package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

import com.cn.tfb.json.JSONStreamAware;

public class JSONStreamAwareSerializer implements ObjectSerializer
{

	public static JSONStreamAwareSerializer instance = new JSONStreamAwareSerializer();

	public void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{
		SerializeWriter out = serializer.getWriter();

		JSONStreamAware aware = (JSONStreamAware) object;
		aware.writeJSONString(out);
	}
}
