<html>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
<link rel="stylesheet" href="css/bootstrap-theme.min.css">
<link rel="stylesheet" href="css/bootstrap.min.css">

<!-- bootstrap JS include -->
<script src="js/jquery-2.2.3.min.js"></script>
<script src="js/bootstrap.min.js"></script>

<!-- dwr JS include -->
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>
<script type='text/javascript' src='dwr/interface/Login.js'></script>

<!-- webSocket-->
<script src="js/sockjs.min.js"></script>
<script src="js/stomp.js"></script>

<script src="js/mainPage.js"></script>

</head>
<body onload="init()">
	<nav class="navbar navbar-default navbar-static-top">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar" aria-expanded="false"
					aria-controls="navbar">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">存储服务管理界面</a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav">
					<li id="menu1" class="active"><a href="javascript:void(0);" onclick="mainPage()"><span class="glyphicon glyphicon-home"></span>&nbsp;主页</a></li>
					<li id="menu2"><a href="javascript:void(0);" onclick="monitorPage()"><span class="glyphicon glyphicon-facetime-video"></span>&nbsp;监控</a></li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</nav>
	
	<div id="mainPage" class="container">
		<div id="accessInfo">
			<div class="panel panel-info">
			  <div class="panel-heading"><p>角色信息:${role.name}</p>
			  </div>
			  <div id="accessInfoTable" class="panel-body">
			  	<form id="backForm" name="backForm" method="post" action="init">
			    	<a class="btn btn-primary" onclick="backLogin()">返回登录</a>
			    </form>
			  </div>
			</div>
		</div>
		
	</div>
    
</body>
</html>