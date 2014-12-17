package com.cn.tfb.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("operation_request")
public class ResponseEntity extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	// 响应包头
	private RespHeader msgheader;
	// 响应包体
	private RespBody msgbody;

	public RespHeader getMsgheader()
	{
		return msgheader;
	}

	public void setMsgheader(RespHeader msgheader)
	{
		this.msgheader = msgheader;
	}

	public RespBody getMsgbody()
	{
		return msgbody;
	}

	public void setMsgbody(RespBody msgbody)
	{
		this.msgbody = msgbody;
	}

}
