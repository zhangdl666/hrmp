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

<title>回访记录</title>

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
<div class="container" id="bodyContainer">
		
	
	<form action="business/saveWorkHireVisit.action" method="post" class="form-horizontal" id="editForm">
		<s:if test="workHireVisit.id!=null">
			<s:hidden name="workHireVisit.id"></s:hidden>
		</s:if>
		<s:hidden name="workHireVisit.visitTime">
			<s:param name="value"><s:date name="workHireVisit.visitTime" format="yyyy-MM-dd HH:mm:ss"></s:date></s:param>
		</s:hidden>
		<s:hidden name="workHireVisit.workHireId"></s:hidden>
		<s:hidden name="workHireVisit.visitUser"></s:hidden>
		<fieldset>
        	<legend>
        		回访记录
        	</legend>
			<div class="form-group">
				<label class="col-sm-2 control-label">回访记录</label>
				<div class="col-sm-10">
					<s:textarea name="workHireVisit.visitRecord" class="form-control"></s:textarea>
				</div>
			</div>
			
			<s:if test="permission=='write'">
				<div class="form-group">
					<div class="col-sm-12 col-md-offset-6">
						<button type="button" onclick="cancel()" class="btn btn-warning" id="saveBtn">取消</button>
						<button type="submit" class="btn btn-primary" id="saveBtn">保存</button>
					</div>
				</div>
			</s:if>
		</fieldset>
	</form>
	
</div>
<script type="text/javascript">

var mess = "${message}";
if(mess=="保存成功") {
	window.close();
}

function cancel(){
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
          "workHireVisit.visitRecord":{
        	  validators: {
                  notEmpty: {
                      message: '回访记录不能为空'
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
