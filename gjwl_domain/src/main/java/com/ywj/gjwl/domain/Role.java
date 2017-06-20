package com.ywj.gjwl.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Role extends BaseEntity {
	/*
	 *  ROLE_ID              VARCHAR(40) not null,
   NAME                 VARCHAR(30),
   REMARK               VARCHAR(200),
   ORDER_NO             INT,
   
   CREATE_BY            VARCHAR(40),
   CREATE_DEPT          VARCHAR(40),
   CREATE_TIME          TIMESTAMP,
   UPDATE_BY            VARCHAR(40),
   UPDATE_TIME          TIMESTAMP,
	 */
	private String id;
	private String name;//角色名
	private String remark;//备注 
	private Integer orderNo;//排序号
	
	//============================
	//角色与用户是多对多的关系
	private Set<User> users=new HashSet<User>(0);
	//============================
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	
	
	
	

}
