package com.cn.tfb.entity;

public class RespHeader extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	// ��Ȩ��
	private String req_token;
	// ��̬��
	private String au_token;
	// ��������
	private String req_bkenv;
	// ���ؽ��
	private RespRetInfo retinfo;

	public String getReq_token()
	{
		return req_token;
	}

	public void setReq_token(String req_token)
	{
		this.req_token = req_token;
	}

	public String getAu_token()
	{
		return au_token;
	}

	public void setAu_token(String au_token)
	{
		this.au_token = au_token;
	}

	public String getReq_bkenv()
	{
		return req_bkenv;
	}

	public void setReq_bkenv(String req_bkenv)
	{
		this.req_bkenv = req_bkenv;
	}

	public RespRetInfo getRetinfo()
	{
		return retinfo;
	}

	public void setRetinfo(RespRetInfo retinfo)
	{
		this.retinfo = retinfo;
	}

}
