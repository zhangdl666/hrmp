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
<body>
	<table class="table table-striped">
		<thead>
			<tr>
				<th>序号</th>
				<th>菜单名称</th>
				<th>菜单URL</th>
				<th>授权时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="roleMenuBoList==null || roleMenuBoList.size==0">
				<tr><td colspan="5">未找到任何数据！</td></tr>
			</s:if>
			<s:else>
				<s:iterator value="roleMenuBoList" status="st">
					<tr>
						<td><s:property value='#st.index+1' /></td>
						<td><s:property value='menu.menuName' /></td>
						<td><s:property value='menu.menuUrl' /></td>
						<td><s:date name="createTime" format="yyyy-MM-dd"></s:date></td>
						<td><a href="javascript:deleteRoleMenu('<s:property value='id' />')">删除</a></td>
					</tr>
				</s:iterator>
			</s:else>
		</tbody>
	</table>
	
<script type="text/javascript">
	var mess = "${message}";
	if(mess!=null && mess!="") {
		alert(mess);
	}
</script>
</BODY>
</html>
