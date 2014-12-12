package com.cn.tfb.event;

final class Subscription
{
	final Object subscriber;
	final SubscriberMethod subscriberMethod;
	final int priority;
	volatile boolean active;

	Subscription(Object subscriber, SubscriberMethod subscriberMethod,
			int priority)
	{
		this.subscriber = subscriber;
		this.subscriberMethod = subscriberMethod;
		this.priority = priority;
		active = true;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof Subscription)
		{
			Subscription otherSubscription = (Subscription) other;
			return subscriber == otherSubscription.subscriber
					&& subscriberMethod
							.equals(otherSubscription.subscriberMethod);
		}
		else
		{
			return false;
		}
	}

	@Override
	public int hashCode()
	{
		return subscriber.hashCode() + subscriberMethod.methodString.hashCode();
	}
}