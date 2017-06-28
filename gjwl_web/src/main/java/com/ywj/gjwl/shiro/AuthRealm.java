package com.ywj.gjwl.shiro;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.ywj.gjwl.domain.Module;
import com.ywj.gjwl.domain.Role;
import com.ywj.gjwl.domain.User;
import com.ywj.gjwl.service.UserService;

public class AuthRealm extends AuthorizingRealm{
	private UserService userService;
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * 授权   当jsp页面出现Shiro标签时，就会执行授权方法
	 */
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		System.out.println("授权");
		//所需授权的用户
		User user = (User) pc.fromRealm(this.getName()).iterator().next();//根据realm的名字去找对应的realm
		
		Set<Role > roles = user.getRoles();//对象导航
		List<String> permissions = new ArrayList<String>();
		for(Role role :roles){
			//遍历每个角色 
			Set<Module> modules = role.getModules();//得到每个角色下的模块列表
			for(Module m :modules){
				permissions.add(m.getName());
			}
		}
		
		//构建授权信息
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addStringPermissions(permissions);//添加用户的模块（权限）
		return info;
	}

	
	/**
	 * 认证方法，先进入认证方法
	 * 认证方法 token代表用户在界面输入的用户名和密码
		我认为它的主要作用就是从数据库中提取值，用于后续密码比较
	 */
	
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("认证");
		
		//1.向下转型
		UsernamePasswordToken upToken  = (UsernamePasswordToken) token;
		
		//2.调用业务方法，实现根据用户名查询获取数据库中的用户信息用于后续比较
		String hql = "from User where userName=?";
		List<User> list = userService.find(hql, User.class, new String[]{upToken.getUsername()});
		if(list!=null && list.size()>0){
			User user = list.get(0);
			AuthenticationInfo info = new SimpleAuthenticationInfo(user,user.getPassword(),this.getName());
			return info;   //此处如果返回，就会立即进入到密码比较器CustomCredentialsMatcher
		}
		
		return null;//就会出现异常
	}
}
