package com.cn.tfb.dao.async;

import com.cn.tfb.dao.DaoException;

public class AsyncDaoException extends DaoException
{

	private static final long serialVersionUID = 5872157552005102382L;

	private final AsyncOperation failedOperation;

	public AsyncDaoException(AsyncOperation failedOperation, Throwable cause)
	{
		super(cause);
		this.failedOperation = failedOperation;
	}

	public AsyncOperation getFailedOperation()
	{
		return failedOperation;
	}

}
