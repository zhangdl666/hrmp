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

<title>错误</title>
<%@include file="/cssAndJs.jsp"%>
</head>

<body>
	<%@include file="/header_nav.jsp"%>

	<!-- Begin page content -->
	<div class="container" id="bodyContainer">
		<div>抱歉！操作失败。</div>
		<div>
			<s:property value="exception.message"/>
		</div>
	</div>

	<%@include file="/footer.jsp"%>
</body>
</html>
