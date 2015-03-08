<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Traffic Collision Mapping Tutorial - HP Haven</title>
<!-- Bootstrap, jQuery, and Chart.js -->
<script src="js/jquery-1.11.2.js"></script>
<script src="js/Chart.js"></script>

<link rel="stylesheet" href="js/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="js/bootstrap/css/bootstrap-theme.min.css">
<script src="js/bootstrap/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="style.css">
</head>
<body>
		<nav class="navbar navbar-default">
			<div class="container-fluid">
				<div class="navbar-header">
					<div class="navbar-brand">Traffic Collision Mapping Tutorial</div>
				</div>
				<div class="nav navbar-nav navbar-right">
					<form action="" method="GET" class="form-inline">
						<div class="form-group">
							<input type="text" class="form-control" name="tag" id="tag" placeholder="Search Tag">
							<input type="number" class="form-control" name="amount" step='1' value="50" />
							<button type="submit" class="btn btn-default">Search</button>
						</div>
					</form>
				</div>
			</div>
		</nav>
		
		<div class="container">
		</div>
</body>
</html>