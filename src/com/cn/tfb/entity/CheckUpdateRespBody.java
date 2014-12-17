package com.cn.tfb.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("msgbody")
public class CheckUpdateRespBody extends RespBody
{
	private static final long serialVersionUID = 1L;
	// APP类型
	private String apptype;
	// 最新版本号
	private String appnewversion;
	// 是否有新版
	private String appisnew;
	// 是否需要清除缓存
	private String clearoldinfo;
	// 新版本下载地址
	private String appdownurl;
	// 更新内容
	private String appnewcontent;
	// 是否强制更新
	private String appstrupdate;
	// 改变登录模式
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
