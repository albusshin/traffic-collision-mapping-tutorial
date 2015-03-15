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
<script src="js/lou-multi-select/js/jquery.multi-select.js"></script>
<script src="js/bootstrap/js/bootstrap.min.js"></script>
<script src="js/Chart.js"></script>
<script src="js/cartodb.js"></script>

<link rel="stylesheet" href="js/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="js/bootstrap/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="js/jquery-ui-1.11.3/jquery-ui.min.css"></script>
<link rel="stylesheet" href="js/jQRangeSlider-5.7.1/css/iThing-min.css">
<link rel="stylesheet" href="js/lou-multi-select/css/multi-select.css">
<!-- This following css file must be loaded from CDN, because it loads some assets from the cartodb server -->
<link rel="stylesheet" href="http://libs.cartocdn.com/cartodb.js/v3/3.12/themes/css/cartodb.css" />

<link rel="stylesheet" href="css/screen.css">
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
			<div class="nav navbar-nav navbar-right">
				<button class="btn btn-primary toggle-filter-form">Toggle Fiter Form</button>
				<button class="btn btn-default toggle-chart">Toggle Chart</button>
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
		<label for="vehiclesInvolved">Vehicles Involved</label>
		<select name="vehiclesInvolved" id="vehiclesInvolved" width="300px">
			<option value="-1" selected></option>
			<option value="0">0</option>
			<option value="1">1</option>
			<option value="2">2</option>
			<option value="3">3</option>
			<option value="4">4</option>
			<option value="5">5</option>
		</select>
		<label for="vehicleTypes">Vehicle Types (Select no more than 5 options)</label>
		<select name="vehicleTypes" id="vehicleTypes" multiple>
			<option value="VAN">Van</option>
			<option value="SMALL COM VEH(4 TIRES)">Small company vehicle (4 tires)</option>
			<option value="SCOOTER">Scooter</option>
			<option value="SPORT UTILITY / STATION WAGON">Sport utility / Station Wagon</option>
			<option value="BUS">Bus</option>
			<option value="FIRE TRUCK">Fire truck</option>
			<option value="LARGE COM VEH(6 OR MORE TIRES)">Large company vehicle (6 or more tires)</option>
			<option value="LIVERY VEHICLE">Livery Vehicle</option>
			<option value="BICYCLE">Bicycle</option>
			<option value="PICK-UP TRUCK">Pick-up truck</option>
			<option value="PEDICAB">Pedicab</option>
			<option value="TAXI">Taxi</option>
			<option value="AMBULANCE">Ambulance</option>
			<option value="PASSENGER VEHICLE">Passenger Vehicle</option>
			<option value="MOTORCYCLE">Motorcycle</option>
			<option value="UNKNOWN">Unknown</option>
			<option value="OTHER">Other</option>
		</select>
		
		<label for="contributingFactors">Contributing Factors (Select no more than 5 options)</label>
		<select name="contributingFactors" id="contributingFactors" multiple>
			<option value="Failure to Yield Right-of-Way">Failure to Yield Right-of-Way</option>
			<option value="Illness">Illness</option>
			<option value="Pavement Slippery">Pavement Slippery</option>
			<option value="Lane Marking Improper/Inadequate">Lane Marking Improper/Inadequate</option>
			<option value="Oversized Vehicle">Oversized Vehicle</option>
			<option value="Unsafe Lane Changing">Unsafe Lane Changing</option>
			<option value="Backing Unsafely">Backing Unsafely</option>
			<option value="Passenger Distraction">Passenger Distraction</option>
			<option value="Headlights Defective">Headlights Defective</option>
			<option value="Other Lighting Defects">Other Lighting Defects</option>
			<option value="Drugs (Illegal)">Drugs (Illegal)</option>
			<option value="Driver Inexperience">Driver Inexperience</option>
			<option value="Cell Phone (hands-free)">Cell Phone (hands-free)</option>
			<option value="Prescription Medication">Prescription Medication</option>
			<option value="Unspecified">Unspecified</option>
			<option value="Unsafe Speed">Unsafe Speed</option>
			<option value="Reaction to Other Uninvolved Vehicle">Reaction to Other Uninvolved Vehicle</option>
			<option value="Obstruction/Debris">Obstruction/Debris</option>
			<option value="Lost Consciousness">Lost Consciousness</option>
			<option value="Following Too Closely">Following Too Closely</option>
			<option value="Traffic Control Device Improper/Non-Working">Traffic Control Device Improper/Non-Working</option>
			<option value="Fell Asleep">Fell Asleep</option>
			<option value="Accelerator Defective">Accelerator Defective</option>
			<option value="Traffic Control Disregarded">Traffic Control Disregarded</option>
			<option value="Physical Disability">Physical Disability</option>
			<option value="Pavement Defective">Pavement Defective</option>
			<option value="Steering Failure">Steering Failure</option>
			<option value="Passing or Lane Usage Improper">Passing or Lane Usage Improper</option>
			<option value="Other Vehicular">Other Vehicular</option>
			<option value="Driver Inattention/Distraction">Driver Inattention/Distraction</option>
			<option value="Tire Failure/Inadequate">Tire Failure/Inadequate</option>
			<option value="Failure to Keep Right">Failure to Keep Right</option>
			<option value="Windshield Inadequate">Windshield Inadequate</option>
			<option value="View Obstructed/Limited">View Obstructed/Limited</option>
			<option value="Outside Car Distraction">Outside Car Distraction</option>
			<option value="Tow Hitch Defective">Tow Hitch Defective</option>
			<option value="Glare">Glare</option>
			<option value="Pedestrian/Bicyclist/Other Pedestrian Error/Confusion">Pedestrian/Bicyclist/Other Pedestrian Error/Confusion</option>
			<option value="Other Electronic Device">Other Electronic Device</option>
			<option value="Turning Improperly">Turning Improperly</option>
			<option value="Alcohol Involvement">Alcohol Involvement</option>
			<option value="Shoulders Defective/Improper">Shoulders Defective/Improper</option>
			<option value="Brakes Defective">Brakes Defective</option>
			<option value="Cell Phone (hand-held)">Cell Phone (hand-held)</option>
			<option value="Fatigued/Drowsy">Fatigued/Drowsy</option>
			<option value="Aggressive Driving/Road Rage">Aggressive Driving/Road Rage</option>
			<option value="Animals Action">Animals Action</option>
		</select>
		<button class="btn btn-large btn-success filter">Filter</button>
	</div>
	
	<div class="confirmation-dialog" title="Warning: too many records" hidden="hidden"></div>
	
	<div class="notification-fadeout" hidden="hidden"></div>
	
	<div id="map"></div>
	
	<div id="chart-canvas-wrapper"><canvas id="chartCanvas" width="800" height="600"></canvas></div>

	<script src="js/script.js"></script>
</body>
</html>