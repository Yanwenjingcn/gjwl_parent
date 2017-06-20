package com.ywj.gjwl.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.ywj.gjwl.dao.BaseDao;
import com.ywj.gjwl.domain.Role;
import com.ywj.gjwl.service.RoleService;
import com.ywj.gjwl.utils.Page;
import com.ywj.gjwl.utils.UtilFuns;

public class RoleServiceImpl implements RoleService {

	private BaseDao baseDao;
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	// ====================================
	public List<Role> find(String hql, Class<Role> entityClass, Object[] params) {
		return baseDao.find(hql, entityClass, params);
	}

	public Role get(Class<Role> entityClass, Serializable id) {
		return baseDao.get(entityClass, id);
	}

	public Page<Role> findPage(String hql, Page<Role> page, Class<Role> entityClass, Object[] params) {
		return baseDao.findPage(hql, page, entityClass, params);
	}

	
	//service的内容不是一成不变的，也是会随着我们的业务需求而不断变化，
	//
	public void saveOrUpdate(Role entity) {
		//用户表和用户扩展表的威一对一关系，我们让他们的id号都相同
		if(UtilFuns.isEmpty(entity.getId())){		
		}
		baseDao.saveOrUpdate(entity);

	}

	public void saveOrUpdateAll(Collection<Role> entitys) {
		baseDao.saveOrUpdateAll(entitys);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ywj.gjwl.service.UserService#deleteById(java.lang.Class, java.io.Serializable)
	 * 这个函数是用于对部门表进行删除的，那么在删除一个部门时，它有可能是个父部门，那么就得同时删除它的子部门
	 * 	删除子部门时，这个子部门也有可能是个父部门，还得继续递归删除
	 * 
	 */
	public void deleteById(Class<Role> entityClass, Serializable id) {
		
		baseDao.deleteById(entityClass, id);
	}

	public void delete(Class<Role> entityClass, Serializable[] ids) {
		//调用自身的deleteById方法删除数据
		for(Serializable id:ids){
			this.deleteById(Role.class, id);
		}
		//baseDao.delete(entityClass, ids);
	}

}
