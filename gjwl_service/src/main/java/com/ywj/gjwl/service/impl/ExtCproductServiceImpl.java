package com.ywj.gjwl.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.ywj.gjwl.dao.BaseDao;
import com.ywj.gjwl.domain.ExtCproduct;
import com.ywj.gjwl.service.ExtCproductService;
import com.ywj.gjwl.service.ModuleService;

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

	public Page<ExtCproduct> findPage(String hql, Page<ExtCproduct> page, Class<ExtCproduct> entityClass, Object[] params) {
		return baseDao.findPage(hql, page, entityClass, params);
	}

	
	//service的内容不是一成不变的，也是会随着我们的业务需求而不断变化，
	//
	public void saveOrUpdate(ExtCproduct entity) {
		//用户表和用户扩展表的威一对一关系，我们让他们的id号都相同
		if(UtilFuns.isEmpty(entity.getId())){		
		}
		baseDao.saveOrUpdate(entity);

	}

	public void saveOrUpdateAll(Collection<ExtCproduct> entitys) {
		baseDao.saveOrUpdateAll(entitys);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ywj.gjwl.service.UserService#deleteById(java.lang.Class, java.io.Serializable)
	 * 这个函数是用于对部门表进行删除的，那么在删除一个部门时，它有可能是个父部门，那么就得同时删除它的子部门
	 * 	删除子部门时，这个子部门也有可能是个父部门，还得继续递归删除
	 * 
	 */
	public void deleteById(Class<ExtCproduct> entityClass, Serializable id) {
		
		baseDao.deleteById(entityClass, id);
	}

	public void delete(Class<ExtCproduct> entityClass, Serializable[] ids) {
		//调用自身的deleteById方法删除数据
		for(Serializable id:ids){
			this.deleteById(ExtCproduct.class, id);
		}
		//baseDao.delete(entityClass, ids);
	}

}