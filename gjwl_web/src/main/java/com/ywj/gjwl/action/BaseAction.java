package com.ywj.gjwl.action;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.ywj.gjwl.domain.User;
import com.ywj.gjwl.utils.SysConstant;

/**
 * 
 * @author ywj
 *
 */

//通过RequestAware, SessionAware, ApplicationAware实行接口获得request,session,application对象，action中就可直接调用

public class BaseAction extends ActionSupport implements RequestAware, SessionAware, ApplicationAware{
	private static Logger log = Logger.getLogger(BaseAction.class);
	
	private static final long serialVersionUID = 1L;

	protected Map<String, Object> request;
	protected Map<String, Object> session;
	protected Map<String, Object> application;

	public Map<String, Object> getRequest() {
		return request;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public Map<String, Object> getApplication() {
		return application;
	}

	@Override
	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setApplication(Map<String, Object> application) {
		this.application = application;
	}
	
	/*
	 * 对象压入值栈中
	 */
	public void push(Object object){
		ActionContext.getContext().getValueStack().push(object);
	}
	
	/*
	 * 将键值对压入值栈中
	 */
	public void put(String key,Object value){
		ActionContext.getContext().put(key, value);
	}
	
	/**
	 * 
	* @Title: getCurUser 
	* @Description: 获取当前用户
	* @param @return    
	* @return User    
	* @throws
	 */
	public User getCurUser(){
		return (User) session.get(SysConstant.CURRENT_USER_INFO);
	}

}
