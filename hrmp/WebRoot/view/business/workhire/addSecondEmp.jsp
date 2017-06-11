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

<title>添加二次用工</title>

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
		
	
	<form action="business/saveSecondEmp.action" method="post" class="form-horizontal" id="editForm">
		<s:hidden name="workHireId"></s:hidden>
		<fieldset>
        	<legend>
        		添加二次用工
        	</legend>
			<div class="form-group">
				<label class="col-sm-2 control-label">工人</label>
				<div class="col-sm-10">
					<s:textfield name="empNames" id="empNames" readonly="readonly" size="50"></s:textfield>
					<button type="button" class="btn btn-default" onclick="showUserTree('empIds','empNames')">选择</button>
					<s:hidden name="empIds" id="empIds"></s:hidden>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">说明</label>
				<div class="col-sm-10">
					<s:textarea name="remark" class="form-control"></s:textarea>
				</div>
			</div>
			
			<div class="form-group">
				<div class="col-sm-12 col-md-offset-6">
					<button type="button" onclick="cancel()" class="btn btn-warning" id="saveBtn">取消</button>
					<button type="submit" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</fieldset>
	</form>
	
</div>
<script type="text/javascript">

var mess = "${message}";
if(mess=="添加成功") {
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
          "remark":{
        	  validators: {
                  notEmpty: {
                      message: '说明不能为空'
                  }
              }
          }
      }
  });
}

function showUserTree(ids,names) {
	var checkedIds = "";
	var rId = document.getElementById("workHireId").value;
	var _url = "business/selectUsersForSecondEmp.action?workHireId=" + rId + "&&checkedIds=" + checkedIds;
	var values = window.showModalDialog(_url,"dialogWidth=50px;dialogHeight=100px");
	if(values!=null && values!="") {
		var aRetValue = values.split(";");
		document.getElementById(ids).value = aRetValue[0];
		document.getElementById(names).value = aRetValue[1];
	}
}

$(document).ready(initValidator());
</script>
</BODY>
</html>
