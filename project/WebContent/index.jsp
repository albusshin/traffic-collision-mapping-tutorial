<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Traffic Collision Mapping Tutorial - HP Haven</title>
<!-- Bootstrap, jQuery, and Chart.js -->
<script src="js/jquery-1.11.2.js"></script>
<script src="js/jquery-ui-1.11.3/jquery-ui.min.js"></script>
<script src="js/jQRangeSlider-5.7.1/jQRangeSlider-min.js"></script>
<script src="js/jQRangeSlider-5.7.1/jQAllRangeSliders-min.js"></script>
<script src="js/bootstrap/js/bootstrap.min.js"></script>
<script src="js/Chart.js"></script>

<link rel="stylesheet" href="js/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="js/bootstrap/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="js/jquery-ui-1.11.3/jquery-ui.min.css"></script>
<link rel="stylesheet" href="js/jQRangeSlider-5.7.1/css/iThing-min.css">
<link rel="stylesheet" href="screen.css">
</head>
<body>
	<div class="page-loading-mask" hidden="hidden">
		<!-- The page loading mask -->
		<div class="top">
		  <div class="square">
		    <div class="square">
		      <div class="square">
		        <div class="square">
		          <div class="square"><div class="square">
		
		          </div></div>
		        </div>
		      </div>
		    </div>
		  </div>
		</div>
		<div class="bottom">
		  <div class="square">
		    <div class="square">
		      <div class="square">
		        <div class="square">
		          <div class="square"><div class="square">
		          </div></div>
		        </div>
		      </div>
		    </div>
		  </div>
		</div>
		<div class="left">
		  <div class="square">
		    <div class="square">
		      <div class="square">
		        <div class="square">
		          <div class="square"><div class="square">
		          </div></div>
		        </div>
		      </div>
		    </div>
		  </div>
		</div>
		<div class="right">
		  <div class="square">
		    <div class="square">
		      <div class="square">
		        <div class="square">
		          <div class="square"><div class="square">
		          </div></div>
		        </div>
		      </div>
		    </div>
		  </div>
		</div>
	</div>
	
	<nav class="navbar navbar-default">
	<div class="container-fluid">
		<div class="navbar-header">
			<div class="navbar-brand">Traffic Collision Mapping Tutorial</div>
		</div>
	</div>
	</nav>

	<div id="slider"></div>
	<div class="filter-form">
		<label><input type="checkbox" id="withdeaths" name="withdeaths" placeholder="with deaths"> With Deaths</label>
		<label><input type="checkbox" id="withinjuries" name="withinjuries"> With Injuries</label>
		<label><input type="checkbox" id="withpedestriansinvolved" name="withpedestriansinvolved"> With Pedestrians Involved</label>
		<label><input type="checkbox" id="withcyclistsinvolved" name="withcyclistsinvolved"> With Cyclists Involved</label>
		<label><input type="checkbox" id="withmotoristsinvolved" name="withmotoristsinvolved"> With Motorists Involved</label>
	</div>

	<button class="btn btn-pretty btn-success filter">Filter</button>
	
	<div class="confirmation-dialog" title="Warning: too many records" hidden="hidden">
	</div>
	
	<div class="notification-fadeout" hidden="hidden"></div>
	
	<iframe id="mapframe" width='100%' height='720' frameborder='0'
		src='http://albusshin.cartodb.com/viz/e43f2674-c941-11e4-bca1-0e4fddd5de28/embed_map'
		allowfullscreen webkitallowfullscreen mozallowfullscreen
		oallowfullscreen msallowfullscreen></iframe>

	<script src="js/script.js"></script>
</body>
</html>