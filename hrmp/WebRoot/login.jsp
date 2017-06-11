<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="utf-8"%>
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

<title>登录</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="icon" href="image/favicon.ico">
<!-- Bootstrap core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<link href="css/ie10-viewport-bug-workaround.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="css/login.css" rel="stylesheet">

<!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
<!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
<script src="js/ie-emulation-modes-warning.js"></script>
<script src="js/jsencrypt.min.js"></script>

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>
	<div class="container">

		<form class="form-signin" id="loginForm" action="security/login.action">
			<h2 class="form-signin-heading">登录</h2>
			<label for="inputEmail" class="sr-only">用户名</label> 
			<input type="text" id="username" name="username" value="${username }" class="form-control" placeholder="用户名" required autofocus> 
			<label for="inputPassword" class="sr-only">密码</label> 
			<input type="password" id="password" name="password" class="form-control" placeholder="密码" required>
			<!-- 
        <div class="checkbox">
          <label>
            <input type="checkbox" value="remember-me"> Remember me
          </label>
        </div>
         -->
			<button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
			<font color="red">${message }</font>
		</form>

	</div>
	<!-- /container -->


	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script src="js/ie10-viewport-bug-workaround.js"></script>

	<script type="text/javascript">
		//获取public key
		var publicKey = null;
		function getPublicKey() {
			//新建一个XHR对象，只支持IE7以上的浏览器，或者其他浏览器，我用的是谷歌浏览器
			var xhr = new XMLHttpRequest();
			//需要访问的链接
			var urlString = "security/passwordEncrypt.action";
			xhr.open("post", urlString, true);
			xhr.send(null);

			//检查响应的状态   
			xhr.onreadystatechange = function() {
				if (xhr.readyState == 4) {
					if ((xhr.status >= 200 && xhr.status < 300)
							|| xhr.status == 304) {
						if (xhr.responseText != null) {
							publicKey = xhr.responseText;
						}
					}
				}
			};

		}
		function encryptPassword(event) {
			//对密码进行加密
			var encrypt = new JSEncrypt();
			encrypt.setPublicKey(publicKey);
			var password = document.getElementById("password").value;
			document.getElementById("password").value = encrypt
					.encrypt(password);

			//提交之前，检查是否已经加密。假设用户的密码不超过20位，加密后的密码不小于20位
			var password = document.getElementById("password").value;
			if (password.length < 20) {
				//实际提示，可以换一种说法
				alert("请刷新页面重新登录");
				//若没有加密，阻止提交
				event.preventDefault();
			}
		}
		//页面加载后请求publicKey
		getPublicKey();
		var loginForm = document.getElementById("loginForm");
		//提交时，进行加密
		loginForm.addEventListener("submit", encryptPassword, false);
	</script>
</body>
</html>
