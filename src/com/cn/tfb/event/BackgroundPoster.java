package com.cn.tfb.event;

import android.util.Log;

final class BackgroundPoster implements Runnable
{

	private final PendingPostQueue queue;
	private volatile boolean executorRunning;

	private final EventBus eventBus;

	BackgroundPoster(EventBus eventBus)
	{
		this.eventBus = eventBus;
		queue = new PendingPostQueue();
	}

	public void enqueue(Subscription subscription, Object event)
	{
		PendingPost pendingPost = PendingPost.obtainPendingPost(subscription,
				event);
		synchronized (this)
		{
			queue.enqueue(pendingPost);
			if (!executorRunning)
			{
				executorRunning = true;
				EventBus.executorService.execute(this);
			}
		}
	}

	@Override
	public void run()
	{
		try
		{
			try
			{
				while (true)
				{
					PendingPost pendingPost = queue.poll(1000);
					if (pendingPost == null)
					{
						synchronized (this)
						{
							pendingPost = queue.poll();
							if (pendingPost == null)
							{
								executorRunning = false;
								return;
							}
						}
					}
					eventBus.invokeSubscriber(pendingPost);
				}
			}
			catch (InterruptedException e)
			{
				Log.w("Event", Thread.currentThread().getName()
						+ " was interruppted", e);
			}
		}
		finally
		{
			executorRunning = false;
		}
	}

}
