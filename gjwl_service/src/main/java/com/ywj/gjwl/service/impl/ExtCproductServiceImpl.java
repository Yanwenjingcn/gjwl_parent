package com.ywj.gjwl.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import com.ywj.gjwl.dao.BaseDao;
import com.ywj.gjwl.domain.Contract;
import com.ywj.gjwl.domain.ExtCproduct;
import com.ywj.gjwl.service.ExtCproductService;
import com.ywj.gjwl.utils.Page;
import com.ywj.gjwl.utils.UtilFuns;

public class ExtCproductServiceImpl implements ExtCproductService {

	private BaseDao baseDao;

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	// ====================================
	public List<ExtCproduct> find(String hql, Class<ExtCproduct> entityClass, Object[] params) {
		return baseDao.find(hql, entityClass, params);
	}

	public ExtCproduct get(Class<ExtCproduct> entityClass, Serializable id) {
		return baseDao.get(entityClass, id);
	}

	public Page<ExtCproduct> findPage(String hql, Page<ExtCproduct> page, Class<ExtCproduct> entityClass,
			Object[] params) {
		return baseDao.findPage(hql, page, entityClass, params);
	}

	public void saveOrUpdate(ExtCproduct entity) {
		// 新增操作
		Double amount = 0d;
		if (UtilFuns.isEmpty(entity.getId())) {

			if (UtilFuns.isNotEmpty(entity.getPrice()) && UtilFuns.isNotEmpty(entity.getCnumber())) {
				amount = entity.getPrice() * entity.getCnumber();
				entity.setAmount(amount);
			}

			// 这个是附件的金额。不需要加在货品中
			Contract contract = baseDao.get(Contract.class, entity.getContractProduct().getContract().getId());
			contract.setTotalAmount(contract.getTotalAmount() + amount);
			baseDao.saveOrUpdate(contract);
		} else {// 修改操作
			Double oldAmount = entity.getAmount();
			if (UtilFuns.isNotEmpty(entity.getPrice()) && UtilFuns.isNotEmpty(entity.getCnumber())) {
				amount = entity.getPrice() * entity.getCnumber();
				entity.setAmount(amount);
			}
			// 这个是附件的金额。不需要加在货品中
			Contract contract = baseDao.get(Contract.class, entity.getContractProduct().getContract().getId());
			contract.setTotalAmount(contract.getTotalAmount() - oldAmount + amount);
			baseDao.saveOrUpdate(contract);
		}
		baseDao.saveOrUpdate(entity);
	}

	public void saveOrUpdateAll(Collection<ExtCproduct> entitys) {
		baseDao.saveOrUpdateAll(entitys);
	}


	public void deleteById(Class<ExtCproduct> entityClass, Serializable id) {

		baseDao.deleteById(entityClass, id);
	}

	public void delete(Class<ExtCproduct> entityClass, Serializable[] ids) {
		// 调用自身的deleteById方法删除数据
		for (Serializable id : ids) {
			this.deleteById(ExtCproduct.class, id);
		}
		
	}

	/**
	 * 删除附件时也要操作购销合同的 金额
	 */
	public void delete(Class<ExtCproduct> entityClass, ExtCproduct model) {
		ExtCproduct extCproduct=baseDao.get(ExtCproduct.class, model.getId());
		
		Contract contract=baseDao.get(Contract.class, model.getContractProduct().getContract().getId());
		
		contract.setTotalAmount(contract.getTotalAmount()-extCproduct.getAmount());
		baseDao.saveOrUpdate(contract);
		baseDao.deleteById(ExtCproduct.class, model.getId());
		
	}

}
