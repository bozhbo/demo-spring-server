
function login() {
	var account = $("#account").val();
	var password = $("#pass").val();
	
	$("#loginForm").submit();
	
}

function handlerLogin(result) {
	alert(result);
}