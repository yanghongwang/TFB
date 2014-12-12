package com.cn.tfb.event;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Looper;
import android.util.Log;

public class EventBus
{
	static ExecutorService executorService = Executors.newCachedThreadPool();

	public static String TAG = "Event";

	private static volatile EventBus defaultInstance;

	private static final String DEFAULT_METHOD_NAME = "onEvent";
	private static final Map<Class<?>, List<Class<?>>> eventTypesCache = new HashMap<Class<?>, List<Class<?>>>();

	private final Map<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType;
	private final Map<Object, List<Class<?>>> typesBySubscriber;
	private final Map<Class<?>, Object> stickyEvents;

	private final ThreadLocal<PostingThreadState> currentPostingThreadState = new ThreadLocal<PostingThreadState>()
	{
		@Override
		protected PostingThreadState initialValue()
		{
			return new PostingThreadState();
		}
	};

	private final HandlerPoster mainThreadPoster;
	private final BackgroundPoster backgroundPoster;
	private final AsyncPoster asyncPoster;
	private final SubscriberMethodFinder subscriberMethodFinder;

	private boolean subscribed;
	private boolean logSubscriberExceptions;

	public static EventBus getDefault()
	{
		if (defaultInstance == null)
		{
			synchronized (EventBus.class)
			{
				if (defaultInstance == null)
				{
					defaultInstance = new EventBus();
				}
			}
		}
		return defaultInstance;
	}

	public static void clearCaches()
	{
		SubscriberMethodFinder.clearCaches();
		eventTypesCache.clear();
	}

	public static void skipMethodVerificationFor(Class<?> clazz)
	{
		SubscriberMethodFinder.skipMethodVerificationFor(clazz);
	}

	public static void clearSkipMethodNameVerifications()
	{
		SubscriberMethodFinder.clearSkipMethodVerifications();
	}

	public EventBus()
	{
		subscriptionsByEventType = new HashMap<Class<?>, CopyOnWriteArrayList<Subscription>>();
		typesBySubscriber = new HashMap<Object, List<Class<?>>>();
		stickyEvents = new ConcurrentHashMap<Class<?>, Object>();
		mainThreadPoster = new HandlerPoster(this, Looper.getMainLooper(), 10);
		backgroundPoster = new BackgroundPoster(this);
		asyncPoster = new AsyncPoster(this);
		subscriberMethodFinder = new SubscriberMethodFinder();
		logSubscriberExceptions = true;
	}

	public void configureLogSubscriberExceptions(boolean logSubscriberExceptions)
	{
		if (subscribed)
		{
			throw new EventBusException(
					"This method must be called before any registration");
		}
		this.logSubscriberExceptions = logSubscriberExceptions;
	}

	public void register(Object subscriber)
	{
		register(subscriber, DEFAULT_METHOD_NAME, false, 0);
	}

	public void register(Object subscriber, int priority)
	{
		register(subscriber, DEFAULT_METHOD_NAME, false, priority);
	}

	@Deprecated
	public void register(Object subscriber, String methodName)
	{
		register(subscriber, methodName, false, 0);
	}

	public void registerSticky(Object subscriber)
	{
		register(subscriber, DEFAULT_METHOD_NAME, true, 0);
	}

	public void registerSticky(Object subscriber, int priority)
	{
		register(subscriber, DEFAULT_METHOD_NAME, true, priority);
	}

	@Deprecated
	public void registerSticky(Object subscriber, String methodName)
	{
		register(subscriber, methodName, true, 0);
	}

	private synchronized void register(Object subscriber, String methodName,
			boolean sticky, int priority)
	{
		List<SubscriberMethod> subscriberMethods = subscriberMethodFinder
				.findSubscriberMethods(subscriber.getClass(), methodName);
		for (SubscriberMethod subscriberMethod : subscriberMethods)
		{
			subscribe(subscriber, subscriberMethod, sticky, priority);
		}
	}

	@Deprecated
	public void register(Object subscriber, Class<?> eventType,
			Class<?>... moreEventTypes)
	{
		register(subscriber, DEFAULT_METHOD_NAME, false, eventType,
				moreEventTypes);
	}

	@Deprecated
	public void register(Object subscriber, String methodName,
			Class<?> eventType, Class<?>... moreEventTypes)
	{
		register(subscriber, methodName, false, eventType, moreEventTypes);
	}

	@Deprecated
	public void registerSticky(Object subscriber, Class<?> eventType,
			Class<?>... moreEventTypes)
	{
		register(subscriber, DEFAULT_METHOD_NAME, true, eventType,
				moreEventTypes);
	}

	@Deprecated
	public void registerSticky(Object subscriber, String methodName,
			Class<?> eventType, Class<?>... moreEventTypes)
	{
		register(subscriber, methodName, true, eventType, moreEventTypes);
	}

	private synchronized void register(Object subscriber, String methodName,
			boolean sticky, Class<?> eventType, Class<?>... moreEventTypes)
	{
		Class<?> subscriberClass = subscriber.getClass();
		List<SubscriberMethod> subscriberMethods = subscriberMethodFinder
				.findSubscriberMethods(subscriberClass, methodName);
		for (SubscriberMethod subscriberMethod : subscriberMethods)
		{
			if (eventType == subscriberMethod.eventType)
			{
				subscribe(subscriber, subscriberMethod, sticky, 0);
			}
			else if (moreEventTypes != null)
			{
				for (Class<?> eventType2 : moreEventTypes)
				{
					if (eventType2 == subscriberMethod.eventType)
					{
						subscribe(subscriber, subscriberMethod, sticky, 0);
						break;
					}
				}
			}
		}
	}

	private void subscribe(Object subscriber,
			SubscriberMethod subscriberMethod, boolean sticky, int priority)
	{
		subscribed = true;
		Class<?> eventType = subscriberMethod.eventType;
		CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType
				.get(eventType);
		Subscription newSubscription = new Subscription(subscriber,
				subscriberMethod, priority);
		if (subscriptions == null)
		{
			subscriptions = new CopyOnWriteArrayList<Subscription>();
			subscriptionsByEventType.put(eventType, subscriptions);
		}
		else
		{
			for (Subscription subscription : subscriptions)
			{
				if (subscription.equals(newSubscription))
				{
					throw new EventBusException("Subscriber "
							+ subscriber.getClass()
							+ " already registered to event " + eventType);
				}
			}
		}

		int size = subscriptions.size();
		for (int i = 0; i <= size; i++)
		{
			if (i == size
					|| newSubscription.priority > subscriptions.get(i).priority)
			{
				subscriptions.add(i, newSubscription);
				break;
			}
		}

		List<Class<?>> subscribedEvents = typesBySubscriber.get(subscriber);
		if (subscribedEvents == null)
		{
			subscribedEvents = new ArrayList<Class<?>>();
			typesBySubscriber.put(subscriber, subscribedEvents);
		}
		subscribedEvents.add(eventType);

		if (sticky)
		{
			Object stickyEvent;
			synchronized (stickyEvents)
			{
				stickyEvent = stickyEvents.get(eventType);
			}
			if (stickyEvent != null)
			{
				postToSubscription(newSubscription, stickyEvent,
						Looper.getMainLooper() == Looper.myLooper());
			}
		}
	}

	public synchronized boolean isRegistered(Object subscriber)
	{
		return typesBySubscriber.containsKey(subscriber);
	}

	@Deprecated
	public synchronized void unregister(Object subscriber,
			Class<?>... eventTypes)
	{
		if (eventTypes.length == 0)
		{
			throw new IllegalArgumentException(
					"Provide at least one event class");
		}
		List<Class<?>> subscribedClasses = typesBySubscriber.get(subscriber);
		if (subscribedClasses != null)
		{
			for (Class<?> eventType : eventTypes)
			{
				unubscribeByEventType(subscriber, eventType);
				subscribedClasses.remove(eventType);
			}
			if (subscribedClasses.isEmpty())
			{
				typesBySubscriber.remove(subscriber);
			}
		}
		else
		{
			Log.w(TAG, "Subscriber to unregister was not registered before: "
					+ subscriber.getClass());
		}
	}

	private void unubscribeByEventType(Object subscriber, Class<?> eventType)
	{
		List<Subscription> subscriptions = subscriptionsByEventType
				.get(eventType);
		if (subscriptions != null)
		{
			int size = subscriptions.size();
			for (int i = 0; i < size; i++)
			{
				Subscription subscription = subscriptions.get(i);
				if (subscription.subscriber == subscriber)
				{
					subscription.active = false;
					subscriptions.remove(i);
					i--;
					size--;
				}
			}
		}
	}

	public synchronized void unregister(Object subscriber)
	{
		List<Class<?>> subscribedTypes = typesBySubscriber.get(subscriber);
		if (subscribedTypes != null)
		{
			for (Class<?> eventType : subscribedTypes)
			{
				unubscribeByEventType(subscriber, eventType);
			}
			typesBySubscriber.remove(subscriber);
		}
		else
		{
			Log.w(TAG, "Subscriber to unregister was not registered before: "
					+ subscriber.getClass());
		}
	}

	public void post(Object event)
	{
		PostingThreadState postingState = currentPostingThreadState.get();
		List<Object> eventQueue = postingState.eventQueue;
		eventQueue.add(event);

		if (postingState.isPosting)
		{
			return;
		}
		else
		{
			postingState.isMainThread = Looper.getMainLooper() == Looper
					.myLooper();
			postingState.isPosting = true;
			if (postingState.canceled)
			{
				throw new EventBusException(
						"Internal error. Abort state was not reset");
			}
			try
			{
				while (!eventQueue.isEmpty())
				{
					postSingleEvent(eventQueue.remove(0), postingState);
				}
			}
			finally
			{
				postingState.isPosting = false;
				postingState.isMainThread = false;
			}
		}
	}

	public void cancelEventDelivery(Object event)
	{
		PostingThreadState postingState = currentPostingThreadState.get();
		if (!postingState.isPosting)
		{
			throw new EventBusException(
					"This method may only be called from inside event handling methods on the posting thread");
		}
		else if (event == null)
		{
			throw new EventBusException("Event may not be null");
		}
		else if (postingState.event != event)
		{
			throw new EventBusException(
					"Only the currently handled event may be aborted");
		}
		else if (postingState.subscription.subscriberMethod.threadMode != ThreadMode.PostThread)
		{
			throw new EventBusException(
					" event handlers may only abort the incoming event");
		}

		postingState.canceled = true;
	}

	public void postSticky(Object event)
	{
		synchronized (stickyEvents)
		{
			stickyEvents.put(event.getClass(), event);
		}
		post(event);
	}

	public <T> T getStickyEvent(Class<T> eventType)
	{
		synchronized (stickyEvents)
		{
			return eventType.cast(stickyEvents.get(eventType));
		}
	}

	public <T> T removeStickyEvent(Class<T> eventType)
	{
		synchronized (stickyEvents)
		{
			return eventType.cast(stickyEvents.remove(eventType));
		}
	}

	public boolean removeStickyEvent(Object event)
	{
		synchronized (stickyEvents)
		{
			Class<? extends Object> eventType = event.getClass();
			Object existingEvent = stickyEvents.get(eventType);
			if (event.equals(existingEvent))
			{
				stickyEvents.remove(eventType);
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	public void removeAllStickyEvents()
	{
		synchronized (stickyEvents)
		{
			stickyEvents.clear();
		}
	}

	private void postSingleEvent(Object event, PostingThreadState postingState)
			throws Error
	{
		Class<? extends Object> eventClass = event.getClass();
		List<Class<?>> eventTypes = findEventTypes(eventClass);
		boolean subscriptionFound = false;
		int countTypes = eventTypes.size();
		for (int h = 0; h < countTypes; h++)
		{
			Class<?> clazz = eventTypes.get(h);
			CopyOnWriteArrayList<Subscription> subscriptions;
			synchronized (this)
			{
				subscriptions = subscriptionsByEventType.get(clazz);
			}
			if (subscriptions != null && !subscriptions.isEmpty())
			{
				for (Subscription subscription : subscriptions)
				{
					postingState.event = event;
					postingState.subscription = subscription;
					boolean aborted = false;
					try
					{
						postToSubscription(subscription, event,
								postingState.isMainThread);
						aborted = postingState.canceled;
					}
					finally
					{
						postingState.event = null;
						postingState.subscription = null;
						postingState.canceled = false;
					}
					if (aborted)
					{
						break;
					}
				}
				subscriptionFound = true;
			}
		}
		if (!subscriptionFound)
		{
			Log.d(TAG, "No subscribers registered for event " + eventClass);
			if (eventClass != NoSubscriberEvent.class
					&& eventClass != SubscriberExceptionEvent.class)
			{
				post(new NoSubscriberEvent(this, event));
			}
		}
	}

	private void postToSubscription(Subscription subscription, Object event,
			boolean isMainThread)
	{
		switch (subscription.subscriberMethod.threadMode)
		{
			case PostThread:
				invokeSubscriber(subscription, event);
				break;
			case MainThread:
				if (isMainThread)
				{
					invokeSubscriber(subscription, event);
				}
				else
				{
					mainThreadPoster.enqueue(subscription, event);
				}
				break;
			case BackgroundThread:
				if (isMainThread)
				{
					backgroundPoster.enqueue(subscription, event);
				}
				else
				{
					invokeSubscriber(subscription, event);
				}
				break;
			case Async:
				asyncPoster.enqueue(subscription, event);
				break;
			default:
				throw new IllegalStateException("Unknown thread mode: "
						+ subscription.subscriberMethod.threadMode);
		}
	}

	private List<Class<?>> findEventTypes(Class<?> eventClass)
	{
		synchronized (eventTypesCache)
		{
			List<Class<?>> eventTypes = eventTypesCache.get(eventClass);
			if (eventTypes == null)
			{
				eventTypes = new ArrayList<Class<?>>();
				Class<?> clazz = eventClass;
				while (clazz != null)
				{
					eventTypes.add(clazz);
					addInterfaces(eventTypes, clazz.getInterfaces());
					clazz = clazz.getSuperclass();
				}
				eventTypesCache.put(eventClass, eventTypes);
			}
			return eventTypes;
		}
	}

	static void addInterfaces(List<Class<?>> eventTypes, Class<?>[] interfaces)
	{
		for (Class<?> interfaceClass : interfaces)
		{
			if (!eventTypes.contains(interfaceClass))
			{
				eventTypes.add(interfaceClass);
				addInterfaces(eventTypes, interfaceClass.getInterfaces());
			}
		}
	}

	void invokeSubscriber(PendingPost pendingPost)
	{
		Object event = pendingPost.event;
		Subscription subscription = pendingPost.subscription;
		PendingPost.releasePendingPost(pendingPost);
		if (subscription.active)
		{
			invokeSubscriber(subscription, event);
		}
	}

	void invokeSubscriber(Subscription subscription, Object event) throws Error
	{
		try
		{
			subscription.subscriberMethod.method.invoke(
					subscription.subscriber, event);
		}
		catch (InvocationTargetException e)
		{
			Throwable cause = e.getCause();
			if (event instanceof SubscriberExceptionEvent)
			{
				Log.e(TAG, "SubscriberExceptionEvent subscriber "
						+ subscription.subscriber.getClass()
						+ " threw an exception", cause);
				SubscriberExceptionEvent exEvent = (SubscriberExceptionEvent) event;
				Log.e(TAG, "Initial event " + exEvent.causingEvent
						+ " caused exception in " + exEvent.causingSubscriber,
						exEvent.throwable);
			}
			else
			{
				if (logSubscriberExceptions)
				{
					Log.e(TAG, "Could not dispatch event: " + event.getClass()
							+ " to subscribing class "
							+ subscription.subscriber.getClass(), cause);
				}
				SubscriberExceptionEvent exEvent = new SubscriberExceptionEvent(
						this, cause, event, subscription.subscriber);
				post(exEvent);
			}
		}
		catch (IllegalAccessException e)
		{
			throw new IllegalStateException("Unexpected exception", e);
		}
	}

	final static class PostingThreadState
	{
		List<Object> eventQueue = new ArrayList<Object>();
		boolean isPosting;
		boolean isMainThread;
		Subscription subscription;
		Object event;
		boolean canceled;
	}

	interface PostCallback
	{
		void onPostCompleted(List<SubscriberExceptionEvent> exceptionEvents);
	}

}
