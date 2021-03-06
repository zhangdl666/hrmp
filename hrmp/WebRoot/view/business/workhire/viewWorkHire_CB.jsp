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
<script type="text/javascript" src="js/common.js" charset="gbk"></script>
<script type="text/javascript" src="js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script type="text/javascript" src="js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
</head>

<BODY>
<%@include file="/header_nav.jsp"%>
<div class="container" id="bodyContainer">
		
	
	<form action="business/saveWorkHire.action" method="post" class="form-horizontal" id="editForm">
		<s:if test="workHire.id!=null">
			<s:hidden name="workHire.id"></s:hidden>
		</s:if>
		<s:hidden name="workHire.createTime">
			<s:param name="value"><s:date name="workHire.createTime" format="yyyy-MM-dd HH:mm:ss"></s:date></s:param>
		</s:hidden>
		<s:hidden name="workHire.businessNumber"></s:hidden>
		<s:hidden name="workHire.publisherId"></s:hidden>
		<s:hidden name="workHire.publisherName"></s:hidden>
		<s:hidden name="workHire.publisherCompanyId"></s:hidden>
		<s:hidden name="workHire.publisherCompanyName"></s:hidden>
		<s:hidden name="workHire.publishDate"></s:hidden>
		<s:hidden name="workHire.status" id="status"></s:hidden>
		<s:hidden name="workHire.empTypeId" id="empTypeId"></s:hidden>
		<s:hidden name="workHire.empDate" id="empTypeId"></s:hidden>
		<s:hidden name="empDateFlag" id="empDateFlag"></s:hidden>
		<fieldset>
        	<legend>
        		招工信息
					<font size="2px">温馨提示：如需帮助，请致电客服：0312-5080365</font>
        	</legend>
			<div class="form-group">
				<label class="col-sm-2 control-label">单号</label>
				<div class="col-sm-4">
					<s:property value="workHire.businessNumber"/>
				</div>
				<s:if test="permission=='write'">
				<label class="col-sm-2 control-label">发布人</label>
				<div class="col-sm-4">
					<s:property value="workHire.publisherCompanyName"/> &nbsp;&nbsp;
					<s:property value="workHire.publisherName"/>&nbsp;&nbsp;
					<s:date name="workHire.createTime" format="yyyy-MM-dd HH:mm:ss"></s:date>
				</div>
				</s:if>
				
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label">条件要求</label>
				<div class="col-sm-10">
					<s:textarea name="workHire.condition" id="descri" class="form-control"></s:textarea>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">工作描述</label>
				<div class="col-sm-10">
					<s:textarea name="workHire.workDescri" id="descri" class="form-control"></s:textarea>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">工作地址</label>
				<div class="col-sm-10">
					<s:textarea name="workHire.workArea" id="descri" class="form-control"></s:textarea>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">付款方式</label>
				<div class="col-sm-4">
					<s:checkbox name="workHire.payMode" id="payMode" onchange="showPayModeRemark()"></s:checkbox>面议
				</div>
				<div id="div_payModeRemark">
				<label class="col-sm-2 control-label">其他付款方式</label>
				<div class="col-sm-4" >
					<s:textfield name="workHire.payModeRemark" id="payModeRemark" class="form-control"></s:textfield>
				</div>
				</div>
			</div>
			
			
			<s:if test="permission=='write'">
				<s:hidden name="remark" id="remark"></s:hidden>
				<div class="form-group">
					<div class="col-sm-12 col-md-offset-6">
						<s:if test="workHire.status=='noPublish' || workHire.status=='publishing'">
							<button type="submit" class="btn btn-primary" id="saveBtn">保存</button>
						</s:if>
						<s:if test="workHire.status=='noPublish'">
							<s:if test="workHire.id!=null">
							<button type="submit" class="btn btn-success" onclick="return workHireDelete()">删除</button>
							</s:if>
							<button type="submit" class="btn btn-success" onclick="return workHirePublish(1)">马上发布</button>
						</s:if>
						<s:if test="workHire.status=='publishing'">
							<button type="submit" class="btn btn-success" onclick="return workHireClose()">关闭</button>
						</s:if>
						<button type="submit" class="btn btn-success" onclick="return workHireBadRecord()">添加违规</button>
					</div>
				</div>
			</s:if>
		</fieldset>
	</form>
	
	<!-- 标签页 -->
	<ul id="myTab" class="nav nav-tabs">
		<li class="active"><a href="#divSign" onclick="showTab('divSign')" data-toggle="tab">报名列表</a></li>
		<li><a href="#divOpinion" onclick="showTab('divOpinion')" data-toggle="tab">处理记录</a></li>
		<li><a href="#divOpinion" onclick="showTab('divBadRecord')" data-toggle="tab">违规记录</a></li>
	</ul>
	<div id="myTabContent" class="tab-content">
		<div class="tab-pane fade in active" id="divSign"></div>
		<div class="tab-pane fade" id="divOpinion"></div>
		<div class="tab-pane fade" id="divBadRecord"></div>
	</div>
</div>
<script type="text/javascript">

var mess = "${message}";
if(mess!=null && mess!="") {
	alert(mess);
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
          "workHire.salary":{
        	  validators: {
                  notEmpty: {
                      message: '工资不能为空'
                  }
              }
          },
          "workHire.workDescri": {
              validators: {
                  notEmpty: {
                      message: '工作描述不能为空'
                  }
              }
          },
          "workHire.workKind": {
              validators: {
                  notEmpty: {
                      message: '工种不能为空'
                  }
              }
          },
          "workHire.hireNum": {
              validators: {
                  notEmpty: {
                      message: '用工数量不能为空'
                  },
                  numeric:{
                	  message: '用工数量必须为数值'
                  }
              }
          }
      }
  });
}
$('.form_date').datetimepicker({
    language:  'zh-CN',
    weekStart: 1,
    todayBtn:  1,
	autoclose: 1,
	todayHighlight: 1,
	startView: 2,
	minView: 2,
	forceParse: 0
});

function preview(){
	var kind = document.getElementById("workKind").value;
	var hire = document.getElementById("hireNum").value;
	var s = document.getElementById("salary").value;
	var d = document.getElementById("descri").value;
	var str = kind + hire + "位，" + s + "，" + d;
	alert(str);
}
function workHirePublish(flag) {
	var s = document.getElementById("status");
	if(s.value == "publishing") {
		alert("已经发布，请勿重复发布！");
		return false;
	}
	var r = prompt("发布意见","");
	if(r==null) {
		return false;
	}
	document.getElementById("empDateFlag").value = flag;
	document.getElementById("remark").value = r;
	var _url = "business/publishWorkHire.action";
	document.getElementById("editForm").action = _url;
	return true;
}

function workHireDelete() {
	var s = document.getElementById("status");
	if(s.value == "delete") {
		alert("已经删除，请勿重复删除！");
		return false;
	}
	
	var r = prompt("删除意见","");
	if(r==null) {
		return false;
	}
	document.getElementById("remark").value = r;
	var _url = "business/deleteWorkHire.action";
	document.getElementById("editForm").action = _url;
	return true;
}

function workHireClose() {
	var s = document.getElementById("status");
	if(s.value == "closed") {
		alert("已经关闭，请勿重复关闭！");
		return false;
	}
	
	var r = prompt("关闭意见","");
	if(r==null) {
		return false;
	}
	document.getElementById("remark").value = r;
	var _url = "business/closeWorkHire.action";
	document.getElementById("editForm").action = _url;
	return true;
}

function workHireBadRecord() {
	var r = prompt("违规信息","");
	if(r==null) {
		return false;
	}
	document.getElementById("remark").value = r;
	var _url = "business/addBadRecordForWorkHire.action";
	document.getElementById("editForm").action = _url;
	return true;
}

//回访
function workHireVisit() {
	var _url = "business/viewWorkHireVisit.action?workHireId=${workHire.id}";
	window.showModalDialog(_url);
}

function showPayModeRemark(){
	var v = document.getElementById("payMode");
	if(v.checked){
		document.getElementById("div_payModeRemark").style.display="none";
		document.getElementById("payModeRemark").value = "";
	}else {
		document.getElementById("div_payModeRemark").style.display="block";
	}
}
//二次用工
function secondEmp() {
	var _url = "business/forwardToSecondEmp.action?workHireId=${workHire.id}";
	window.showModalDialog(_url);
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
	}else if(tabId=="divBadRecord") {
		_url = "business/viewBadRecords.action?workHireId=${workHire.id}";
	}else if(tabId=="divVisit") {
		_url = "business/viewVisits.action?workHireId=${workHire.id}";
	}
	queryByAjax(tabId, _url, "post", null);
}
$(document).ready(initValidator());
showPayModeRemark();
showTab("divSign");
</script>
</BODY>
</html>
