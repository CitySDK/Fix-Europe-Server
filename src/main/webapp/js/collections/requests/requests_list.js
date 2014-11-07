define([
	'underscore',
	'backbone',
	'models/requests/requests_model'
], function (_, Backbone, RequestModel){
	var RequestCollection = Backbone.Collection.extend({
		url: '/participation/rest/requests',
		model: RequestModel
	});

	return RequestCollection;
});