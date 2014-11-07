define([
	'jquery',
	'underscore',
	'backbone',
	'blockui'
], function ($, _, Backbone, BlockUI){
	var MessageView = Backbone.View.extend({
		
		renderMessage: function (message) {
			$.blockUI({ 
	            message: message, 
	            fadeIn: 700, 
	            fadeOut: 700, 
	            timeout: 1500, 
	            showOverlay: false, 
	            centerY: false, 
	            css: { 
	                width: '200px', 
	                top: '10px', 
	                left: '', 
	                right: '10px', 
	                border: 'none', 
	                padding: '10px', 
	                backgroundColor: '#000', 
	                '-webkit-border-radius': '10px', 
	                '-moz-border-radius': '10px', 
	                opacity: .6, 
	                'font-family': "'Droid Sans', sans-serif",
	                'font-size': '16px',
	                color: '#dcdcdc' 
	            }
	        });
		}
	});

	return MessageView;
});