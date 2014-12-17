package com.cn.tfb.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("msgbody")
public class LoginRespBody extends RespBody
{
	private static final long serialVersionUID = 1L;
	// 操作者ID
	private String authorid;
	// 认证代理
	private String relateAgent;
	// 代理商编号
	private String agentid;
	// 代理商类型
	private String agenttypeid;
	// 手势密码
	private String gesturepasswd;

	public String getRelateAgent()
	{
		return relateAgent;
	}

	public void setRelateAgent(String relateAgent)
	{
		this.relateAgent = relateAgent;
	}

	public String getAgentid()
	{
		return agentid;
	}

	public void setAgentid(String agentid)
	{
		this.agentid = agentid;
	}

	public String getAgenttypeid()
	{
		return agenttypeid;
	}

	public void setAgenttypeid(String agenttypeid)
	{
		this.agenttypeid = agenttypeid;
	}

	public String getGesturepasswd()
	{
		return gesturepasswd;
	}

	public void setGesturepasswd(String gesturepasswd)
	{
		this.gesturepasswd = gesturepasswd;
	}

	public String getAuthorid()
	{
		return authorid;
	}

	public void setAuthorid(String authorid)
	{
		this.authorid = authorid;
	}

}
