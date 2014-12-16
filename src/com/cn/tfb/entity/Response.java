package com.cn.tfb.entity;

public class Response<T> extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	// 响应包头
	private RespHeader msgheader;
	// 响应包体
	private RespBody<T> msgbody;

	public RespHeader getMsgheader()
	{
		return msgheader;
	}

	public void setMsgheader(RespHeader msgheader)
	{
		this.msgheader = msgheader;
	}

	public RespBody<T> getMsgbody()
	{
		return msgbody;
	}

	public void setMsgbody(RespBody<T> msgbody)
	{
		this.msgbody = msgbody;
	}

}
