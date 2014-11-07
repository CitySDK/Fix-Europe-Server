define([
	'jquery',
	'underscore',
	'backbone',
	'blockui'
], function ($, _, Backbone, BlockUI){
	var LoadingView = Backbone.View.extend({

		render: function () {
			$.blockUI({ message: 'Loading...please wait', css: { 
				border: 'none', 
	            padding: '15px', 
	            backgroundColor: '#000', 
	            'font-size': '20px',
	            '-webkit-border-radius': '10px', 
	            '-moz-border-radius': '10px', 
            	opacity: .5, 
            	color: '#fff' 
            } 
    		}); 

			return this;
		},

		remove: function () {
			$.unblockUI();
		}
	});

	return LoadingView;
});