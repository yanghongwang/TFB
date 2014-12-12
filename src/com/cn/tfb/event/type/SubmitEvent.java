package com.cn.tfb.event.type;

public class SubmitEvent extends BaseEvent
{
	private int id;

	public SubmitEvent()
	{

	}

	public SubmitEvent(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
}
