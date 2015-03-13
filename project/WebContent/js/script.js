"use strict";
var dateSlider; //Date Range Slider appearing in the page
	//TODO months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"]; //Months display
var btnFilter = $("button.filter");
Date.prototype.yyyymmdd = function() { //Date formatter See http://stackoverflow.com/questions/3066586/get-string-in-yyyymmdd-format-from-js-date-object
	var yyyy = this.getFullYear().toString();
	var mm = (this.getMonth()+1).toString(); // getMonth() is zero-based
	var dd  = this.getDate().toString();
	return yyyy + '-' + (mm.length==2?mm:"0"+mm) + '-' + (dd.length==2?dd:"0"+dd); // padding
};
$(function (){
	
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
		},
		range: {
			min: {days: 1},
			max: {days: 15}
		}
	});
	
	btnFilter.click(function () {
		var dateValues = dateSlider.dateRangeSlider("values"),
			minDate = dateValues.min,
			maxDate = dateValues.max;
		
			var loadUrl =  "TestServlet?start=" + minDate.yyyymmdd() + "&end=" + maxDate.yyyymmdd();
			loadUrl += "&withDeaths=" + $("#withdeaths").is(":checked");
			loadUrl += "&withInjuries=" + $("#withinjuries").is(":checked");
			loadUrl += "&withPedestriansInvolved=" + $("#withpedestriansinvolved").is(":checked");
			loadUrl += "&withCyclistsInvolved=" + $("#withcyclistsinvolved").is(":checked");
			loadUrl += "&withMotoristsInvolved=" + $("#withmotoristsinvolved").is(":checked");
			
			console.log(loadUrl);
			
		$.ajax({
			url: loadUrl
		}).success(function() {
			console.log("Load success");
			$("iframe#mapframe").attr('src', function(i, val) {return val;}); //Reload map iframe. See http://stackoverflow.com/a/4249946/1831275
		});
	});
	
	console.log($("iframe#mapframe").contents());
	/*
	.click(function() {
		var poppedUpContent = $("p", $("div.carttodb-popup-content", $("div.cartodb-popup-content-wrapper")));
		console.log(poppedUpContent.html());
	});
	*/
});