<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<link rel="icon" href="image/favicon.ico">

<title>用户管理</title>
<!-- Bootstrap core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<link href="css/ie10-viewport-bug-workaround.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="css/sticky-footer-navbar.css" rel="stylesheet">

<link href="css/dashboard.css" rel="stylesheet">

<!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
<!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
<script src="js/ie-emulation-modes-warning.js"></script>

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>
</head>

<BODY>
	<form action="organization/userQuery.action" id="queryForm">
		<s:hidden name="deptId" id="deptId"></s:hidden>
		<s:hidden name="page.currentPage" id="currentPage"></s:hidden>
		<s:hidden name="page.totalRowSize" id="totalRowSize"></s:hidden>
		<s:hidden name="page.totalPage" id="totalPage"></s:hidden>
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">查询条件</h3>
			</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-sm-1" align="right">登录名</div>
					<div class="col-sm-2">
						<input type="text" name="loginName" value="${loginName }" />
					</div>
					<div class="col-sm-1" align="right">姓名</div>
					<div class="col-sm-2">
						<input type="text" name="userName" value="${userName }" />
					</div>
					<div class="col-sm-1">
						<button type="button" class="btn btn-primary" onclick="queryUser()">查询</button>
					</div>
					<div class="col-sm-1">
						<button type="button" class="btn btn-primary" onclick="addUser()">添加用户</button>
					</div>
				</div>
			</div>
		</div>
	
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">查询结果</h3>
			</div>
			<div class="panel-body">
	
				<div class="col-sm-12">
					<table class="table table-condensed">
						<thead>
							<tr>
								<th>序号</th>
								<th>姓名</th>
								<th>手机号</th>
								<th>用户类型</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<s:if test="userList==null || userList.size==0">
								<tr><td colspan="4">未找到任何数据！</td></tr>
							</s:if>
							<s:else>
								<s:iterator value="userList" status="st">
									<tr>
										<td><s:property value='#st.index+1' /></td>
										<td><a href="javascript:viewUser('<s:property value='id' />')"><s:property value='userName' /></a></td>
										<td><s:property value='loginName' /></td>
										<td><s:if test="userKind=='manager'">公司员工</s:if><s:else>注册用户</s:else></td>
										<td><a href="javascript:deleteUser('<s:property value='id' />')">删除</a></td>
									</tr>
								</s:iterator>
							</s:else>
						</tbody>
					</table>
				</div>
				<div align="center">
					共<s:property value='page.totalRowSize' />条记录，每页
					<s:select list="#{10:10,20:20,50:50,100:100 }" listKey="key" listValue="value" name="page.pageSize" id="pageSize" onchange="pageSizeChange()"></s:select>条
					<s:property value='page.currentPage' />/<s:property value='page.totalPage' />
					<s:if test="page.currentPage>1">
						<a href="javascript:movePage('first')">首页</a>
						<a href="javascript:movePage('up')">上一页</a>
					</s:if>
					<s:if test="page.currentPage<page.totalPage">
						<a href="javascript:movePage('down')">下一页</a>
						<a href="javascript:movePage('last')">末页</a>
					</s:if>
					&nbsp;&nbsp;&nbsp;跳转到<s:textfield name="forwardPage" id="forwardPage" size="3" onkeyup="this.value=this.value.replace(/[^\d]/g,'') "></s:textfield>页
					<button type="button" class="btn btn-default" onclick="jumpPage()">确定</button>
				</div>
			</div>
		</div>
	</form>
<script type="text/javascript">
function movePage(pageNo) {
	//var page = $("currentPage");
	var pg = document.getElementById("currentPage");
	if(pageNo=="first"){
		pg.value = 1;
	}else if(pageNo=="last"){
		var tp = document.getElementById("totalPage").value;
		pg.value = parseInt(tp);
	}else if(pageNo=="up"){
		pg.value = parseInt(pg.value) - 1;
	}else if(pageNo=="down"){
		pg.value = parseInt(pg.value) + 1;
	}
	var _url = "organization/userQuery.action";
	var params = $("#queryForm").serialize();
	queryByAjax("userManagerContainer", _url, "post", params);
}

function jumpPage(){
	var pg = document.getElementById("currentPage");
	var totalPage = document.getElementById("totalPage");
	var forwardPage = document.getElementById("forwardPage");
	var forwardPageValue = parseInt(forwardPage.value);
	var totalPageValue = parseInt(totalPage.value);
	if(forwardPageValue>totalPageValue){
		forwardPageValue = totalPageValue;
		forwardPage.value = totalPageValue;
	}
	pg.value = forwardPageValue;
	var _url = "organization/userQuery.action";
	var params = $("#queryForm").serialize();
	queryByAjax("userManagerContainer", _url, "post", params);
}

function queryUser(){
	var _url = "organization/userQuery.action";
	var params = $("#queryForm").serialize();
	queryByAjax("userManagerContainer", _url, "post", params);
}

function pageSizeChange(){
	var pg = document.getElementById("currentPage");
	pg.value = 1;
	var _url = "organization/userQuery.action";
	var params = $("#queryForm").serialize();
	queryByAjax("userManagerContainer", _url, "post", params);
}

function deleteUser(id) {
	var _url = "organization/delUser.action";
	var params = $("#queryForm").serialize();
	params = params + "&delUserId=" + id;
	queryByAjax("userManagerContainer", _url, "post", params);
}

function viewUser(id) {
	var _url = "organization/viewUser.action?userId=" + id;
	window.open(_url);
}

function addUser(){
	var dId = document.getElementById("deptId").value;
	var _url = "organization/viewUser.action?deptId=" + dId;
	window.open(_url);
}

var mess = "${message}";
if(mess!=null && mess!="") {
	alert(mess);
}

</script>
</BODY>
</html>
