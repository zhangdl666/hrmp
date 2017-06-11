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

<title>消息详情</title>

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
	<form action="" method="post" class="form-horizontal" id="editForm">
		<fieldset>
        	<legend>消息详情</legend>
			<div class="form-group">
				<label class="col-sm-2 control-label">发送人</label>
				<div class="col-sm-2">
					<s:property value="messageBo.sendUser.userName"/> &nbsp;&nbsp;
					<s:property value="messageBo.sendUser.mobile"/>
				</div>
				<label class="col-sm-2 control-label">发送时间</label>
				<div class="col-sm-4">
					<s:date name="messageBo.createTime" format="yyyy-MM-dd HH:mm:ss"></s:date>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">接收人</label>
				<div class="col-sm-2">
					<s:property value="messageBo.receiverUser.userName"/> &nbsp;&nbsp;
					<s:property value="messageBo.receiverUser.mobile"/>
				</div>
				
				<s:if test="messageBo.isRead=='0'">
					<label class="col-sm-2 control-label">状态</label>
					<div class="col-sm-4">
						未读
					</div>
				</s:if>
				<s:else>
					<label class="col-sm-2 control-label">状态</label>
					<div class="col-sm-1">
						已读
					</div>
					<label class="col-sm-2 control-label">阅读时间</label>
					<div class="col-sm-1">
						<s:date name="messageBo.readTime" format="yyyy-MM-dd HH:mm:ss"></s:date>
					</div>
				</s:else>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">标题</label>
				<div class="col-sm-8">
					<s:textfield name="messageBo.messageTitle" class="form-control"></s:textfield>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">内容</label>
				<div class="col-sm-8">
					<s:textarea name="messageBo.messageContent" class="form-control"></s:textarea>
				</div>
			</div>
			
			<div class="form-group">
				<div class="col-sm-10 col-md-offset-5">
					<button type="button" class="btn btn-primary" id="saveBtn" onclick="closeWin()">关闭</button>
				</div>
			</div>
		</fieldset>
	</form>
</div>
<script type="text/javascript">

function closeWin() {
	window.close();
}
</script>
</BODY>
</html>
