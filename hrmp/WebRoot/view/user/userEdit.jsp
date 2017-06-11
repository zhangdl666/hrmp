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

<title>用户编辑</title>
<!-- Bootstrap core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/bootstrapValidator.css" rel="stylesheet">
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
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/bootstrapValidator.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>
</head>

<BODY>
	<form action="organization/saveUser.action" method="post" class="form-horizontal" id="editForm">
		<s:hidden name="deptId"></s:hidden>
		<s:if test="user.id!=null">
			<s:hidden name="user.id"></s:hidden>
			<s:hidden name="user.pwd"></s:hidden>
			<s:hidden name="user.createTime"></s:hidden>
			<s:hidden name="user.pwdUpdateTime"></s:hidden>
			<s:hidden name="user.validstatus"></s:hidden>
		</s:if>
		<s:hidden name="user.deptId"></s:hidden>
		<fieldset>
        	<legend>基本信息</legend>
			<div class="form-group">
				<label class="col-md-2 control-label">登录名</label>
				<div class="col-md-4">
					<s:if test="user.id!=null">
						<s:hidden name="user.loginName"></s:hidden>
						<s:property value="user.loginName"/>
					</s:if>
					<s:else>
						<s:textfield name="user.loginName" id="loginName" class="form-control required"></s:textfield>
					</s:else>
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-2 control-label">姓名</label>
				<div class="col-md-4">
					<s:textfield name="user.userName" class="form-control"></s:textfield>
				</div>
			</div>
			<s:if test="user.id!=null">
			</s:if>
			<s:else>
				<div class="form-group">
						<label class="col-md-2 control-label">初始密码</label>
						<div class="col-md-4">
							<s:password name="user.pwd" class="form-control"></s:password>
						</div>
				</div>
				<div class="form-group">
						<label class="col-md-2 control-label">确认密码</label>
						<div class="col-md-4">
							<s:password name="user.confirmPwd" class="form-control"></s:password>
						</div>
				</div>
			
			</s:else>
			<div class="form-group">
				<label class="col-md-2 control-label">性别</label>
				<div class="col-md-4">
					<s:select class="form-control" list="#{'':'','M':'男','F':'女' }" listKey="key" listValue="value" name="user.gender" ></s:select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-2 control-label">用户分类</label>
				<div class="col-md-4">
					<s:select class="form-control" list="#{'':'','badCredit':'不良信用','Normal':'普通用户','VIP':'VIP用户' }" listKey="key" listValue="value" name="user.userKind"></s:select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-2 control-label">出生日期</label>
				<div class="col-md-4">
					<s:textfield name="user.birthday" id="datepickerstart" class="form-control">
						<s:param name="value">
							<s:date name="user.birthday" format="yyyy-MM-dd" />
						</s:param>
					</s:textfield>（YYYY-MM-DD）
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-2 control-label">手机号</label>
				<div class="col-md-4">
					<s:textfield name="user.mobile" class="form-control"></s:textfield>
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-2 control-label">工种</label>
				<div class="col-md-4">
					<s:textarea name="user.workKind" class="form-control"></s:textarea>
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-2 control-label">备注</label>
				<div class="col-md-4">
					<s:textarea name="user.remark" class="form-control"></s:textarea>
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-md-2 control-label">角色</label>
				<div class="col-md-4">
					<s:textarea name="roleNames" id="roleNames" class="form-control" readonly="true" onclick="selectRoles()"></s:textarea>
					<s:hidden name="roleIds" id="roleIds"></s:hidden>
				</div>
			</div>
			
			<div class="form-group">
				<div class="col-md-6 col-md-offset-2">
					<button type="button" class="btn btn-default" onclick="closeWin()">关闭</button>
					<button type="submit" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</fieldset>
	</form>
<script type="text/javascript">

function closeWin(){
	window.opener.queryUser();
	window.close();
}

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
    	  "user.loginName": {
              validators: {
                  notEmpty: {
                      message: '登录名不能为空'
                  },
                  regexp: {
                      regexp: /^[a-zA-Z0-9_]+$/,
                      message: '登录名只能包含大写、小写、数字和下划线'
                  }
              }
          },
          "user.userName": {
              validators: {
                  notEmpty: {
                      message: '姓名不能为空'
                  }
              }
          },
          "user.pwd": {
              validators: {
                  notEmpty: {
                      message: '初始密码不能为空'
                  },
                  identical: {
                      field: 'user.confirmPwd',
                      message: '初始密码与确认密码不一致'
                  }
              }
          },
          "user.confirmPwd": {
              validators: {
                  notEmpty: {
                      message: '确认密码不能为空'
                  },
                  identical: {
                      field: 'user.pwd',
                      message: '初始密码与确认密码不一致'
                  }
              }
          },
          "user.gender": {
              validators: {
                  notEmpty: {
                      message: '性别不能为空'
                  }
              }
          },
          "user.userKind": {
              validators: {
                  notEmpty: {
                      message: '用户分类不能为空'
                  }
              }
          },
          "user.birthday": {
              validators: {
                  notEmpty: {
                      message: '出生日期不能为空'
                  },
                  date: {
                      format: 'YYYY-MM-DD',
                      message: '日期格式不正确，应为YYYY-MM-DD格式。以2016年1月1日为例，应填写：2016-1-1'
                  }
              }
          },
          "user.mobile": {
              validators: {
                  notEmpty: {
                      message: '手机号不能为空'
                  },
                  numeric: {
                      message: '手机号必须为数字'
                  }
              }
          }
      }
  });
}

function initValidatorForExistsUser() {
	$('#editForm').bootstrapValidator({
      live: 'enabled',
      message: 'This value is not valid',
      feedbackIcons: {
          valid: 'glyphicon glyphicon-ok',
          invalid: 'glyphicon glyphicon-remove',
          validating: 'glyphicon glyphicon-refresh'
      },
      fields: {
          "user.userName": {
              validators: {
                  notEmpty: {
                      message: '姓名不能为空'
                  }
              }
          },
          "user.gender": {
              validators: {
                  notEmpty: {
                      message: '性别不能为空'
                  }
              }
          },
          "user.userKind": {
              validators: {
                  notEmpty: {
                      message: '用户分类不能为空'
                  }
              }
          },
          "user.birthday": {
              validators: {
                  notEmpty: {
                      message: '出生日期不能为空'
                  },
                  date: {
                      format: 'YYYY-MM-DD',
                      message: '日期格式不正确，应为YYYY-MM-DD格式。以2016年1月1日为例，应填写：2016-1-1'
                  }
              }
          },
          "user.mobile": {
              validators: {
                  notEmpty: {
                      message: '手机号不能为空'
                  },
                  numeric: {
                      message: '手机号必须为数字'
                  }
              }
          }
      }
  });
}

//显示角色树
function selectRoles() {
	var checkedIds = document.getElementById("roleIds").value;
	var dId = document.getElementById("deptId").value;
	var _url = "organization/selectRoles.action?deptId=" + dId + "&&checkedIds=" + checkedIds;
	var returnData = window.showModalDialog(_url,"dialogWidth=50px;dialogHeight=100px");
	if (!(returnData == undefined)){
        setParticipant(returnData, "roleIds", "roleNames");
    }
}

function setParticipant(val, id, name) {
    if (val != null && val != '') {
        var aRetValue = val.split(";");
        var ids = aRetValue[0];
        var names = aRetValue[1];
        document.getElementById(id).value = ids;
        document.getElementById(name).value = names;
    }
}

var uid = "${user.id}";
if(uid==null || uid=="") {
	$(document).ready(initValidator());
}else {
	$(document).ready(initValidatorForExistsUser());
}

</script>
</BODY>
</html>
