package com.cn.tfb.event;

import android.util.Log;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class SubscriberMethodFinder
{
	private static final String ON_EVENT_METHOD_NAME = "onEvent";

	private static final int BRIDGE = 0x40;
	private static final int SYNTHETIC = 0x1000;

	private static final int MODIFIERS_IGNORE = Modifier.ABSTRACT
			| Modifier.STATIC | BRIDGE | SYNTHETIC;
	private static final Map<String, List<SubscriberMethod>> methodCache = new HashMap<String, List<SubscriberMethod>>();

	private final Map<Class<?>, Class<?>> skipMethodVerificationForClasses;

	SubscriberMethodFinder(List<Class<?>> skipMethodVerificationForClassesList)
	{
		skipMethodVerificationForClasses = new ConcurrentHashMap<Class<?>, Class<?>>();
		if (skipMethodVerificationForClassesList != null)
		{
			for (Class<?> clazz : skipMethodVerificationForClassesList)
			{
				skipMethodVerificationForClasses.put(clazz, clazz);
			}
		}
	}

	List<SubscriberMethod> findSubscriberMethods(Class<?> subscriberClass)
	{
		String key = subscriberClass.getName();
		List<SubscriberMethod> subscriberMethods;
		synchronized (methodCache)
		{
			subscriberMethods = methodCache.get(key);
		}
		if (subscriberMethods != null) { return subscriberMethods; }
		subscriberMethods = new ArrayList<SubscriberMethod>();
		Class<?> clazz = subscriberClass;
		HashSet<String> eventTypesFound = new HashSet<String>();
		StringBuilder methodKeyBuilder = new StringBuilder();
		while (clazz != null)
		{
			String name = clazz.getName();
			if (name.startsWith("java.") || name.startsWith("javax.")
					|| name.startsWith("android."))
			{
				// Skip system classes, this just degrades performance
				break;
			}

			// Starting with EventBus 2.2 we enforced methods to be public
			// (might change with annotations again)
			Method[] methods = clazz.getDeclaredMethods();
			for (Method method : methods)
			{
				String methodName = method.getName();
				if (methodName.startsWith(ON_EVENT_METHOD_NAME))
				{
					int modifiers = method.getModifiers();
					if ((modifiers & Modifier.PUBLIC) != 0
							&& (modifiers & MODIFIERS_IGNORE) == 0)
					{
						Class<?>[] parameterTypes = method.getParameterTypes();
						if (parameterTypes.length == 1)
						{
							String modifierString = methodName
									.substring(ON_EVENT_METHOD_NAME.length());
							ThreadMode threadMode;
							if (modifierString.length() == 0)
							{
								threadMode = ThreadMode.PostThread;
							}
							else if (modifierString.equals("MainThread"))
							{
								threadMode = ThreadMode.MainThread;
							}
							else if (modifierString.equals("BackgroundThread"))
							{
								threadMode = ThreadMode.BackgroundThread;
							}
							else if (modifierString.equals("Async"))
							{
								threadMode = ThreadMode.Async;
							}
							else
							{
								if (skipMethodVerificationForClasses
										.containsKey(clazz))
								{
									continue;
								}
								else
								{
									throw new EventBusException(
											"Illegal onEvent method, check for typos: "
													+ method);
								}
							}
							Class<?> eventType = parameterTypes[0];
							methodKeyBuilder.setLength(0);
							methodKeyBuilder.append(methodName);
							methodKeyBuilder.append('>').append(
									eventType.getName());
							String methodKey = methodKeyBuilder.toString();
							if (eventTypesFound.add(methodKey))
							{
								// Only add if not already found in a sub class
								subscriberMethods.add(new SubscriberMethod(
										method, threadMode, eventType));
							}
						}
					}
					else if (!skipMethodVerificationForClasses
							.containsKey(clazz))
					{
						Log.d(EventBus.TAG,
								"Skipping method (not public, static or abstract): "
										+ clazz + "." + methodName);
					}
				}
			}
			clazz = clazz.getSuperclass();
		}
		if (subscriberMethods.isEmpty())
		{
			throw new EventBusException("Subscriber " + subscriberClass
					+ " has no public methods called " + ON_EVENT_METHOD_NAME);
		}
		else
		{
			synchronized (methodCache)
			{
				methodCache.put(key, subscriberMethods);
			}
			return subscriberMethods;
		}
	}

	static void clearCaches()
	{
		synchronized (methodCache)
		{
			methodCache.clear();
		}
	}

}
