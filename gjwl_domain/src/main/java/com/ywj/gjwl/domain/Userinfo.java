package com.ywj.gjwl.domain;


import java.util.Date;
/*
 * USER_INFO_ID         VARCHAR(40) not null,
   NAME                 VARCHAR(20),
   MANAGER_ID           VARCHAR(40),
   JOIN_DATE            TIMESTAMP,
   SALARY               NUMERIC(8, 2),
   BIRTHDAY             TIMESTAMP,
   GENDER               CHAR(1),
   STATION              VARCHAR(20),
   TELEPHONE            VARCHAR(100),
   DEGREE               INT comment '0-超级管理员
            1-跨部门跨人员
            2-管理所有下属部门和人员
            3-管理本部门
            4-普通员工
            
            
            0作为内部控制只对sysdebug，用户不能进行添加',
   REMARK               VARCHAR(600),
   ORDER_NO             INT,
   CREATE_BY            VARCHAR(40) comment '登录人编号',
   CREATE_DEPT          VARCHAR(40) comment '登录人所属部门编号',
   CREATE_TIME          TIMESTAMP,
   UPDATE_BY            VARCHAR(40),
   UPDATE_TIME          TIMESTAMP,
 */



public class Userinfo extends BaseEntity {
	
	private String id;//id
	private String name;//姓名
	//=====================================================
	private User manager;//直属领导，多个用户有同一个直属领导
	//=====================================================
	private Date joinDate;//入职时间
	private Double salary;//薪水
	private Date birthday;//出生年月
	private String gender;//性别，char的也用string类型
	private String station;//岗位
	private String telephone;//电话
	private Integer degree;//等级
	private String remark;//说明信息，备注
	private Integer orderNo;//排序号
	private String email;
	
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
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

	public User getManager() {
		return manager;
	}
	public void setManager(User manager) {
		this.manager = manager;
	}
	public Date getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
	public Double getSalary() {
		return salary;
	}
	public void setSalary(Double salary) {
		this.salary = salary;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public Integer getDegree() {
		return degree;
	}
	public void setDegree(Integer degree) {
		this.degree = degree;
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
	
	

	
}
