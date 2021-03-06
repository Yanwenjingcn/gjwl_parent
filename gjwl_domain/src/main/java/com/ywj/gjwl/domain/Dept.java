package com.ywj.gjwl.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Dept implements Serializable {
//	create table DEPT_P
//	(
//	   DEPT_ID              varchar(40) not null,
//	   DEPT_NAME            varchar(50),
//	   PARENT_ID            varchar(40) comment '自关联',
//	   STATE                INT comment '1启用0停用',
//	   primary key (DEPT_ID)
//	);

	
	//po的七个定律，
	private String id;//部门id号
	
	//部门与用户是一对多的关系，那么在部门表中就要加上用户的set集合
	//注意不要忘了它的getset方法
	private Set<User> users=new HashSet<User>(0);
	
	private String deptName;//部门名称
	//数据库里面对应的外键什么的，在po里面要写成对应的对象。
	private Dept parent;//父部门，自关联，组部门与父部门，多对一
	private Integer state;//状态    1代表启用    0代表停用
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Dept getParent() {
		return parent;
	}
	public void setParent(Dept parent) {
		this.parent = parent;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	


}
