package com.ywj.gjwl.cargo;

import java.util.HashSet;
import java.util.Set;

import com.opensymphony.xwork2.ModelDriven;
import com.ywj.gjwl.action.BaseAction;
import com.ywj.gjwl.domain.Contract;
import com.ywj.gjwl.domain.Export;
import com.ywj.gjwl.domain.ExportProduct;
import com.ywj.gjwl.domain.User;
import com.ywj.gjwl.service.ContractService;
import com.ywj.gjwl.service.ExportProductService;
import com.ywj.gjwl.service.ExportService;
import com.ywj.gjwl.utils.Page;
import com.ywj.gjwl.utils.SysConstant;
import com.ywj.gjwl.utils.UtilFuns;
/**
 * 
* @ClassName: ExportAction 
* @Description: TODO
* @author YWJ
* @date 2017年7月2日 下午4:49:36
 */
public class ExportAction extends BaseAction implements ModelDriven<Export> {


	/** 
	* @Fields serialVersionUID : TODO
	*/ 
	private static final long serialVersionUID = 1L;
	private Export model = new Export();

	public Export getModel() {
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
	private ExportService exportService;
	public void setExportService(ExportService exportService) {
		this.exportService = exportService;
	}

	private ContractService contractService;
	public void setContractService(ContractService contractService) {
		this.contractService = contractService;
	}

	private ExportProductService exportProductService;
	public void setExportProductService(ExportProductService exportProductService) {
		this.exportProductService = exportProductService;
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
		
		String hql="from Export where 1=1";
//		//获取当前用户，在baseaction中抽象出来的一个函数
//		User user=super.getCurUser();
//		//确定当前用户等级并给出他能管理的细粒度数据
//		int degree=user.getUserinfo().getDegree();
//		
//		if(degree==4){
//			//说明是员工
//			hql+="and createBy='"+user.getId()+"'";
//		}else if (degree==3) {
//			//说明是部门经理，管理本部门的.可以查看本部门所有员工创建的合同内容
//			hql+="and createDept='"+user.getDept().getId()+"'";
//		}else if (degree==2) {
//			//可管理本部门及下属部门
//			
//		}else if (degree==1) {
//			//副总，有的部门内容无权查看
//			
//		}else if (degree==0) {
//			//总经理，可以查看所有记录
//			
//		}
		
		page = exportService.findPage(hql, page, Export.class, null);
		// 设置分页的url
		page.setUrl("exportAction_list");

		// 将数据压入值栈
		super.push(page);
		return "list";
	}

	
	/**
	 * 
	* @Title: contractList 
	* @Description: 查询状态为1的购销合同  
	* @return String    
	* @throws
	 */
	public String contractList() throws Exception {
		String hql="from Contract where state=1";
		page=contractService.findPage(hql, page, Contract.class, null);
		page.setUrl("exportAction_contractList");
		super.push(page);
		return "contractList";
	}
	/*
	 * 查看
	 */
	public String toview() {
		
		Export role = exportService.get(Export.class, model.getId());
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
 * @throws Exception 
 * 
* @Title: insert 
* @Description: 添加新的购销合同   
* @return String    
* @throws
 */
	public String insert() throws Exception {
		
		//细粒度控制，不同用户能看到的合同数据不一样
		User user=(User) session.get(SysConstant.CURRENT_USER_INFO);
		//通过区分合同创建者的id和创建者所属于的部门来抉择
		model.setCreateBy(user.getId());
		model.setCreateDept(user.getDept().getId());
		
		
		
		exportService.saveOrUpdate(model);

		return contractList();
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
	
		Export obj = exportService.get(Export.class, model.getId());
		
		// 将对象压入值栈
		super.push(obj);
		
		

		//3.addTRRecord("mRecordTable", "id", "productNo", "cnumber", "grossWeight", "netWeight", "sizeLength", "sizeWidth", "sizeHeight", "exPrice", "tax");
		StringBuilder sb = new StringBuilder();
		Set<ExportProduct> epSet = obj.getExportProducts();//关联级别的数据检索
		//遍历集合
		for(ExportProduct ep :epSet){
			sb.append("addTRRecord(\"mRecordTable\", \"").append(ep.getId());
			sb.append("\", \"").append(ep.getProductNo());
			sb.append("\", \"").append(UtilFuns.convertNull(ep.getCnumber()));
			sb.append("\", \"").append(UtilFuns.convertNull(ep.getGrossWeight()));
			sb.append("\", \"").append(UtilFuns.convertNull(ep.getNetWeight()));
			sb.append("\", \"").append(UtilFuns.convertNull(ep.getSizeLength()));
			sb.append("\", \"").append(UtilFuns.convertNull(ep.getSizeWidth()));
			sb.append("\", \"").append(UtilFuns.convertNull(ep.getSizeHeight()));
			sb.append("\", \"").append(UtilFuns.convertNull(ep.getExPrice()));
			sb.append("\", \"").append(UtilFuns.convertNull(ep.getTax())).append("\");");
		}
		
		 //4.将接好的串放入值栈中
		super.put("mRecordData", sb.toString());
		
		// 跳转到更新页面
		return "toupdate";
	}

	/*
	 * 将更新的内容保存至数据库中 返回至列表页面
	 */
	public String update() {
		//待修改的值
		Export obj = exportService.get(Export.class, model.getId());

		//2.设置出口把运单的属性
		obj.setInputDate(model.getInputDate());
		
        
        obj.setLcno(model.getLcno());
        obj.setConsignee(model.getConsignee());
        obj.setShipmentPort(model.getShipmentPort());
        obj.setDestinationPort(model.getDestinationPort());
        obj.setTransportMode(model.getTransportMode());
        obj.setPriceCondition(model.getPriceCondition());
        obj.setMarks(model.getMarks());//唛头是指具有一定格式的备注信息
        obj.setRemark(model.getRemark());
       
        Set<ExportProduct> epSet =new HashSet<ExportProduct>();//出口报运单下的商品列表
      
        //修改保存前台修改值后的商品
        for(int i=0;i<mr_id.length;i++){
        	//遍历数组，得到每个商品对象
        	ExportProduct ep = exportProductService.get(ExportProduct.class, mr_id[i]);
        	
        	if("1".equals(mr_changed[i])){
        		ep.setCnumber(mr_cnumber[i]);
        		ep.setGrossWeight(mr_grossWeight[i]);
        		ep.setNetWeight(mr_netWeight[i]);
        		ep.setSizeLength(mr_sizeLength[i]);
        		ep.setSizeWidth(mr_sizeWidth[i]);
        		ep.setSizeHeight(mr_sizeHeight[i]);
        		ep.setExPrice(mr_exPrice[i]);
        		ep.setTax(mr_tax[i]);
        		
        	}
        	
        	//即使前台没有修改报运货物信息，但是也是要加入到set集合中的
        	epSet.add(ep);
        }
        
        //设置报运单与商品列表的关系 ，报运单中添加修改后的新的商品列表内容，这个是需要维护的
        //这个是因为需要做级联，这样就可以直接保存新的报运商品的信息
        obj.setExportProducts(epSet);
        
		exportService.saveOrUpdate(obj);
		return "alist";
	}
	private String mr_changed[];
	private String mr_id[];
	private Integer mr_cnumber[];
	private Double mr_grossWeight[];
	private Double mr_netWeight[];
	private Double mr_sizeLength[];
	private Double mr_sizeWidth[];
	private Double mr_sizeHeight[];
	private Double mr_exPrice[];
	private Double mr_tax[];

	
	
	public void setMr_changed(String[] mr_changed) {
		this.mr_changed = mr_changed;
	}
	public void setMr_id(String[] mr_id) {
		this.mr_id = mr_id;
	}
	public void setMr_cnumber(Integer[] mr_cnumber) {
		this.mr_cnumber = mr_cnumber;
	}
	public void setMr_grossWeight(Double[] mr_grossWeight) {
		this.mr_grossWeight = mr_grossWeight;
	}
	public void setMr_netWeight(Double[] mr_netWeight) {
		this.mr_netWeight = mr_netWeight;
	}
	public void setMr_sizeLength(Double[] mr_sizeLength) {
		this.mr_sizeLength = mr_sizeLength;
	}
	public void setMr_sizeWidth(Double[] mr_sizeWidth) {
		this.mr_sizeWidth = mr_sizeWidth;
	}
	public void setMr_sizeHeight(Double[] mr_sizeHeight) {
		this.mr_sizeHeight = mr_sizeHeight;
	}
	public void setMr_exPrice(Double[] mr_exPrice) {
		this.mr_exPrice = mr_exPrice;
	}
	public void setMr_tax(Double[] mr_tax) {
		this.mr_tax = mr_tax;
	}
	
	/*
	 * 实现批量删除 同名复选框的的id取值有讲究
	 */
	public String delete() throws Exception {
		// 接收id数组然后分割
		String[] ids = model.getId().split(", ");
		
		// 调用业务层实现批量删除
		exportService.delete(Export.class, ids);
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
		
		exportService.changeState(ids, 1);
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
		exportService.changeState(ids, 0);
		return "alist";
	}
}
