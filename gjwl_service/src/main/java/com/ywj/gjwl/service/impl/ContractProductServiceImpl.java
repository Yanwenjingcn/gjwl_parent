package com.ywj.gjwl.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.ywj.gjwl.dao.BaseDao;
import com.ywj.gjwl.domain.Contract;
import com.ywj.gjwl.domain.ContractProduct;
import com.ywj.gjwl.domain.ExtCproduct;
import com.ywj.gjwl.service.ContractProductService;
import com.ywj.gjwl.service.ModuleService;

import com.ywj.gjwl.utils.Page;
import com.ywj.gjwl.utils.UtilFuns;

public class ContractProductServiceImpl implements ContractProductService {

	private BaseDao baseDao;
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	// ====================================
	public List<ContractProduct> find(String hql, Class<ContractProduct> entityClass, Object[] params) {
		return baseDao.find(hql, entityClass, params);
	}

	public ContractProduct get(Class<ContractProduct> entityClass, Serializable id) {
		return baseDao.get(entityClass, id);
	}

	public Page<ContractProduct> findPage(String hql, Page<ContractProduct> page, Class<ContractProduct> entityClass, Object[] params) {
		return baseDao.findPage(hql, page, entityClass, params);
	}

	
	
	public void saveOrUpdate(ContractProduct entity) {
		Double amount=0d;
		//
		if(UtilFuns.isEmpty(entity.getId())){
			
			if(UtilFuns.isNotEmpty(entity.getPrice())&&UtilFuns.isNotEmpty(entity.getCnumber())){		
				amount=entity.getPrice()*entity.getCnumber();//货物总金额
				//冗余字段
				entity.setAmount(amount);
			}
			
			Contract contract=baseDao.get(Contract.class, entity.getContract().getId());
			contract.setTotalAmount(contract.getTotalAmount()+amount);
			
			//保存购销合同的总金额
			baseDao.saveOrUpdate(contract);
		}else{
			//去除货物的原有的总金额
			double oldAmount=entity.getAmount();
			if(UtilFuns.isNotEmpty(entity.getPrice())&&UtilFuns.isNotEmpty(entity.getCnumber())){		
				amount=entity.getPrice()*entity.getCnumber();//货物总金额
				//冗余字段
				entity.setAmount(amount);
			}
			
			Contract contract=baseDao.get(Contract.class, entity.getContract().getId());
			contract.setTotalAmount(contract.getTotalAmount()-oldAmount+amount);
			baseDao.saveOrUpdate(contract);
		}
		
		baseDao.saveOrUpdate(entity);

	}

	public void saveOrUpdateAll(Collection<ContractProduct> entitys) {
		baseDao.saveOrUpdateAll(entitys);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ywj.gjwl.service.UserService#deleteById(java.lang.Class, java.io.Serializable)
	 * 这个函数是用于对部门表进行删除的，那么在删除一个部门时，它有可能是个父部门，那么就得同时删除它的子部门
	 * 	删除子部门时，这个子部门也有可能是个父部门，还得继续递归删除
	 * 
	 */
	public void deleteById(Class<ContractProduct> entityClass, Serializable id) {
		
		baseDao.deleteById(entityClass, id);
	}

	public void delete(Class<ContractProduct> entityClass, Serializable[] ids) {
		//调用自身的deleteById方法删除数据
		for(Serializable id:ids){
			this.deleteById(ContractProduct.class, id);
		}
		//baseDao.delete(entityClass, ids);
	}

	/**
	 * 删除某购销合同下的货物，主要是操作金额
	 */
	public void delete(Class<ContractProduct> entityClass, ContractProduct model) {
		//1.获取货物对象
		ContractProduct cp=baseDao.get(ContractProduct.class, model.getId());
		
		//2.获取货物下附件内容
		Set<ExtCproduct> extcSet=cp.getExtCproducts();
		
		//3.获取购销合同。删除时依旧要修改购销合同的总金额
		Contract contract=baseDao.get(Contract.class, model.getContract().getId());
		
		//4.修改总金额，减去附件的金额
		for(ExtCproduct ext:extcSet){
			contract.setTotalAmount(contract.getTotalAmount()-ext.getAmount());
		}
		
		//5.修改总金额。减去货物的金额
		contract.setTotalAmount(contract.getTotalAmount()-cp.getAmount());
		
		//6.跟新购销合同的总金额
		baseDao.saveOrUpdate(contract);
		
		//7.删除货物对象 级联删除附件
		baseDao.deleteById(ContractProduct.class, model.getId());
	}

}
