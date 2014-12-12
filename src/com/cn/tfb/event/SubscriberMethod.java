package com.cn.tfb.event;

import java.lang.reflect.Method;

final class SubscriberMethod
{
	final Method method;
	final ThreadMode threadMode;
	final Class<?> eventType;
	String methodString;

	SubscriberMethod(Method method, ThreadMode threadMode, Class<?> eventType)
	{
		this.method = method;
		this.threadMode = threadMode;
		this.eventType = eventType;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof SubscriberMethod)
		{
			checkMethodString();
			SubscriberMethod otherSubscriberMethod = (SubscriberMethod) other;
			otherSubscriberMethod.checkMethodString();
			return methodString.equals(otherSubscriberMethod.methodString);
		}
		else
		{
			return false;
		}
	}

	private synchronized void checkMethodString()
	{
		if (methodString == null)
		{
			StringBuilder builder = new StringBuilder(64);
			builder.append(method.getDeclaringClass().getName());
			builder.append('#').append(method.getName());
			builder.append('(').append(eventType.getName());
			methodString = builder.toString();
		}
	}

	@Override
	public int hashCode()
	{
		return method.hashCode();
	}
}