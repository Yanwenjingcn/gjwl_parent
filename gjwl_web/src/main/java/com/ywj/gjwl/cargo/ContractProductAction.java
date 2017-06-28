package com.ywj.gjwl.cargo;

import java.util.List;

import com.opensymphony.xwork2.ModelDriven;
import com.ywj.gjwl.action.BaseAction;
import com.ywj.gjwl.domain.ContractProduct;
import com.ywj.gjwl.domain.Factory;
import com.ywj.gjwl.service.ContractProductService;
import com.ywj.gjwl.service.FactoryService;
import com.ywj.gjwl.utils.Page;



/**
 * 
* @ClassName: ContractProductAction 
* @Description: 购销合同下货品的添加
* @author YWJ
* @date 2017年6月28日 下午7:38:09
 */
public class ContractProductAction extends BaseAction implements ModelDriven<ContractProduct> {


	private ContractProduct model = new ContractProduct();

	public ContractProduct getModel() {
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

	// 注入货品的service
	private ContractProductService contractProductService;

	public void setContractProductService(ContractProductService contractProductService) {
		this.contractProductService = contractProductService;
	}

	// 注入工厂的service
	private FactoryService factoryService;

	public void setFactoryService(FactoryService factoryService) {
		this.factoryService = factoryService;
	}

	// ==========================================================================

	/**
	 * 进入货物新增页面
	 * 
	 * 新增后还是留在本页面，只是内容有所刷新
	 */
	public String tocreate() {
		// 1.查询出所有的生产厂家加入到下拉列表中
		String hql = "from Factory where ctype='货物' and state=1";
		List<Factory> factoryList = factoryService.find(hql, Factory.class, null);
		super.put("factoryList", factoryList);

		// 2.查询当前购销合同下的货物列表
		contractProductService.findPage("from ContractProduct where contract.id=?", page, ContractProduct.class,
				new String[] { model.getContract().getId() });
		page.setUrl("contractProductAction_tocreate");
		super.push(page);

		return "tocreate";
	}

	/*
	 * 给当前购销合同添加新的货物
	 */
	public String insert() {
		contractProductService.saveOrUpdate(model);
		
		// 这种方式的返回就是将方法看做类中的普通方法，这样返回String
		// com.ywj.gjwl.cargo.ContractProductAction.tocreate()
		return tocreate();
	}

	/*
	 * 跳转到更新数据的页面， 要从数据库中获取原本已存在的属性数据 在更新的页面中也是需要有更改父部门的下拉列表的，那么就是需要查找数据库中所有信息。
	 */
	public String toupdate() throws Exception {

		ContractProduct role = contractProductService.get(ContractProduct.class, model.getId());
		// 将对象压入值栈
		super.push(role);

		//查询出所有的生产厂家加入到下拉列表中
		String hql = "from Factory where ctype='货物' and state=1";
		List<Factory> factoryList = factoryService.find(hql, Factory.class, null);
		super.put("factoryList", factoryList);

		// 跳转到更新页面
		return "toupdate";
	}
	

	/**
	 * 将更新的内容保存至数据库中 返回至列表页面
	 */
	public String update() {

		// 但是直接更新的话，原来的值就没有了，得先把以前的值取出来。
		ContractProduct obj = contractProductService.get(ContractProduct.class, model.getId());

		// 设置修改了的值
		obj.setFactory(model.getFactory());
		obj.setFactoryName(model.getFactoryName());
		obj.setProductNo(model.getProductNo());
		obj.setProductImage(model.getProductImage());
		obj.setCnumber(model.getCnumber());
		obj.setAmount(model.getAmount());
		obj.setPackingUnit(model.getPackingUnit());
		obj.setLoadingRate(model.getLoadingRate());
		obj.setBoxNum(model.getBoxNum());
		obj.setPrice(model.getPrice());
		obj.setOrderNo(model.getOrderNo());
		obj.setProductDesc(model.getProductDesc());
		obj.setProductRequest(model.getProductRequest());

		// 将模型接收到的数据保存至数据库
		/**
		 * 对应主要是要操作合同的金额
		 */
		contractProductService.saveOrUpdate(obj);
		// 返回页面
		return tocreate();
	}

	
	/**
	 * 删除 <a href=
	 * "contractProductAction_delete.action?
	 * 	id=4028d3db5662dfb4015663208f470002
	 * 	&contract.id=4028d3db5662dfb4015662f0ecbc0001">
	 * [删除]</a> id指货物编号 contract.id：购销合同的id
	 * 
	 * 结论：如果操作子项时，最好同时传递他的所有祖宗
	 */
	public String delete() throws Exception {
		/**
		 * 对应主要是要操作合同的金额
		 */
		contractProductService.delete(ContractProduct.class, model);
		return tocreate();
	}

}
