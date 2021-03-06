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
		<fieldset>
        	<legend>
        		招工信息
        		<s:if test="workHire.id!=null">
					<font color="red">报名要诚信，如审核通过后不去，系统将自动关闭以后的报名权限！</font>
				</s:if>
        	</legend>
			<div class="form-group">
				<label class="col-sm-2 control-label">单号</label>
				<div class="col-sm-1">
					<s:property value="workHire.businessNumber"/>
				</div>
				<label class="col-sm-2 control-label">发布人</label>
				<div class="col-sm-3">
					<s:property value="workHire.publisherCompanyName"/> &nbsp;&nbsp;
					<s:property value="workHire.publisherName"/>&nbsp;&nbsp;
					<s:date name="workHire.createTime" format="yyyy-MM-dd HH:mm:ss"></s:date>
				</div>
				
				<label class="col-sm-2 control-label">状态</label>
				<div class="col-sm-2">
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
			<s:if test="permission=='write'">
				<div class="form-group">
					<label class="col-sm-2 control-label">联系电话</label>
					<div class="col-sm-10">
						<s:textfield name="workHire.workCompanyMobile" class="form-control"></s:textfield>
					</div>
				</div>
			</s:if>
			
			<s:if test="permission=='write'">
				<s:hidden name="remark" id="remark"></s:hidden>
				<div class="form-group">
					<div class="col-sm-12 col-md-offset-6">
						<button type="button" class="btn btn-success" onclick="preview()">内容预览</button>
						<s:if test="workHire.status=='noPublish' || workHire.status=='publishing'">
							<button type="submit" class="btn btn-primary" id="saveBtn">保存</button>
						</s:if>
						<s:if test="workHire.status=='noPublish'">
							<button type="submit" class="btn btn-success" onclick="return workHireDelete()">删除</button>
							<button type="submit" class="btn btn-success" onclick="return workHirePublish()">发布</button>
						</s:if>
						<s:if test="workHire.status=='publishing'">
							<button type="submit" class="btn btn-success" onclick="return workHireClose()">关闭</button>
						</s:if>
						<s:if test="workHire.status=='closed'">
							<button type="button" class="btn btn-success" onclick="workHireVisit()">回访</button>
							<button type="button" class="btn btn-success" onclick="secondEmp()">二次用工</button>
						</s:if>
					</div>
				</div>
			</s:if>
		</fieldset>
	</form>
	
	<!-- 标签页 -->
	<ul id="myTab" class="nav nav-tabs">
		<li class="active"><a href="#divSign" onclick="showTab('divSign')" data-toggle="tab">报名列表</a></li>
		<li><a href="#divOpinion" onclick="showTab('divOpinion')" data-toggle="tab">处理记录</a></li>
		<li><a href="#divVisit" onclick="showTab('divVisit')" data-toggle="tab">回访记录</a></li>
	</ul>
	<div id="myTabContent" class="tab-content">
		<div class="tab-pane fade in active" id="divSign"></div>
		<div class="tab-pane fade" id="divOpinion"></div>
		<div class="tab-pane fade" id="divVisit"></div>
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
function workHirePublish() {
	var s = document.getElementById("status");
	if(s.value == "publishing") {
		alert("已经发布，请勿重复发布！");
		return false;
	}
	var r = prompt("发布意见","");
	if(r==null) {
		return false;
	}
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

//回访
function workHireVisit() {
	var _url = "business/viewWorkHireVisit.action?workHireId=${workHire.id}";
	window.showModalDialog(_url);
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
	}else if(tabId=="divVisit") {
		_url = "business/viewVisits.action?workHireId=${workHire.id}";
	}
	queryByAjax(tabId, _url, "post", null);
}
$(document).ready(initValidator());
showTab("divSign");
</script>
</BODY>
</html>
