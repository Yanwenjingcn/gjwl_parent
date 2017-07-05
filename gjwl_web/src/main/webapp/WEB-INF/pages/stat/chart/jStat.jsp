<%@ page contentType="text/html; charset=utf-8"%>
<html>
<head>
</head>

<script type="text/javascript">
	//必须从此转向，否则路径错误会导致读取配置xml和数据xml文件错误。
	var _date = new Date();
	
	//因为直接访问html页面，服务器就会做缓存，我希望自己的结果是最新的，所以就要通过跳转来完成数据的查询与刷新
	//加上参数date后，每次请求的url看起来都不一样，就不缓存了。
	window.location.href = "${pageContext.request.contextPath}/stat/chart/<%=request.getParameter("forward")%>/index.html?d="+_date;
</script>

<body>


</body>
</html>
