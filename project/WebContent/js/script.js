"use strict";
var dateSlider; //Date Range Slider appearing in the page
var btnFilter = $("button.filter");
Date.prototype.yyyymmdd = function() { //Date formatter See http://stackoverflow.com/questions/3066586/get-string-in-yyyymmdd-format-from-js-date-object
	var yyyy = this.getFullYear().toString();
	var mm = (this.getMonth()+1).toString(); // Date.getMonth() is zero-based
	var dd  = this.getDate().toString();
	return yyyy + '-' + (mm.length==2?mm:"0"+mm) + '-' + (dd.length==2?dd:"0"+dd); // padding
};
var iframesrc;
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
		}
	});
	iframesrc = $("iframe#mapframe").attr('src');
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
		
		var confirmationDialogHeight = 260;
			
		var begin = new Date().getTime();
		var ajaxSuccessHandler = function (recordSize) { 
			var rand = Math.floor((Math.random()*1000000) + 1);
			//$("iframe#mapframe").attr('src', iframesrc + "?rand=" + rand); //Reload map iframe. Here we use an random number to force the hard reload.
			var iframe = document.getElementById('mapframe');
			iframe.src = iframesrc + "?rand=" + rand;
			var now = new Date().getTime();
			var interval = now - begin;
			interval /= 1000;
			$("div.notification-fadeout").html(recordSize + " records loaded in " + interval + " seconds.");
			$("div.notification-fadeout").show();
			setTimeout(function() {
				$("div.notification-fadeout").fadeOut(2000);
			}, 4000); //After 4 seconds, fade out the notification
		},
		ajaxStartHandler = function() {
			$("div.page-loading-mask").show();
			for (var i = 1; i <= 16; i++) {
				$('div.loader').append('<span class="block-' + i + '"></span>');
			}
		},
		ajaxFailHandler = function() {
			// This function should never be called.
			alert("Something went wrong while requesting TestServlet. Maybe there is a network problem.");
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
				confirmationDialog.html("Are you sure you want to load " + retJson.amount + "records?<br><br>" +
						"This might take a <em>VERY LONG</em> time.<br><br>" +
						"Maybe you want to add more filters or shorten the selected time interval.");
				confirmationDialog.dialog({ //See http://jqueryui.com/dialog/#modal-confirmation for the jQuery UI dialog options
					resizable: false,
					height: confirmationDialogHeight,
					modal: true,
					buttons: {
						"Yes, Load them" : function() {
							$(this).dialog("close");
							begin = getTime();
							loadUrl += "&confirmed=true"; // Add the `confirmed` parameter to the request,
							ajaxStartHandler(); //start making ajax call, mask the page to prevent any changes,
							$.ajax({ //and make the confirmed request again.
								url: loadUrl
							}).success(function(msg2) {
								var retJson2 = $.parseJSON(msg2);
								ajaxSuccessHandler(retJson2.amount);
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
				ajaxSuccessHandler(retJson.amount); //If there aren't too many records, we reload the map after the success of this filtering request.
			}
		}).fail(ajaxFailHandler)
		.complete(ajaxCompleteHandler);
	});
});