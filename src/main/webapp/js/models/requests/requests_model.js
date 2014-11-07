define([
	'underscore',
	'backbone'
], function (_, Backbone){
	var RequestModel = Backbone.Model.extend({
		urlRoot: '/participation/rest/requests',
		defaults: {
			service_request_id: '',
			service_request_notice: ''
		}
	});

	return RequestModel;
});