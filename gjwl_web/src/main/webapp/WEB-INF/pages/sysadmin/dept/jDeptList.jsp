<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../../baselist.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<script type="text/javascript" src="${ctx }/js/jquery-1.4.4.js"></script>
<script>
	function isOnlyChecked() {
		var checkBoxArray = document.getElementsByName('id');
		var count = 0;
		for (var index = 0; index < checkBoxArray.length; index++) {
			if (checkBoxArray[index].checked) {
				count++;
			}
		}
		//jquery
		//var count = $("[input name='id']:checked").size();
		if (count == 1)
			return true;
		else
			return false;
	}
	function toView() {
		if (isOnlyChecked()) {
			formSubmit('deptAction_toview', '_self');
		} else {
			alert("请先选择一项并且只能选择一项，再进行操作！");
		}
	}
	//实现更新
	function toUpdate() {
		if (isOnlyChecked()) {
			formSubmit('deptAction_toupdate', '_self');
		} else {
			alert("请先选择一项并且只能选择一项，再进行操作！");
		}
	}
</script>
</head>

<body>
	<form name="icform" method="post">

		<div id="menubar">
			<div id="middleMenubar">
				<div id="innerMenubar">
					<div id="navMenubar">
						<ul>
							<li id="view"><a href="#" onclick="javascript:toView()">查看</a></li>
							<li id="new"><a href="#"
								onclick="formSubmit('deptAction_tocreate','_self');this.blur();">新增</a></li>
							<li id="update"><a href="#" onclick="javascript:toUpdate()">修改</a></li>
							<li id="delete"><a href="#"
								onclick="formSubmit('deptAction_delete','_self');this.blur();">删除</a></li>
						</ul>
					</div>
				</div>
			</div>
		</div>

		<div class="textbox" id="centerTextbox">
			<div class="textbox-header">
				<div class="textbox-inner-header">
					<div class="textbox-title">部门列表</div>
				</div>
			</div>

			<div>


				<div class="eXtremeTable">
					<table id="ec_table" class="tableRegion" width="98%">
						<thead>
							<tr>
								<td class="tableHeader"><input type="checkbox" name="selid"
									onclick="checkAll('id',this)"></td>
								<td class="tableHeader">序号</td>
								<td class="tableHeader">编号</td>
								<td class="tableHeader">上级</td>
								<td class="tableHeader">名称</td>
							</tr>
						</thead>
						<tbody class="tableBody">

							<!-- 这里的这个也是直接操作page对象中的内容所得的 ，实际上是调用的getLinks（）的方法-->
							<!-- 在struts2中将page加入到值栈中，
								那么可以直接使用其中的属性，都不用什么page.result，直接result就可以 -->
							${links }

							<!-- 這这个是jstl表达式，如果我们要操作的对象在栈顶，那么我们就可以直接操作里面的内容了
	比如这里的page，我们就是直接操作page里面的属性result的，如果page不在栈顶就会比较麻烦了-->
	
							<c:forEach items="${results }" var="dept" varStatus="st">
								<tr class="odd" onmouseover="this.className='highlight'"
									onmouseout="this.className='odd'">
									<td><input type="checkbox" name="id" value="${dept.id }" /></td>
									<td>${st.count }</td>
									<td>${dept.id }</td>
									<td>${dept.parent.deptName }</td>
									<td><a href="deptAction_toview?id=${dept.id }">${dept.deptName }</a></td>
								</tr>
							</c:forEach>
							

						</tbody>
					</table>
				</div>

			</div>
	</form>
</body>
</html>

