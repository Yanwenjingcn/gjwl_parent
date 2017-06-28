package com.ywj.gjwl.cargo;

import com.opensymphony.xwork2.ModelDriven;
import com.ywj.gjwl.action.BaseAction;
import com.ywj.gjwl.domain.Contract;


import com.ywj.gjwl.service.ContractService;
import com.ywj.gjwl.utils.Page;
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


	// ==================================

	public String list() {
		page = contractService.findPage("from Contract", page, Contract.class, null);
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
		
		contractService.saveOrUpdate(model);

		return "alist";
	}

	/*
	 * 跳转到更新数据的页面， 要从数据库中获取原本已存在的属性数据 在更新的页面中也是需要有更改父部门的下拉列表的，那么就是需要查找数据库中所有信息。
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
}
