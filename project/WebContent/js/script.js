"use strict";
var dateSlider; //Date Range Slider appearing in the page
var btnFilter = $("button.filter");
var btnToggleFilterForm = $("button.toggle-filter-form");
var btnToggleChart = $("button.toggle-chart");
Date.prototype.yyyymmdd = function () { //Date formatter See http://stackoverflow.com/questions/3066586/get-string-in-yyyymmdd-format-from-js-date-object
    var yyyy = this.getFullYear().toString();
    var mm = (this.getMonth()+1).toString(); // Date.getMonth() is zero-based
    var dd  = this.getDate().toString();
    return yyyy + '-' + (mm.length == 2 ? mm : "0" + mm) + '-' + (dd.length == 2 ? dd : "0" + dd); // padding
};
$(function () {
	//Display the map first, store the visualization into a var.
	var vis = cartodb.createVis('map', 'http://albusshin.cartodb.com/api/v2/viz/e43f2674-c941-11e4-bca1-0e4fddd5de28/viz.json');
	// Check http://ghusse.github.io/jQRangeSlider/options.html#optionsUsage 
	// for dateRangeSlider usage
	dateSlider = $("#slider").dateRangeSlider({
		
		bounds: {
			min: new Date(2014, 0, 1),
			max: new Date(2014, 11, 31)
		},
		defaultValues: {
			min: new Date(2014, 5, 1),
			max: new Date(2014, 5, 10)
		}
	});
	
	//Limit the both multiselect widgets under 5 selected options each
	var lastVehicleTypesSelection = null;
	$("select#vehicleTypes").change(function (event) {
		if ($(this).val().length > 5) {
			$(this).val(lastVehicleTypesSelection);
			$(this).multiSelect('refresh');
		} else {
			lastVehicleTypesSelection = $(this).val();
		}
	});
	var lastContributingFactorsSelection = null;
	$("select#contributingFactors").change(function (event) {
		if ($(this).val().length > 5) {
			$(this).val(lastContributingFactorsSelection);
			$(this).multiSelect('refresh');
		} else {
			lastContributingFactorsSelection = $(this).val();
		}
	});
	
	// Check http://loudev.com/ for this jQuery UI Multiselect plugin
	$("select#vehicleTypes").multiSelect();
	$("select#contributingFactors").multiSelect();
	
	// Wait for a while for the dateRangeSlider to initialize, then hide the slider
	setTimeout(function() {
		$("div#slider").fadeToggle(200);
	}, 500);
	$("div.filter-form").hide();
	
	var toggleFilterForm = function () {
		$("div#slider").fadeToggle(200);
		$("div.filter-form").fadeToggle(200);
	};
	btnToggleFilterForm.click(toggleFilterForm);

	btnToggleChart.click(function() {
		$("div#chart-canvas-wrapper").toggleClass("displayed");
	});
	
	
	var ctx = document.getElementById("chartCanvas").getContext("2d");
	var myBarChart = null;
	
	btnFilter.click(function () {
		var dateValues = dateSlider.dateRangeSlider("values"),
			minDate = dateValues.min,
			maxDate = dateValues.max;
		
		//Build loadUrl to get the records from our Servlet
		//With the options the user specified in the filter form and the date slider
		var loadUrl =  "ShowRecordsOnMap?start=" + minDate.yyyymmdd() + "&end=" + maxDate.yyyymmdd();
			loadUrl += "&withDeaths=" + $("#withdeaths").is(":checked");
			loadUrl += "&withInjuries=" + $("#withinjuries").is(":checked");
			loadUrl += "&withPedestriansInvolved=" + $("#withpedestriansinvolved").is(":checked");
			loadUrl += "&withCyclistsInvolved=" + $("#withcyclistsinvolved").is(":checked");
			loadUrl += "&withMotoristsInvolved=" + $("#withmotoristsinvolved").is(":checked");
			
		var vehicleTypes = $("select#vehicleTypes").val();
		if (vehicleTypes !== null) loadUrl += "&vehicleTypes=" + vehicleTypes;
		var contributingFactors = $("select#contributingFactors").val();
		if (contributingFactors !== null) loadUrl += "&contributingFactors=" + contributingFactors;
		loadUrl += "&vehiclesInvolved=" + $("select#vehiclesInvolved").val();
		
		console.log(loadUrl);
		
		var confirmationDialogHeight = 260;
			
		var begin = new Date().getTime();
		var ajaxSuccessHandler = function (retJson) { 
			toggleFilterForm(); //Hide the filter form
			setTimeout(function(){vis.getLayers()[1].invalidate();}, 500);
			var now = new Date().getTime();
			var interval = now - begin;
			interval /= 1000;
			$("div.notification-fadeout").html(retJson.amount + " records loaded in " + interval + " seconds.");
			$("div.notification-fadeout").show();
			setTimeout(function() {
				$("div.notification-fadeout").fadeOut(2000);
			}, 4000); //After 4 seconds, fade out the notification
			
			//Set chart data
			var chartData = {
					labels: ["Persons",  "Pedestrians", "Cyclists", "Motorists"],
					datasets: [{
						label: "Killed",
			            fillColor: "rgba(151,187,205,0.5)",
			            strokeColor: "rgba(151,187,205,0.8)",
			            highlightFill: "rgba(151,187,205,0.75)",
			            highlightStroke: "rgba(151,187,205,1)",
			            data: [retJson.killedPersons, retJson.killedCyclists, retJson.killedPedestrians, retJson.killedMotorists]
					}, {
			            label: "Injured",
			            fillColor: "rgba(220,220,220,0.5)",
			            strokeColor: "rgba(220,220,220,0.8)",
			            highlightFill: "rgba(220,220,220,0.75)",
			            highlightStroke: "rgba(220,220,220,1)",
		                data: [retJson.injuredPersons , retJson.injuredCyclists, retJson.injuredPedestrians, retJson.killedMotorists]
					}]
			};
			
			//See http://www.chartjs.org/docs/#bar-chart-introduction for Chartjs Bar Chart configurations
			//Destroy the previous Bar Chart before drawing a next one
			if (myBarChart !== null) myBarChart.destroy();
			myBarChart = new Chart(ctx).Bar(chartData, {
			    scaleBeginAtZero : true,
			    scaleShowGridLines : true,
			    scaleGridLineColor : "rgba(0,0,0,.05)",
			    scaleGridLineWidth : 1,
			    scaleShowHorizontalLines: true,
			    scaleShowVerticalLines: true,
			    barShowStroke : true,
			    barStrokeWidth : 2,
			    barValueSpacing : 5,
			    barDatasetSpacing : 1,
			    legendTemplate : "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<datasets.length; i++){%><li><span style=\"background-color:<%=datasets[i].fillColor%>\"></span><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>",
			    multiTooltipTemplate: "<%= datasetLabel %>: <%= value %>"
			});
		},
		ajaxStartHandler = function() {
			$("div.page-loading-mask").show();
			for (var i = 1; i <= 16; i++) {
				$('div.loader').append('<span class="block-' + i + '"></span>');
			}
		},
		ajaxFailHandler = function() {
			// This function should never be called.
			alert("The ajax request failed. Something went wrong. Maybe there is a problem with your database connection.");
		},
		ajaxCompleteHandler = function() {
			$("div.page-loading-mask").hide();
		};
		
		ajaxStartHandler(); //Start making ajax call, and mask the page to prevent any changes.
		$.ajax({
			url: loadUrl
		}).success(function(msg) {
			var retJson = $.parseJSON(msg);
			if (retJson.exceeded) {
				var confirmationDialog = $("div.confirmation-dialog");
				confirmationDialog.html("Are you sure you want to load " + retJson.amount + " records?<br><br>" +
						"This might take a <em>VERY LONG</em> time.<br><br>" +
						"Maybe you want to add more filters or shorten the selected time interval.");
				confirmationDialog.dialog({ //See http://jqueryui.com/dialog/#modal-confirmation for the jQuery UI dialog options
					resizable: false,
					height: confirmationDialogHeight,
					modal: true,
					buttons: {
						"Yes, Load Them Any Way" : function() {
							$(this).dialog("close");
							begin = getTime();
							loadUrl += "&confirmed=true"; // Add the `confirmed` parameter to the request,
							ajaxStartHandler(); //start making ajax call, mask the page to prevent any changes,
							$.ajax({ //and make the confirmed request again.
								url: loadUrl
							}).success(function(msg2) {
								var retJson2 = $.parseJSON(msg2);
								ajaxSuccessHandler(retJson2);
							})
							.fail(ajaxFailHandler)
							.complete(ajaxCompleteHandler);
						},
						Cancel: function() {
							$(this).dialog("close");
						}
					}
				});
			} else {
				ajaxSuccessHandler(retJson); //If there aren't too many records, we reload the map after the success of this filtering request.
			}
		}).fail(ajaxFailHandler)
		.complete(ajaxCompleteHandler);
	});
});