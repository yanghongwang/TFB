package com.cn.tfb.event;

public enum ThreadMode
{

	//事件响应函数和事件发生在同一线程中
	PostThread,
	//事件响应函数在Android的主线程中
	MainThread,
	//事件响应函数在一个后台线程中执行
	BackgroundThread,
	//事件响应函数在一个异步线程中执行
	Async
}