package com.ywj.gjwl.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.ywj.gjwl.dao.BaseDao;
import com.ywj.gjwl.domain.Dept;
import com.ywj.gjwl.service.DeptService;
import com.ywj.gjwl.utils.Page;
import com.ywj.gjwl.utils.UtilFuns;

public class DeptServiceImpl implements DeptService {

	private BaseDao baseDao;
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	// ====================================
	public List<Dept> find(String hql, Class<Dept> entityClass, Object[] params) {
		return baseDao.find(hql, entityClass, params);
	}

	public Dept get(Class<Dept> entityClass, Serializable id) {
		return baseDao.get(entityClass, id);
	}

	public Page<Dept> findPage(String hql, Page<Dept> page, Class<Dept> entityClass, Object[] params) {
		return baseDao.findPage(hql, page, entityClass, params);
	}

	
	//service的内容不是一成不变的，也是会随着我们的业务需求而不断变化，
	//
	public void saveOrUpdate(Dept entity) {
		//如果没有id，那么使用的是save方法，为其它没有添加的属性赋予初始值
		if(UtilFuns.isEmpty(entity.getId())){
			//0是停用，1是启用，默认为启用
			entity.setState(1);//0是停用，1是启用，默认为启用
		}
		baseDao.saveOrUpdate(entity);

	}

	public void saveOrUpdateAll(Collection<Dept> entitys) {
		baseDao.saveOrUpdateAll(entitys);
	}

	public void deleteById(Class<Dept> entityClass, Serializable id) {
		baseDao.deleteById(entityClass, id);
	}

	public void delete(Class<Dept> entityClass, Serializable[] ids) {
		baseDao.delete(entityClass, ids);
	}

}
