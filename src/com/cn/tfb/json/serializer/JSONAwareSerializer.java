package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

import com.cn.tfb.json.JSONAware;

public class JSONAwareSerializer implements ObjectSerializer
{

	public static JSONAwareSerializer instance = new JSONAwareSerializer();

	public void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{
		SerializeWriter out = serializer.getWriter();

		JSONAware aware = (JSONAware) object;
		out.write(aware.toJSONString());
	}
}
