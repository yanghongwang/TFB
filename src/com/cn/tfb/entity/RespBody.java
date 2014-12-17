package com.cn.tfb.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("msgbody")
public class RespBody extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	// ����˵��
	private String result;
	// ��ʾ��Ϣ
	private String message;

	public String getResult()
	{
		return result;
	}

	public void setResult(String result)
	{
		this.result = result;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
}
