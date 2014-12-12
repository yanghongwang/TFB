package com.cn.tfb.event;

public enum ThreadMode
{
	// 事件的处理和事件的发送在相同的进程,所以事件处理时间不应太长，
	// 不然影响事件的发送线程，而这个线程可能是UI线程,对应的函数名是onEvent
	PostThread,
	// 事件的处理会在UI线程中执行。事件处理时间不能太长，这个不用说的，
	// 长了会ANR的，对应的函数名是onEventMainThread
	MainThread,
	// 事件的处理会在一个后台线程中执行，对应的函数名是onEventBackgroundThread，
	// 虽然名字是BackgroundThread，事件处理是在后台线程，但事件处理时间还是不应该太长，
	// 因为如果发送事件的线程是后台线程，会直接执行事件，如果当前线程是UI线程，
	// 事件会被加到一个队列中，由一个线程依次处理这些事件，
	// 如果某个事件处理时间太长，会阻塞后面的事件的派发或处理
	BackgroundThread,
	// 事件处理会在单独的线程中执行，主要用于在后台线程中执行耗时操作，
	// 每个事件会开启一个线程（有线程池），但最好限制线程的数目
	Async
}