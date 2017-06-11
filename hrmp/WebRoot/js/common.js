function queryByAjax(selector, _url, requestType, params) {
	if (!selector) {
		return;
	}
	selector = "#" + selector;
	var container = $(selector);

	if (!_url) {
		container.html("url错误，没有请求到内容");
		return;
	}
	if (requestType == "") {
		requestType = "get";
	}
	container.html("正在加载...，请稍候");
	$.ajax({
		url : _url,
		dataType : "html",
		data : params,
		cache : false,
		error : function(data) {
			alert("请求错误");
			container.html("加载失败！");
		},
		type : requestType,
		success : function(data) {
			// 取body.html
			container.html(data);
		}
	});
};