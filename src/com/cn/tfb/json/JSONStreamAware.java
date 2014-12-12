package com.cn.tfb.json;

import java.io.IOException;

public interface JSONStreamAware
{

	void writeJSONString(Appendable out) throws IOException;
}
