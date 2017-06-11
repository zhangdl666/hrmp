<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="utf-8"%>
<nav class="navbar navbar-default navbar-fixed-top">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#navbar" aria-expanded="false"
				aria-controls="navbar">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#">BD</a>
		</div>
		<div id="navbar" class="collapse navbar-collapse">
			<ul class="nav navbar-nav">
				<li><a href="security/index.action">首页</a></li>

				<!-- 循环展示菜单 -->
				<s:iterator value="#session.menuList" status="st">
					<s:if test="childMenus!=null && childMenus.size>0">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown" role="button" aria-haspopup="true"
							aria-expanded="false"><s:property value="menuName" /> <span
								class="caret"></span> </a>
							<ul class="dropdown-menu">
								<s:iterator value="childMenus" status="st2">
									<li>
										<a href='<s:property value="menuUrl"/>'><s:property value="menuName" /> </a>
									</li>
									<!-- <li role="separator" class="divider"></li> -->
								</s:iterator>
							</ul></li>
					</s:if>
					<s:else>
						<li>
							<a href='<s:property value="menuUrl"/>'><s:property value="menuName" /> </a>
						</li>
					</s:else>
				</s:iterator>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li><a href="../navbar/"><s:property
							value="#session.currentUsername" /> </a>
				</li>
				<li><a href="security/logout.action">退出</a>
				</li>
			</ul>
		</div>
		<!--/.nav-collapse -->
	</div>
</nav>

