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

<title>修改密码</title>

<!-- Bootstrap core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/navbar-fixed-top.css" rel="stylesheet">
<link href="css/bootstrapValidator.css" rel="stylesheet">
<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<link href="css/ie10-viewport-bug-workaround.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="css/sticky-footer-navbar.css" rel="stylesheet">
<link href="css/bootstrap-datetimepicker.min.css" rel="stylesheet">
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
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/bootstrapValidator.js"></script>
<script type="text/javascript" src="js/common.js" charset="gbk"></script>
<script type="text/javascript" src="js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script type="text/javascript" src="js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
</head>

<BODY>
<%@include file="/header_nav.jsp"%>
<div class="container" id="bodyContainer">
	<form action="organization/modifyPwd.action" method="post" class="form-horizontal" id="editForm">
		<fieldset>
        	<legend>修改密码</legend>
			<div class="form-group">
				<label class="col-sm-2 control-label">原密码</label>
				<div class="col-sm-2">
					<s:password name="originalPwd" class="form-control"></s:password>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">新密码</label>
				<div class="col-sm-2">
					<s:password name="newPwd" class="form-control"></s:password>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">再次输入新密码</label>
				<div class="col-sm-2">
					<s:password name="confirmPwd" class="form-control"></s:password>
				</div>
			</div>
			
			<div class="form-group">
				<div class="col-sm-4 col-md-offset-2">
					<button type="submit" class="btn btn-primary" id="saveBtn">修改</button>
				</div>
			</div>
		</fieldset>
	</form>
</div>
<script type="text/javascript">

function closeWin() {
	window.close();
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
    	  "originalPwd": {
              validators: {
                  notEmpty: {
                      message: '原密码不能为空'
                  }
              }
          },
          "newPwd": {
              validators: {
                  notEmpty: {
                      message: '新密码不能为空'
                  },
                  identical: {
                      field: 'confirmPwd',
                      message: '两次输入的新密码不一致'
                  }
              }
          },
          "confirmPwd": {
              validators: {
                  notEmpty: {
                      message: '再次输入新密码不能为空'
                  },
                  identical: {
                      field: 'newPwd',
                      message: '两次输入的新密码不一致'
                  }
              }
          }
      }
  });
}

$(document).ready(initValidator());

var mess = "${message}";
if(mess!=null && mess!="") {
	alert(mess);
}
</script>
</BODY>
</html>
