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

<title>角色编辑</title>
<!-- Bootstrap core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/bootstrapValidator.css" rel="stylesheet">
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
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/bootstrapValidator.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>
<script type="text/javascript" src="js/common.js"  charset="gbk"></script>
</head>

<BODY>
	<form action="organization/saveRole.action" method="post" class="form-horizontal" id="editForm">
		<s:hidden name="deptId"></s:hidden>
		<s:hidden name="roleId" id="roleId"></s:hidden>
		<s:hidden name="delId" id="delId"></s:hidden>
		<s:if test="role.id!=null">
			<s:hidden name="role.id"></s:hidden>
			<s:hidden name="role.validstatus"></s:hidden>
		</s:if>
		<s:hidden name="role.deptId"></s:hidden>
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h3 class="panel-title">基本信息</h3>
			</div>
			<div class="panel-body">
				<div class="form-group">
					<label class="col-md-2 control-label">角色名称</label>
					<div class="col-md-4">
						<s:textfield name="role.roleName" class="form-control"></s:textfield>
					</div>
				</div>
				
				<div class="form-group">
					<div class="col-md-6 col-md-offset-2">
						<button type="button" class="btn btn-default" onclick="closeWin()">关闭</button>
						<button type="submit" class="btn btn-primary" id="saveBtn">保存</button>
					</div>
				</div>
			</div>
		</div>
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h3 class="panel-title">关联用户列表 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<button type="button" class="btn btn-default" onclick="showUserTree()">添加</button>
				</h3>
			</div>
			<div class="panel-body">
				<div class="col-md-8" id="div_relationUser">
					
				</div>
			</div>
		</div>
		
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h3 class="panel-title">关联菜单列表 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<button type="button" class="btn btn-default" onclick="showMenuTree()">添加</button>
				</h3>
			</div>
			<div class="panel-body">
				<div class="col-md-8" id="div_relationMenu">
				
				</div>
			</div>
		</div>
	</form>
<script type="text/javascript">

function closeWin(){
	window.opener.queryRole();
	window.close();
}

var mess = "${message}";
if(mess!=null && mess!="") {
	alert(mess);
}

function initValidator() {
	$('#editForm').bootstrapValidator({
      live: 'enabled',
      message: 'This value is not valid',
      feedbackIcons: {
          valid: 'glyphicon glyphicon-ok',
          invalid: 'glyphicon glyphicon-remove',
          validating: 'glyphicon glyphicon-refresh'
      },
      fields: {
          "role.roleName": {
              validators: {
                  notEmpty: {
                      message: '角色名称不能为空'
                  }
              }
          }
      }
  });
}

function deleteUserRole(id) {
	var _url = "organization/delUserRole.action";
	var rId = document.getElementById("roleId").value;
	var params = "roleId=" + rId + "&&delId=" + id;
	queryByAjax("div_relationUser", _url, "post", params);
}

function deleteRoleMenu(id) {
	var _url = "organization/delRoleMenu.action";
	var rId = document.getElementById("roleId").value;
	var params = "roleId=" + rId + "&&delId=" + id;
	queryByAjax("div_relationMenu", _url, "post", params);
}

function showUserTree() {
	var checkedIds = "";
	var rId = document.getElementById("roleId").value;
	var _url = "organization/selectUsersForRole.action?roleId=" + rId + "&&checkedIds=" + checkedIds;
	window.showModalDialog(_url,"dialogWidth=50px;dialogHeight=100px");
}

function showMenuTree() {
	var checkedIds = "";
	var rId = document.getElementById("roleId").value;
	var _url = "organization/selectMenusForRole.action?roleId=" + rId + "&&checkedIds=" + checkedIds;
	window.showModalDialog(_url,"dialogWidth=50px;dialogHeight=100px");
}
$(document).ready(initValidator());

function refreshRelationUser(){
	var rId = document.getElementById("roleId").value;
	var _url = "organization/ajaxRefreshRelationUser.action";
	var params = "roleId=" + rId;
	queryByAjax("div_relationUser", _url, "post", params);
}

function refreshRelationMenu(){
	var rId = document.getElementById("roleId").value;
	var _url = "organization/ajaxRefreshRelationMenu.action";
	var params = "roleId=" + rId;
	queryByAjax("div_relationMenu", _url, "post", params);
}

//加载关联用户列表
refreshRelationUser();
//加载关联菜单列表
refreshRelationMenu();
</script>
</BODY>
</html>
