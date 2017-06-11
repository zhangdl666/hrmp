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

<title>菜单编辑</title>
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
	<form action="organization/saveMenu.action" method="post" class="form-horizontal" id="editForm">
		<s:if test="menu.id!=null">
			<s:hidden name="menu.id"></s:hidden>
		</s:if>
		<s:hidden name="menu.parentId"></s:hidden>
		<s:hidden name="menu.validstatus"></s:hidden>
		<s:hidden name="menu.createTime"></s:hidden>
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h3 class="panel-title">基本信息</h3>
			</div>
			<div class="panel-body">
				<div class="form-group">
					<label class="col-md-2 control-label">菜单名称</label>
					<div class="col-md-4">
						<s:textfield name="menu.menuName" class="form-control"></s:textfield>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">菜单Url</label>
					<div class="col-md-4">
						<s:textfield name="menu.menuUrl" class="form-control"></s:textfield>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">菜单排序</label>
					<div class="col-md-4">
						<s:textfield name="menu.menuIndex" class="form-control"></s:textfield>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">备注</label>
					<div class="col-md-4">
						<s:textfield name="menu.remark" class="form-control"></s:textfield>
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
		
	</form>
<script type="text/javascript">

function closeWin(){
	window.opener.queryMenu();
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
          "menu.menuName": {
              validators: {
                  notEmpty: {
                      message: '菜单名称不能为空'
                  }
              }
          },
          "menu.menuIndex": {
              validators: {
                  notEmpty: {
                      message: '菜单排序不能为空'
                  },
                  numeric:{
                	  message: '菜单排序必须为数值'
                  }
              }
          }
      }
  });
}

$(document).ready(initValidator());
</script>
</BODY>
</html>
