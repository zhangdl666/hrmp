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

<title></title>
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
	<div class="col-sm-12" id="workSignDiv">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>序号</th>
					<th>报名时间</th>
					<th>工人名称</th>
					<th>审核结果</th>
					<th>审核时间</th>
					<th>备注</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="signList==null || signList.size==0">
					<tr>
						<td colspan="6">未找到任何数据！</td>
					</tr>
				</s:if>
				<s:else>
					<s:iterator value="signList" status="st">
						<tr>
							<td><s:property value='#st.index+1' />
							</td>
							<td><s:date name="workSign.signTime"
									format="yyyy-MM-dd HH:mm:ss"></s:date>
							</td>
							<td <s:if test="emp.userKind=='VIP'">bgcolor="green"</s:if>
							    <s:if test="emp.userKind=='badCredit'">bgcolor="red"</s:if>
							>
									<s:property value='emp.userName' />
							</td>
							<td><s:if test="workSign.confirmResult=='pass'">
									通过
								</s:if> 
								<s:elseif test="workSign.confirmResult=='noPass'">
									未通过
								</s:elseif> 
								<s:elseif test="workSign.confirmResult=='cancel'">
									已取消
								</s:elseif> 
								<s:elseif test="workSign.confirmResult=='callOut'">
									被调出
								</s:elseif> 
								<s:elseif test="workSign.confirmResult=='callIn'">
									调入
								</s:elseif> 
								<s:elseif test="workSign.confirmResult=='approving'">
									审核中
								</s:elseif> 
							</td>
							<td><s:date name="workSign.confirmTime"
									format="yyyy-MM-dd HH:mm:ss"></s:date>
							</td>
							<td><s:property value='workSign.confirmDescri' />
							</td>
						</tr>
					</s:iterator>
				</s:else>
			</tbody>
		</table>
	</div>
<script type="text/javascript">

</script>
</BODY>
</html>
