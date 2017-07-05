package com.ywj.gjwl.stat;

import java.io.FileNotFoundException;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.ywj.gjwl.action.BaseAction;
import com.ywj.gjwl.springdao.SqlDao;
import com.ywj.gjwl.utils.file.FileUtil;

public class StatChartAction extends BaseAction{

	/** 
	* @Fields serialVersionUID : TODO
	*/ 
	private static final long serialVersionUID = 1L;
	private SqlDao sqlDao;
	
	public void setSqlDao(SqlDao sqlDao) {
		this.sqlDao = sqlDao;
	}
	
	//select语句的执行过程
//	1、from
//	2、order by
//	3、select
//	4、order by

	//=============================================
	public String factorysale() throws Exception {
		//1.执行sql语句，得到统计结果
	    List<String> list = execSQL("select factory_name,sum(amount) samount from contract_product_c group by factory_name order by samount desc");
	    
	    //2.组织符合要求的json数据
	    StringBuilder sb = new StringBuilder();
	    sb.append("[");
	    /**
	     *      {
                    "country": "USA",
                    "visits": 4025,
                    "color": "#FF0F00"
                }
	     */
	    String colors[]={"#FF0F00","#FF6600","#FF9E01","#FCD202","#F8FF01","#B0DE09","#04D215","#0D52D1","#2A0CD0","#8A0CCF","#CD0D74","#754DEB"};
	    int j=0;
	    for(int i=0;i<list.size();i++){
	    	sb.append("{").append("\"factory\":\"").append(list.get(i)).append("\",")
	    	              .append("\"amount\":\"").append(list.get((++i))).append("\",")
	    	              .append("\"color\":\"").append(colors[j++]).append("\"")
	    	.append("}").append(",");
	    	if(j>=colors.length){
	    		j=0;
	    	}
	    }
	    sb.delete(sb.length()-1, sb.length());
	    
	    sb.append("]");
	    
	    //3.将json数据放入值栈中
	    super.put("result", sb.toString());
	    
	    //4.返回结果
		return "factorysale01";
	}
	
	//早期xml版本的数据读取十分的挑浏览器
	/*public String factorysale() throws Exception {
		String sql="select factory_name,sum(amount) smount from contract_product_c group by factory_name order by desc ";
		//这里的泛型是String,为啥不是对象啊？
		List<String> list=sqlDao.executeSQL(sql);
		
		//组织符合要求的xml数据
		//StringBuilder是非线程安全的，因此效率更快
		StringBuilder sb=new StringBuilder();
		return "factorysale";
	}
	*/
	
	
	/**
	 * 产品销量的前15名
	 */
	public String productsale() throws Exception {
		//1.执行sql语句，得到统计结果
	
		//List<String> list = execSQL("select * from ( select product_no,sum(amount) samount from contract_product_c group by product_no order by samount desc ) b  where rownum<16");
		List<String> list = execSQL("	SELECT product_no,SUM(amount) samount FROM contract_product_c GROUP BY product_no ORDER BY samount DESC LIMIT 0,15");
		//2.组织符合要求的xml数据
		String content = genBarDataSet(list);
		
		//3.将拼接好的字符串写入data.xml文件中
		writeXML("stat\\chart\\productsale\\data.xml",content);
		
		//4.返回结果
		return "productsale";
	}
	
	
	/**
	 * 在线人数统计
	 * 表：LOGIN_LOG_P
	 *     它的数据来源于，在登录时，对于登录者的ip信息进行记录
	 *     
	 *  select a.a1, nvl(b.c,0)
		from 
		     (select * from online_info_t) a
		left join 
		      (select to_char(login_time,'HH24') a1, count(*) c from login_log_p group by  to_char(login_time,'HH24') order by a1)
		       b
		on (a.a1=b.a1)
		order by a.a1
	 */
	public String onlineinfo() throws Exception {
		//1.执行sql语句，得到统计结果
		
		//ifnull等同于oracle的nvl，就是如果a如果为空的话则值设置为b.ifnull(a,b)
		//List<String> list = execSQL("select a.a1, nvl(b.c,0) from (select * from online_info_t) a left join (select to_char(login_time,'HH24') a1, count(*) c from login_log_p group by  to_char(login_time,'HH24') order by a1) b on (a.a1=b.a1) order by a.a1");
		List<String> list = execSQL("SELECT a.a1, IFNULL(b.c,0) FROM (SELECT * FROM online_info_t) a LEFT JOIN (SELECT DATE_FORMAT(login_time,'%H') a1, COUNT(*) c FROM login_log_p GROUP BY  DATE_FORMAT(login_time,'%H') ORDER BY a1) b ON (a.a1=b.a1) ORDER BY a.a1");
		//2.组织符合要求的xml数据
		String content = genBarDataSet(list);
		
		//3.将拼接好的字符串写入data.xml文件中
		writeXML("stat\\chart\\onlineinfo\\data.xml",content);
		
		//4.返回结果
		return "onlineinfo";
	}
	//==============================================================
	
	
	
	
	/**
	 * 执行sql
	 * @param sql
	 * @return
	 */
	private List<String> execSQL(String sql ) {
	    //1.执行sql语句，得到统计结果
	    List<String> list = sqlDao.executeSQL(sql);
		return list;
	}

	/**
	 * 生成柱状图的数据源
	 * @param list
	 * @return
	 */
	private String genBarDataSet(List<String> list) {
		StringBuilder sb = new StringBuilder();
	    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	    sb.append("<chart><series>");
	    
	    int j=0;
	    for(int i=0;i<list.size();i++){
	    	sb.append("<value xid=\""+(j++)+"\">"+list.get(i)+"</value>");
	    	i++;
	    }
	    
	    sb.append("</series><graphs><graph gid=\"30\" color=\"#FFCC00\" gradient_fill_colors=\"#111111, #1A897C\">");
	    
	    
	    j=0;
	    for(int i=0;i<list.size();i++){
	    	i++;
	    	sb.append("<value xid=\""+(j++)+"\" description=\"\" url=\"\">"+list.get(i)+"</value>");
	    }
	    
	    sb.append("</graph></graphs>");
	    sb.append("<labels><label lid=\"0\"><x>0</x><y>20</y><rotate></rotate><width></width><align>center</align><text_color></text_color><text_size></text_size><text><![CDATA[<b>JavaEE28期产品销量排名</b>]]></text></label></labels>");
	    sb.append("</chart>");
		return sb.toString();
	}
	
	/**
	 * 生成饼图的数据源
	 * @param list
	 * @return
	 */
	private String genPieDataSet(List<String> list) {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<pie>");
		
		for(int i=0;i<list.size();i++){
			sb.append("<slice title=\""+list.get(i)+"\" pull_out=\"true\">"+list.get(++i)+"</slice>");
		}
		
		sb.append("</pie>");
		return sb.toString();
	}
	
	/**
	 * 写入xml
	 * @param sb
	 * @throws FileNotFoundException
	 */
	private void writeXML(String fileName ,String content) throws FileNotFoundException {
		FileUtil fileUtil = new FileUtil();
	    String sPath = ServletActionContext.getServletContext().getRealPath("/");
	    fileUtil.createTxt(sPath, fileName, content, "UTF-8");
	}
}
