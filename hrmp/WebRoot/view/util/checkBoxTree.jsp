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

<title>请选择</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="css/zTreeStyle/zTreeStyle.css" type="text/css">
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="js/jquery.ztree.excheck.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<SCRIPT type="text/javascript">
	
	
	var setting = {
		check : {
			enable : true
		},
		data : {
			simpleData : {
				enable : true
			}
		},
	};
	var zTree;
	var treeNodes =  ${treeNodeData};
	$(document).ready(function(){
		$.fn.zTree.init($("#tree"), setting, treeNodes);
	});
	function cancel(){
		window.returnValue = null;
		window.close();
	}
	
	function confirm(){
		var treeObj=$.fn.zTree.getZTreeObj("tree");
        nodes=treeObj.getCheckedNodes(true);
        if(nodes.length==0) {
        	alert("未选择任何值！");
        	return;
        }
        var names = "";
        var ids = "";
        for(var i=0;i<nodes.length;i++){
        	if(!nodes[i].isParent){//只取叶子节点
		        names = names + nodes[i].name + ",";
        		ids = ids + nodes[i].id + ",";
        	}
        }
        window.returnValue = ids + ";" + names;
        window.close();
	}
//-->
</SCRIPT>
</head>

<BODY>
	<div class="form-group">
		<button type="button" class="btn btn-xs btn-default" onclick="cancel()">取消</button>
		<button type="button" class="btn btn-xs btn-default" onclick="confirm()">确定</button>
	</div>
	<div class="form-group">
		<ul id="tree" class="ztree"></ul>
	</div>
	<div class="form-group">
		<button type="button" class="btn btn-xs btn-default" onclick="cancel()">取消</button>
		<button type="button" class="btn btn-xs btn-default" onclick="confirm()">确定</button>
	</div>
</BODY>
</html>
