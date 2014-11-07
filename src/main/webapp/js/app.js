define([
	'underscore',
	'backbone',
	'router',
], function (_, Backbone, Router){
	var initialize = function () {
		Backbone.View.prototype.close = function () {
			this.unbind();
    		this.remove();
    		if (this.onClose) {
        		this.onClose();
    		}
		};

		var local = localStorage.getItem('token');
		if (local) {
			var token = local.split("|");
			$.ajaxSetup({
				beforeSend: function(xhr, settings) {
				    xhr.setRequestHeader('Authorization', token[0]);
				}
			});
		}
		
		var pubSub = _.extend({}, Backbone.Events);
		Router.initialize({ pubSub: pubSub });
	};

	return {
		initialize: initialize
	};
});