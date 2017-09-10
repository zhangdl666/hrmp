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

<title>招工详情</title>

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
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script type="text/javascript" src="js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
</head>

<BODY>
<%@include file="/header_nav.jsp"%>
<div class="container" id="bodyContainer">
		
	
	<form action="business/workSign.action" method="post" class="form-horizontal" id="editForm">
		<s:if test="workHire.id!=null">
			<s:hidden name="workHire.id"></s:hidden>
		</s:if>
		<s:hidden name="workHire.createTime">
			<s:param name="value"><s:date name="workHire.createTime" format="yyyy-MM-dd HH:mm:ss"></s:date></s:param>
		</s:hidden>
		<s:hidden name="workHire.publisherId"></s:hidden>
		<s:hidden name="workHire.publisherName"></s:hidden>
		<s:hidden name="workHire.publisherCompanyId"></s:hidden>
		<s:hidden name="workHire.publisherCompanyName"></s:hidden>
		<s:hidden name="workHire.publishDate"></s:hidden>
		<s:hidden name="workHire.status" id="status"></s:hidden>
		<s:hidden name="workHireId" id="workHireId"></s:hidden>
		<fieldset>
        	<legend>
        		招工信息
        		<s:if test="workHire.id!=null">
					<font color="red">报名要诚信，如审核通过后不去，系统将自动关闭以后的报名权限！</font>
				</s:if>
        	</legend>
			<div class="form-group">
				<label class="col-sm-2 control-label">发布人</label>
				<div class="col-sm-4">
					<s:property value="workHire.publisherCompanyName"/> &nbsp;&nbsp;
					<s:property value="workHire.publisherName"/>&nbsp;&nbsp;
					<s:date name="workHire.createTime" format="yyyy-MM-dd HH:mm:ss"></s:date>
				</div>
				<label class="col-sm-2 control-label">状态</label>
				<div class="col-sm-4">
					<s:if test="workHire.status=='noPublish'">
						草稿
					</s:if>
					<s:elseif test="workHire.status=='publishing'">
						招工中
					</s:elseif>
					<s:elseif test="workHire.status=='closed'">
						已关闭
					</s:elseif>
					<s:elseif test="workHire.status=='delete'">
						已删除
					</s:elseif>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">工种</label>
				<div class="col-sm-4">
					<s:select list="#{'壮工':'壮工','瓦工':'瓦工','抹灰工':'抹灰工','焊工':'焊工','钢筋工':'钢筋工','电工':'电工','建筑木工':'建筑木工','家装木工':'家装木工','架子工':'架子工','油漆工':'油漆工','腻子工':'腻子工','粘砖工':'粘砖工','彩钢':'彩钢','钢构工':'钢构工','水暖工':'水暖工','维修工':'维修工','防水工':'防水工','外墙保温':'外墙保温','高空作业工':'高空作业工','保洁工':'保洁工','各种司机':'各种司机','宣传员':'宣传员','促销员':'促销员','长期工':'长期工'}"
						listKey="key" listValue="value" name="workHire.workKind" value="workHire.workKind" id="workKind" emptyOption="true"></s:select>
				</div>
				<label class="col-sm-2 control-label">招工数量</label>
				<div class="col-sm-4">
					<s:textfield name="workHire.hireNum" id="hireNum" class="form-control"></s:textfield>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">工资</label>
				<div class="col-sm-10">
					<s:textarea name="workHire.salary" id="salary" class="form-control"></s:textarea>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">工作描述</label>
				<div class="col-sm-10">
					<s:textarea name="workHire.workDescri" id="descri" class="form-control"></s:textarea>
				</div>
			</div>
			<s:if test="permission=='cancelSign'">
				<div class="form-group">
					<label class="col-sm-2 control-label">联系电话</label>
					<div class="col-sm-10">
						<s:textfield name="workHire.workCompanyMobile" class="form-control"></s:textfield>
					</div>
				</div>
			</s:if>
			
			<div class="form-group">
				<div class="col-sm-12 col-md-offset-6">
					<s:if test="permission=='sign'">
						<button type="submit" class="btn btn-success" onclick="return workSign('<s:property value='workHire.id' />')">报名</button>
					</s:if>
					<s:if test="permission=='cancelSign'">
						<button type="submit" class="btn btn-success" onclick="return cancelWorkSign('<s:property value='workHire.id' />')">取消报名</button>
					</s:if>
				</div>
			</div>
		</fieldset>
	</form>
	
	<!-- 标签页 -->
	<ul id="myTab" class="nav nav-tabs">
		<li class="active"><a href="#divSign" onclick="showTab('divSign')" data-toggle="tab">报名列表</a></li>
		<li><a href="#divOpinion" onclick="showTab('divOpinion')" data-toggle="tab">处理记录</a></li>
	</ul>
	<div id="myTabContent" class="tab-content">
		<div class="tab-pane fade in active" id="divSign"></div>
		<div class="tab-pane fade" id="divOpinion"></div>
	</div>
</div>
<script type="text/javascript">

var mess = "${message}";
if(mess!=null && mess!="") {
	alert(mess);
}
function workSign(id) {
	document.getElementById("workHireId").value = id;
	var _url = "business/workSign.action";
	document.getElementById("editForm").action = _url;
	return true;
}

function cancelWorkSign(id) {
	document.getElementById("workHireId").value = id;
	var _url = "business/cancelWorkSign.action";
	document.getElementById("editForm").action = _url;
	return true;
}

var currentTab = "";
function showTab(tabId) {
	if(currentTab == tabId) {
		return;
	}
	currentTab = tabId;
	var _url = "";
	if(tabId=="divSign") {
		_url = "business/viewWorkSigns.action?workHireId=${workHire.id}";
	}else if(tabId=="divOpinion") {
		_url = "business/viewOpinions.action?workHireId=${workHire.id}";
	}
	queryByAjax(tabId, _url, "post", null);
}
showTab("divSign");
</script>
</BODY>
</html>
