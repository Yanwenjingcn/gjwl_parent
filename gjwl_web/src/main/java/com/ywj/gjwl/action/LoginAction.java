package com.ywj.gjwl.action;

import org.apache.shiro.SecurityUtils;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.ywj.gjwl.domain.User;
import com.ywj.gjwl.utils.SysConstant;
import com.ywj.gjwl.utils.UtilFuns;
/**
 * 
* @ClassName: LoginAction 
* @Description: TODO
* @author YWJ
* @date 2017年6月28日 下午4:30:02
 */
public class LoginAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;



	//SSH传统登录方式
	public String login() throws Exception {
		
//		if(true){
//			String msg = "登录错误，请重新填写用户名密码!";
//			this.addActionError(msg);
//			throw new Exception(msg);
//		}
//		User user = new User(username, password);
//		User login = userService.login(user);
//		if (login != null) {
//			ActionContext.getContext().getValueStack().push(user);
//			session.put(SysConstant.CURRENT_USER_INFO, login);	//记录session
//			return SUCCESS;
//		}
//		return "login";
		
		//如果没有输入用户名则回到原页面
		if(UtilFuns.isEmpty(username)){
			return "login";
		}
		
		//采用shiro安全框架
		try {
			//1.得到Subject
			Subject subject = SecurityUtils.getSubject();
			
			//2.调用登录方法
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			subject.login(token);//当这一代码执行时，就会自动跳入到AuthRealm中认证方法
			
			//3.登录成功时，就从Shiro中取出用户的登录信息
			User user = (User) subject.getPrincipal();
			
			//4.将用户放入session域中
			session.put(SysConstant.CURRENT_USER_INFO, user);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			request.put("errorInfo", "对不起，用户或密码错误！");
			return "login";
		}
		
		return SUCCESS;
	}
	
	
	//退出
	public String logout(){
		session.remove(SysConstant.CURRENT_USER_INFO);		//删除session
		
		return "logout";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

