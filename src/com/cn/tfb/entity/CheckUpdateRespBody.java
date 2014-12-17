package com.cn.tfb.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("msgbody")
public class CheckUpdateRespBody extends RespBody
{
	private static final long serialVersionUID = 1L;
	// APP����
	private String apptype;
	// ���°汾��
	private String appnewversion;
	// �Ƿ����°�
	private String appisnew;
	// �Ƿ���Ҫ�������
	private String clearoldinfo;
	// �°汾���ص�ַ
	private String appdownurl;
	// ��������
	private String appnewcontent;
	// �Ƿ�ǿ�Ƹ���
	private String appstrupdate;
	// �ı��¼ģʽ
	private String chargeWithoutLogin;

	public String getChargeWithoutLogin()
	{
		return chargeWithoutLogin;
	}

	public void setChargeWithoutLogin(String chargeWithoutLogin)
	{
		this.chargeWithoutLogin = chargeWithoutLogin;
	}

	public String getApptype()
	{
		return apptype;
	}

	public void setApptype(String apptype)
	{
		this.apptype = apptype;
	}

	public String getAppnewversion()
	{
		return appnewversion;
	}

	public void setAppnewversion(String appnewversion)
	{
		this.appnewversion = appnewversion;
	}

	public String getAppisnew()
	{
		return appisnew;
	}

	public void setAppisnew(String appisnew)
	{
		this.appisnew = appisnew;
	}

	public String getClearoldinfo()
	{
		return clearoldinfo;
	}

	public void setClearoldinfo(String clearoldinfo)
	{
		this.clearoldinfo = clearoldinfo;
	}

	public String getAppdownurl()
	{
		return appdownurl;
	}

	public void setAppdownurl(String appdownurl)
	{
		this.appdownurl = appdownurl;
	}

	public String getAppnewcontent()
	{
		return appnewcontent;
	}

	public void setAppnewcontent(String appnewcontent)
	{
		this.appnewcontent = appnewcontent;
	}

	public String getAppstrupdate()
	{
		return appstrupdate;
	}

	public void setAppstrupdate(String appstrupdate)
	{
		this.appstrupdate = appstrupdate;
	}

}
