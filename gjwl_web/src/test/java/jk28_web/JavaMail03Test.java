package jk28_web;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class JavaMail03Test {

	@Test
	public void testJavaMail() throws Exception{
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext-mail.xml");
		
		
		JavaMailSender sender = (JavaMailSender) ac.getBean("mailSender");       //得到邮件的发送对象，专门用于邮件发送
		
		//发送一个允许带图片，同时带附件的邮件
		MimeMessage message = sender.createMimeMessage();//创建一封允许带图片，同时带附件的邮件对象
		
	    //为了更好的操作MimeMessage对象，借用一个工具类来操作它
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		
		//通过工具类设置主题，内容，图片，附件
		//cid是值得contentID，内容编号
		helper.setFrom("itheima14@163.com");
		helper.setTo("3462420264@qq.com");
		helper.setSubject("这是来自百合网的一个请求");
		helper.setText("<html><head></head><body><h1>hello!!baby </h1>"
					+"<a href=http://www.itheima.com>黑马程序员</a>"	+ "<img src=cid:image/></body></html>",true);//第二个参数说明内容要解析为html代码
		
		//添加图片，spring提供出来的resource：FileSystemResource
		FileSystemResource resource = new FileSystemResource(new File("E:\\01分配权限原理分析.png"));
		//为cid为image的位置添加内容
		helper.addInline("image", resource);
		
		sender.send(message);
		
		/*JavaMailSenderImpl mailSender = (JavaMailSenderImpl) ac.getBean("mailSender");
		
		//3.创建一封允许带附件的邮件对象
		MimeMessage mimeMessage = mailSender.createMimeMessage();//创建出允许带附件的邮件对象
		
		
		//4.创建出一个用于操作MimeMessage的帮助类的对象
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		
		//5.设置邮件的相关内容 （发送者，拼接者，主题，内容 ）
		helper.setFrom("itheima14@163.com");
		helper.setTo("3462420264@qq.com");
		helper.setSubject("带图片和附件的邮件测试");
		helper.setText("<html><head></head><body><h1>hello!!spring image html mail</h1>"
		               +"<a href=http://www.itheima.com>黑马程序员</a>"	+ "<img src=cid:image/></body></html>", true);//cid:是固定的，后面的image是自己定义的
		
		//指定image所在的位置（是指本地电脑）
		FileSystemResource img = new FileSystemResource(new File("E:/01分配权限原理分析.png"));//将本地的图片转化成一个图片资源 
		helper.addInline("image", img);//image的参数来自上面cid的取值
		
		//发送时带附件
		FileSystemResource zipResource = new FileSystemResource(new File("E:/javamail1_4_4.zip"));
		helper.addAttachment("javamail1_4_4.zip", zipResource);*/
		
	    //发送邮件
		//mailSender.send(mimeMessage);
		
		
	}
}
