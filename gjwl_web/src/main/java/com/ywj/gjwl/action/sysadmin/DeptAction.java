package com.ywj.gjwl.action.sysadmin;

import java.util.List;

import org.springframework.ui.Model;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.ywj.gjwl.action.BaseAction;
import com.ywj.gjwl.domain.Dept;
import com.ywj.gjwl.service.DeptService;
import com.ywj.gjwl.utils.Page;

/*
 * 部门管理的Action
 */
public class DeptAction extends BaseAction implements ModelDriven<Dept> {

	//为什么这里的取值命名为model是有学问的，具体参考day02。08
	private Dept model=new Dept();
	public Dept getModel() {		
		return model;
	}
	
	//分页查询
	private Page page=new Page();
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	//注入service
	private DeptService deptService;
	public void setDeptService(DeptService deptService) {
		this.deptService = deptService;
	}
	
	//==================================
	
	/*
	 * 分页的功能
	 */
	
	public String list(){
		page = deptService.findPage("from Dept", page, Dept.class, null);
		//设置分页的url
		page.setUrl("deptAction_list");
		
		//将数据压入值栈
		super.push(page);
		return "list";
	}
	
	/*
	 * 查看
	 */
	public String toview(){
		//1.调用业务方法，根据id，得到对象
		//这种查看某项的具体内容的东西，都会从前台传送一个id号过来，这个id号也是可以被model直接接受的。
		Dept dept=deptService.get(Dept.class, model.getId());
		super.push(dept);
		
		return "toview";
	}
	
	/*
	 * 进入新增页面
	 */
	public String tocreate(){
		
		//调用业务方法查询父部门集合
		List<Dept> deptsList=deptService.find("from Dept where state=1", Dept.class, null);
		//将结果压入值栈中
		ActionContext.getContext().put("deptList", deptsList);
		return "tocreate";
	}
	
	
	/*
	 * //获取要插入的数据
	 * <s:select name="parent.id" list="#deptList" headerKey=""
	 * 		 headerValue="--请选择--" listKey="id" listValue="deptName"></s:select>
	 * model对象能接收：
	 * 	parent
	 * 		id	
	 * 	deptName
	 */
	public String insert(){
		
		//插入数据库,这个函数是可以做两件事情的，要么就是save要么就是update。
		//当传入的数据没有oid的时候就是做save否则就是update
		deptService.saveOrUpdate(model);
		
		//返回新的页面，此时一般插入新的数据后一般再跳转到列表页面，
		//重定向
		return "alist";
	}
	
	/*
	 * 跳转到更新数据的页面，
	 * 要从数据库中获取原本已存在的属性数据
	 * 在更新的页面中也是需要有更改父部门的下拉列表的，那么就是需要查找数据库中所有信息。
	 */
	public String toupdate() throws Exception {
		//获取原本已经存在的Dept对象
		Dept dept=deptService.get(Dept.class, model.getId());
		//将对象压入值栈
		super.push(dept);
		
		//获取所有部门名称
		List<Dept> deptList=deptService.find("from Dept where state=1", Dept.class, null);
		super.put("deptList", deptList);
		
		//跳转到更新页面
		return "toupdate";
	}
	
	/*
	 * 将更新的内容保存至数据库中
	 * 返回至列表页面
	 */
	public String update(){
		
		//傻逼了，不能直接这么弄，因为原本年数据库里面是有值的，但是直接更新的话，原来的值就没有了，得先把以前的值取出来。
		Dept obj=deptService.get(Dept.class, model.getId());
		
		//设置修改了的值
		obj.setParent(model.getParent());
		obj.setDeptName(model.getDeptName());
		
		//将模型接收到的数据保存至数据库 
		deptService.saveOrUpdate(obj);
		
		//返回页面
		return "alist";
	}
}
