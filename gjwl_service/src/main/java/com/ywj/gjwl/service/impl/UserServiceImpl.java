package com.ywj.gjwl.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.sun.istack.FinalArrayList;
import com.ywj.gjwl.dao.BaseDao;
import com.ywj.gjwl.domain.User;
import com.ywj.gjwl.service.UserService;
import com.ywj.gjwl.utils.Encrypt;
import com.ywj.gjwl.utils.MailUtil;
import com.ywj.gjwl.utils.Page;
import com.ywj.gjwl.utils.SysConstant;
import com.ywj.gjwl.utils.UtilFuns;

public class UserServiceImpl implements UserService {

	private BaseDao baseDao;
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	private SimpleMailMessage mailMessage;
	private JavaMailSender mailSender;
	public void setMailMessage(SimpleMailMessage mailMessage) {
		this.mailMessage = mailMessage;
	}
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	// ====================================
	public List<User> find(String hql, Class<User> entityClass, Object[] params) {
		return baseDao.find(hql, entityClass, params);
	}

	public User get(Class<User> entityClass, Serializable id) {
		return baseDao.get(entityClass, id);
	}

	public Page<User> findPage(String hql, Page<User> page, Class<User> entityClass, Object[] params) {
		return baseDao.findPage(hql, page, entityClass, params);
	}

	
/**
 * service的内容不是一成不变的，也是会随着我们的业务需求而不断变化，
 */
	public void saveOrUpdate(final User entity) {
		//用户表和用户扩展表的为一对一关系，我们让他们的id号都相同
		if(UtilFuns.isEmpty(entity.getId())){
			String id=UUID.randomUUID().toString();
			entity.setId(id);
			entity.getUserinfo().setId(id);
			entity.setPassword(Encrypt.md5(SysConstant.DEFAULT_PASS, entity.getUserName()));
			baseDao.saveOrUpdate(entity);
			
//			//再开启一个新的线程，完成邮件发送功能
//			Thread thread=new Thread(new Runnable() {
//				public void run() {
//					try {
//						MailUtil.sendMessage(entity.getUserinfo().getEmail(), "新员工入职账户通知", "欢迎您加入本集团，您的用户名"+entity.getUserName()+",初始密码是"+SysConstant.DEFAULT_PASS);
//					} catch (Exception e) {
//						
//						e.printStackTrace();		
//					}
//					
//				}
//			});
//			th.start();//开启线程，如果比较费时的工作都可以交给线程去做
			
			//用spring的邮件相关内容发送邮件
			//我此时还没有开启163的smtp服务
			
			Thread th = new Thread(new Runnable() {
				public void run() {
					try {
						mailMessage.setTo(entity.getUserinfo().getEmail());
						mailMessage.setSubject("新员工入职的系统账户通知");
						mailMessage.setText("欢迎您加入本集团，您的用户名:"+entity.getUserName()+",初始密码："+SysConstant.DEFAULT_PASS);
						mailSender.send(mailMessage);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			/**
			 * 开启线程
			 */
			th.start();
			
			
		}else {
			baseDao.saveOrUpdate(entity);
		}
		

	}

	public void saveOrUpdateAll(Collection<User> entitys) {
		baseDao.saveOrUpdateAll(entitys);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ywj.gjwl.service.UserService#deleteById(java.lang.Class, java.io.Serializable)
	 * 这个函数是用于对部门表进行删除的，那么在删除一个部门时，它有可能是个父部门，那么就得同时删除它的子部门
	 * 	删除子部门时，这个子部门也有可能是个父部门，还得继续递归删除
	 * 
	 */
	public void deleteById(Class<User> entityClass, Serializable id) {
		
		baseDao.deleteById(entityClass, id);
	}

	/**
	 * 批量删除用户？
	 */
	public void delete(Class<User> entityClass, Serializable[] ids) {
		//调用自身的deleteById方法删除数据
		for(Serializable id:ids){
			this.deleteById(User.class, id);
		}
		
	}

}
