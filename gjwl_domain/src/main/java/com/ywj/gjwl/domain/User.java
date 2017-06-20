package com.ywj.gjwl.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class User extends BaseEntity{

	/*
	 *  USER_ID              VARCHAR(40) not null,
   DEPT_ID              VARCHAR(40),
   USER_NAME            VARCHAR(50) comment '不能重复,可为中文',
   PASSWORD             VARCHAR(64) comment 'shiro MD5密码32位',
   STATE                INT comment '1启用0停用',
   
   CREATE_BY            VARCHAR(40) comment '登录人编号',
   CREATE_DEPT          VARCHAR(40) comment '登录人所属部门编号',
   CREATE_TIME          TIMESTAMP,
   UPDATE_BY            VARCHAR(40),
   UPDATE_TIME          TIMESTAMP,
	 */
	
	private String id;//用户id
	private String userName;//用户名
	private String password;//用户密码
	private Integer state;//用户状态
	
	//===============================
	//用户与其扩展信息是一对一关系，这里就用用户表来维护其中的关系
	private Userinfo userinfo;
	private Dept dept;//用户所属于的部门
	private Set<Role> roles=new HashSet<Role>(0);//用户与角色多对多
	
	//===============================
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Dept getDept() {
		return dept;
	}
	public void setDept(Dept dept) {
		this.dept = dept;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Userinfo getUserinfo() {
		return userinfo;
	}
	public void setUserinfo(Userinfo userinfo) {
		this.userinfo = userinfo;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	

	
	
}
