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

	/*
	 * (non-Javadoc)
	 * @see com.ywj.gjwl.service.DeptService#deleteById(java.lang.Class, java.io.Serializable)
	 * 这个函数是用于对部门表进行删除的，那么在删除一个部门时，它有可能是个父部门，那么就得同时删除它的子部门
	 * 	删除子部门时，这个子部门也有可能是个父部门，还得继续递归删除
	 * 
	 */
	public void deleteById(Class<Dept> entityClass, Serializable id) {
		//查询出该部门名下的所有子部门
		String hql="from Dept where parent.id=?";
		//获取所有子部门列表
		List<Dept> list=baseDao.find(hql, Dept.class, new Object[]{id});
		//递归调用删除子部门
		if(list!=null&&list.size()>0){
			for(Dept dept:list){
				deleteById(Dept.class, dept.getId());
			}
		}
		baseDao.deleteById(entityClass, id);
	}

	public void delete(Class<Dept> entityClass, Serializable[] ids) {
		//调用自身的deleteById方法删除数据
		for(Serializable id:ids){
			this.deleteById(Dept.class, id);
		}
		
		//baseDao.delete(entityClass, ids);
	}

}
