package com.ywj.gjwl.action.sysadmin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.ui.Model;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.ywj.gjwl.action.BaseAction;
import com.ywj.gjwl.domain.Dept;
import com.ywj.gjwl.domain.Module;
import com.ywj.gjwl.domain.Role;
import com.ywj.gjwl.domain.User;
import com.ywj.gjwl.exception.SysException;
import com.ywj.gjwl.service.DeptService;
import com.ywj.gjwl.service.ModuleService;
import com.ywj.gjwl.service.RoleService;
import com.ywj.gjwl.utils.Page;

/**
 * 
 * @ClassName: RoleAction
 * @Description: 角色管理Action
 * @author YWJ
 * @date 2017年6月28日 下午4:53:57
 */
public class RoleAction extends BaseAction implements ModelDriven<Role> {

	private Role model = new Role();

	public Role getModel() {
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

	// 注入角色service
	private RoleService roleService;

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	// 注入模块service
	private ModuleService moduleService;
	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}
	// ===============================================================

	/*
	 * 分页的功能
	 */
	public String list() {
		page = roleService.findPage("from Role", page, Role.class, null);
		// 设置分页的url
		page.setUrl("roleAction_list");

		// 将数据压入值栈
		super.push(page);
		return "list";
	}

	/*
	 * 查看
	 */
	public String toview() throws Exception {
		try {
			Role role = roleService.get(Role.class, model.getId());
			super.push(role);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SysException("请先勾选内容");
		}
		return "toview";
	}

	/*
	 * 进入新增页面
	 */
	public String tocreate() {
		return "tocreate";
	}

	public String insert() {

		roleService.saveOrUpdate(model);

		return "alist";
	}

	/*
	 * 跳转到更新数据的页面， 要从数据库中获取原本已存在的属性数据 在更新的页面中也是需要有更改父部门的下拉列表的，那么就是需要查找数据库中所有信息。
	 */
	public String toupdate() throws Exception {
		// 获取原本已经存在的Role对象
		Role role = roleService.get(Role.class, model.getId());
		// 将对象压入值栈
		super.push(role);
		// 跳转到更新页面
		return "toupdate";
	}

	/*
	 * 将更新的内容保存至数据库中 返回至列表页面
	 */
	public String update() {


		Role obj = roleService.get(Role.class, model.getId());
		// 设置修改了的值
		obj.setName(model.getName());
		obj.setRemark(model.getRemark());

		// 将模型接收到的数据保存至数据库
		roleService.saveOrUpdate(obj);
		// 返回页面
		return "alist";
	}

	/*
	 * 实现批量删除 同名复选框的的id取值有讲究
	 */

	public String delete() throws Exception {
		// 接收id数组然后分割
		String[] ids = model.getId().split(", ");
		// 调用业务层实现批量删除
		roleService.delete(Role.class, ids);
		return "alist";
	}

	
	/*
	 * 权限管理
	 */
	public String tomodule() throws Exception {
		// 1.根据角色id，得到角色的对象
		Role obj = roleService.get(Role.class, model.getId());
		// 2.将结果压入值栈
		super.push(obj);

		return "tomodule";
	}

	/**
	 * 为了使用 zTree树，就要组织好zTree树所使用的json数据 json数据结构如下：
	 * [{"id":"模块的id","pId":"父模块id","name":"模块名","checked":"true|false"},{"id":
	 * "模块的id","pId":"父模块id","name":"模块名","checked":"true|false"}]
	 * 
	 * 常用的json插件有哪些？ json-lib fastjson struts-json-plugin-xxx.jar,手动拼接
	 * 
	 * 如何输出? 借助于response对象输出数据
	 */

	public String roleModuleJsonStr() throws Exception {
		// 1.获取当前用户的角色信息
		Role role = roleService.get(Role.class, model.getId());
		// 2.通过对象导航的方式加载出当前的模块列表
		Set<Module> moduleSet = role.getModules();

		// 3.加载出所有模块列表
		List<Module> moduleList = moduleService.find("from Module", Module.class, null);
		int size = moduleList.size();
		// 4.组织json串
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (Module module : moduleList) {
			size--;
			sb.append("{\"id\":\"").append(module.getId());
			sb.append("\",\"pId\":\"").append(module.getParentId());
			sb.append("\",\"name\":\"").append(module.getName());
			sb.append("\",\"checked\":\"");
			if (moduleSet.contains(module)) {
				sb.append("true");
			} else {
				sb.append("false");
			}
			sb.append("\"}");
			if (size > 0)
				sb.append(",");
		}
		sb.append("]");

		// 5.得到response对象
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-control", "no-cache");
		
		// 6.使用 response对象输出json串
		response.getWriter().write(sb.toString());
		
		// 7.返回NONE
		return NONE;
	}

	/*
	 * 
	 */
	private String moduleIds;

	public void setModuleIds(String moduleIds) {
		this.moduleIds = moduleIds;
	}

	public String module() throws Exception {
		// 1.哪个角色?
		Role role = roleService.get(Role.class, model.getId());

		// 2.选中的模块有什么
		String[] ids = moduleIds.split(",");

		// 加载出这些模块列表
		Set<Module> moduleSet = new HashSet<Module>();
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				// 添加选中的模块在模块列表中
				Module mo = moduleService.get(Module.class, id);
				moduleSet.add(mo);
			}
		}

		// 实现为角色分配新的模块
		role.setModules(moduleSet);

		// 保存结果
		roleService.saveOrUpdate(role);

		// 跳转页面
		return "alist";
	}

}
