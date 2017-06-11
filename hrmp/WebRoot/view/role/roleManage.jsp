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

<title>角色管理</title>
<%@include file="/cssAndJs.jsp"%>
<link href="css/dashboard.css" rel="stylesheet">
<link rel="stylesheet" href="css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="js/common.js" charset="gbk"></script>
<SCRIPT type="text/javascript">
	
	var setting = {
		data : {
			simpleData : {
				enable : true
			}
		},
		callback: {
			onClick: showDeptRoleList
		}
	};
	var zTree;
	var treeNodes;

	$(function() {
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			dataType : "json",
			url : "organization/getDeptTree.action?deptId=000",//请求的action路径   
			error : function() {//请求失败处理函数   
				alert('请求失败');
			},
			success : function(data) { //请求成功后处理函数。     
				treeNodes = data; //把后台封装好的简单Json格式赋给treeNodes   
			}
		});
		$.fn.zTree.init($("#tree"), setting, treeNodes);
	});
	
	function showDeptRoleList(event, treeId, treeNode, clickFlag) {
		document.getElementById("deptId").value = treeNode.id;
		document.getElementById("currentPage").value = 1;
		var _url = "organization/roleQuery.action";
		var params = $("#queryForm").serialize();
		//var params = "deptId=" + treeNode.id + "&currentPage=1";
		queryByAjax("roleManagerContainer", _url, "post", params);
	}
//-->
</SCRIPT>
</head>

<BODY>
	<%@include file="/header_nav.jsp"%>
	<div class="row">
		<div class="col-sm-3 col-md-2 sidebar">
			<div class="zTreeDemoBackground left">
				<ul id="tree" class="ztree"></ul>
			</div>
		</div>
		<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main"
			id="roleManagerContainer">
			请先选择左侧的组织结构树，以便进行角色信息维护！
			<form action="organization/roleQuery.action" id="queryForm">
				<s:hidden name="deptId"></s:hidden>
				<s:hidden name="page.currentPage" id="currentPage"></s:hidden>
				<s:hidden name="page.totalRowSize" id="totalRowSize"></s:hidden>
				<s:hidden name="page.totalPage" id="totalPage"></s:hidden>
			</form>
		</div>
	</div>
	<%@include file="/footer.jsp"%>
</BODY>
</html>
