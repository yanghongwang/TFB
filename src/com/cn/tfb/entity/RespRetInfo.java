package com.cn.tfb.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("retinfo")
public class RespRetInfo extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	// ��������
	private String rettype;
	// ������
	private String retcode;
	// ������Ϣ
	private String retmsg;

	public String getRettype()
	{
		return rettype;
	}

	public void setRettype(String rettype)
	{
		this.rettype = rettype;
	}

	public String getRetcode()
	{
		return retcode;
	}

	public void setRetcode(String retcode)
	{
		this.retcode = retcode;
	}

	public String getRetmsg()
	{
		return retmsg;
	}

	public void setRetmsg(String retmsg)
	{
		this.retmsg = retmsg;
	}

}
