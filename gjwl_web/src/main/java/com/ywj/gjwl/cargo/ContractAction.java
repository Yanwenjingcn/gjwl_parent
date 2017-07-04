package com.ywj.gjwl.cargo;

import javax.jws.soap.SOAPBinding.Use;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ModelDriven;
import com.ywj.gjwl.action.BaseAction;
import com.ywj.gjwl.domain.Contract;
import com.ywj.gjwl.domain.User;
import com.ywj.gjwl.service.ContractService;
import com.ywj.gjwl.utils.Page;
import com.ywj.gjwl.utils.SysConstant;
/**
 * 
* @ClassName: ContractAction 
* @Description: 购销合同的action
* @author YWJ
* @date 2017年6月28日 下午7:33:18
 */
public class ContractAction extends BaseAction implements ModelDriven<Contract> {

	private static final long serialVersionUID = 1L;

	private Contract model = new Contract();

	public Contract getModel() {
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

	// 注入购销合同的service
	private ContractService contractService;
	public void setContractService(ContractService contractService) {
		this.contractService = contractService;
	}


	// ===========================================================

	/**
	 * 
	* @Title: list 
	* @Description: 合同的分页展示，提供细粒度的查询结果
	* @param @return    
	* @return String    
	* @throws
	 */
	public String list() {
		
		String hql="from Contract where 1=1";
		//获取当前用户，在baseaction中抽象出来的一个函数
		User user=super.getCurUser();
		//确定当前用户等级并给出他能管理的细粒度数据
		int degree=user.getUserinfo().getDegree();
		
		if(degree==4){
			//说明是员工
			hql+="and createBy='"+user.getId()+"'";
		}else if (degree==3) {
			//说明是部门经理，管理本部门的.可以查看本部门所有员工创建的合同内容
			hql+="and createDept='"+user.getDept().getId()+"'";
		}else if (degree==2) {
			//可管理本部门及下属部门
			
		}else if (degree==1) {
			//副总，有的部门内容无权查看
			
		}else if (degree==0) {
			//总经理，可以查看所有记录
			
		}
		
		contractService.findPage(hql, page, Contract.class, null);
		System.out.println(page.getResults().size());
		// 设置分页的url
		page.setUrl("contractAction_list");

		// 将数据压入值栈
		super.push(page);
		
		return "list";
	}

	/*
	 * 查看
	 */
	public String toview() {
		
		Contract role = contractService.get(Contract.class, model.getId());
		super.push(role);
		return "toview";
	}

	/*
	 * 进入新增页面
	 */
	public String tocreate() {
		return "tocreate";
	}

/**
 * 
* @Title: insert 
* @Description: 插入新的购销合同
* @param @return    
* @return String    
* @throws
 */
	public String insert() {
		
		//细粒度控制，不同用户能看到的合同数据不一样
		User user=(User) session.get(SysConstant.CURRENT_USER_INFO);
		//通过区分合同创建者的id和创建者所属于的部门来抉择
		model.setCreateBy(user.getId());
		model.setCreateDept(user.getDept().getId());
		
		contractService.saveOrUpdate(model);

		return "alist";
	}
	

	/**
	 * 
	* @Title: toupdate 
	* @Description: 跳转到更新数据的页面， 要从数据库中获取原本已存在的属性数据 在更新的页面中也是需要有更改父部门的下拉列表的，那么就是需要查找数据库中所有信息。
	* @param @return
	* @param @throws Exception    
	* @return String    
	* @throws
	 */
	public String toupdate() throws Exception {
	
		Contract role = contractService.get(Contract.class, model.getId());
		
		// 将对象压入值栈
		super.push(role);
		
		// 跳转到更新页面
		return "toupdate";
	}

	/*
	 * 将更新的内容保存至数据库中 返回至列表页面
	 */
	public String update() {
		//待修改的值
		Contract obj = contractService.get(Contract.class, model.getId());
		
		// 设置修改了的值
		obj.setCustomName(model.getCustomName());
		obj.setPrintStyle(model.getPrintStyle());
		obj.setContractNo(model.getContractNo());
		obj.setOfferor(model.getOfferor());
		obj.setInputBy(model.getInputBy());
		obj.setCheckBy(model.getCheckBy());
		obj.setInspector(model.getInspector());
		obj.setSigningDate(model.getSigningDate());
		obj.setImportNum(model.getImportNum());
		obj.setShipTime(model.getShipTime());
		obj.setTradeTerms(model.getTradeTerms());
		obj.setDeliveryPeriod(model.getDeliveryPeriod());
		obj.setCrequest(model.getCrequest());
		obj.setRemark(model.getRemark());

		// 将模型接收到的数据保存至数据库
		contractService.saveOrUpdate(obj);
		
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
		contractService.delete(Contract.class, ids);
		return "alist";
	}
	
	/**
	 * 
	* @Title: submit 
	* @Description:将某个合同提交。可以批量完成
	*	 具有同名框的一组值struts2如何封装？
	*提交就是改变购销合同的状态 将它由0改变为1，对应页面显示为“已提交”
	* @param @return
	* @param @throws Exception    
	* @return String    
	* @throws
	 */
	public String submit() throws Exception {
		String[] ids=model.getId().split(", ");
		
		contractService.changeState(ids, 1);
		return "alist";
	}
	
	/**
	 * 
	* @Title: cancel 
	* @Description: 取消合同的提交
	* 	也就是改变合同的状态 由1改变为0，对应页面显示为“草稿”
	* @param @return
	* @param @throws Exception    
	* @return String    
	* @throws
	 */
	public String cancel() throws Exception {
		String[] ids=model.getId().split(", ");
		contractService.changeState(ids, 0);
		return "alist";
	}
	
	
	
	/**
	 * 打印
	 */
	public String print() throws Exception {
		//1.根据购销合同的id,得到购销合同对象
		Contract contract = contractService.get(Contract.class, model.getId());
		
		//2.指定path
		String path = ServletActionContext.getServletContext().getRealPath("/");//应用程序的根路径
		
		//3.指定response
		HttpServletResponse response = ServletActionContext.getResponse();
		
		ContractPrint cp = new ContractPrint();
		cp.print(contract, path, response);
		
		return NONE;
	}
}
