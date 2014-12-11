package com.cn.tfb.dao.async;

public interface AsyncOperationListener
{
	void onAsyncOperationCompleted(AsyncOperation operation);
}
