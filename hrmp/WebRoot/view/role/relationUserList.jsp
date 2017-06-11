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
				<th>登录名</th>
				<th>用户名称</th>
				<th>授权时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="userRoleBoList==null || userRoleBoList.size==0">
				<tr><td colspan="5">未找到任何数据！</td></tr>
			</s:if>
			<s:else>
				<s:iterator value="userRoleBoList" status="st">
					<tr>
						<td><s:property value='#st.index+1' /></td>
						<td><s:property value='user.loginName' /></td>
						<td><s:property value='user.userName' /></td>
						<td><s:date name="createTime" format="yyyy-MM-dd"></s:date></td>
						<td><a href="javascript:deleteUserRole('<s:property value='id' />')">删除</a></td>
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
