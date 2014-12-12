package com.cn.tfb.json.parser.deserializer;

import java.lang.reflect.Type;

public interface ExtraTypeProvider extends ParseProcess
{

	Type getExtraType(Object object, String key);
}
