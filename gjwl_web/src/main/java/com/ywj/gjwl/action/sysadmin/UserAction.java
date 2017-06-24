package com.ywj.gjwl.action.sysadmin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;



import com.opensymphony.xwork2.ModelDriven;
import com.ywj.gjwl.action.BaseAction;
import com.ywj.gjwl.domain.Dept;
import com.ywj.gjwl.domain.Role;
import com.ywj.gjwl.domain.User;
import com.ywj.gjwl.service.DeptService;
import com.ywj.gjwl.service.RoleService;
import com.ywj.gjwl.service.UserService;
import com.ywj.gjwl.utils.Page;

/*
 * 部门管理的Action
 */
public class UserAction extends BaseAction implements ModelDriven<User> {

	/**
	 * 
	 */
	// 为什么这里的取值命名为model是有学问的，具体参考day02。08
	private User model = new User();

	public User getModel() {
		return model;
	}

	// 分页查询
	private Page page = new Page();

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	// 注入service
	private UserService userService;
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
	//要提取其它表的内容，就要注入其它的表对应的service，不要忘了spring中的配置
	private DeptService deptService;
	public void setDeptService(DeptService deptService) {
		this.deptService = deptService;
	}

	private RoleService roleService;

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	// ==================================

	/*
	 * 分页的功能
	 */


	public String list() {
		page = userService.findPage("from User", page, User.class, null);
		// 设置分页的url
		page.setUrl("userAction_list");

		// 将数据压入值栈
		super.push(page);
		return "list";
	}

	/*
	 * 查看
	 */
	public String toview() {
		// 1.调用业务方法，根据id，得到对象
		// 这种查看某项的具体内容的东西，都会从前台传送一个id号过来，这个id号也是可以被model直接接受的。
		User User = userService.get(User.class, model.getId());
		super.push(User);
		return "toview";
	}

	/*
	 * 进入新增页面
	 */
	public String tocreate() {
		
		List<Dept> deptList =deptService.find("from Dept where state=1", Dept.class, null);
		super.put("deptList", deptList);
		// 调用业务方法查询父部门集合
		List<User> userList = userService.find("from User where state=1", User.class, null);
		// 将结果压入值栈中
		super.put("userList", userList);
		return "tocreate";
	}
	

	/*
	 * //获取要插入的数据 <s:select name="parent.id" list="#UserList" headerKey=""
	 * headerValue="--请选择--" listKey="id" listValue="UserName"></s:select>
	 * model对象能接收： parent id UserName
	 */
	public String insert() {

		// 插入数据库,这个函数是可以做两件事情的，要么就是save要么就是update。
		// 当传入的数据没有oid的时候就是做save否则就是update
		userService.saveOrUpdate(model);

		// 返回新的页面，此时一般插入新的数据后一般再跳转到列表页面，
		// 重定向
		return "alist";
	}

	/*
	 * 跳转到更新数据的页面， 要从数据库中获取原本已存在的属性数据 在更新的页面中也是需要有更改父部门的下拉列表的，那么就是需要查找数据库中所有信息。
	 */
	public String toupdate() throws Exception {
		// 获取原本已经存在的User对象
		User user = userService.get(User.class, model.getId());
		// 将对象压入值栈
		super.push(user);

		// 获取所有部门名称，也就是那个列表
		List<Dept> deptList = deptService.find("from Dept where state=1", Dept.class, null);
		//deptList.remove(user);
		super.put("deptList", deptList);

		// 跳转到更新页面
		return "toupdate";
	}

	/*
	 * 将更新的内容保存至数据库中 返回至列表页面
	 */
	public String update() {

		// 傻逼了，不能直接这么弄，因为原本年数据库里面是有值的，
		//但是直接更新的话，原来的值就没有了，得先把以前的值取出来。
		User obj = userService.get(User.class, model.getId());

		// 设置修改了的值
		obj.setDept(model.getDept());
		obj.setUserName(model.getUserName());
        obj.setState(model.getState());
   
	
		// 将模型接收到的数据保存至数据库
		userService.saveOrUpdate(obj);

		// 返回页面
		return "alist";
	}
	
	/*
	 * 实现批量删除
	 * 同名复选框的的id取值有讲究
	 */
	
	public String delete() throws Exception {
		//接收id数组然后分割
		String[] ids=model.getId().split(", ");
		//调用业务层实现批量删除
		userService.delete(User.class, ids);
		
		return "alist";
	}

	/*
	 * 给用户添加角色
	 */
	public String torole() throws Exception {
		//查找到需要修改的用户
		User obj=userService.get(User.class, model.getId());
		
		//页面上需要回显内容的，将当前用户压入值栈
		//一个方法中，栈顶只有一次，但键值对可以有很多个
		super.push(obj);
		
		//获取角色列表
		List<Role> roleList=roleService.find("from Role ", Role.class, null);
		//将全部角色列表加入到值栈的context中
		super.put("roleList", roleList);
		
		//对象导航,
		//提取出角色名
		Set<Role> roleSet=obj.getRoles();
		StringBuilder sb=new StringBuilder();
		for(Role role:roleSet){
			sb.append(role.getName()).append(",");
		}
		
		//当前用户的角色字符串放入值栈中
		super.put("roleStr", sb.toString());
		
		return "torole";
	}
	
	/*
	 *  修改用户角色操作
	 */
	
	//用户对应的角色可能是多种的
	private String[] roleIds;
	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}

	public String role() throws Exception {
		//1.查找到需要修改的用户
		User obj=userService.get(User.class, model.getId());
		
		//2.有哪些角色？遍历roleIds获取修改后的角色
		Set<Role> roles=new HashSet<Role>();
		for(String id:roleIds){
			Role role=roleService.get(Role.class, id);
			roles.add(role);
		}
		//3.设置用户与角色列表之间的关系
		//影响的不是user表也不是role表，而是他们的中间表
		obj.setRoles(roles);
		//4.保存到数据库中
		userService.saveOrUpdate(obj);
		//5.跳转页面
		return "alist";
	}


}
