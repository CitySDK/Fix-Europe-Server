define([
	'underscore',
	'backbone'
], function (_, Backbone){
	var StatsModel = Backbone.Model.extend({
		urlRoot: '/participation/rest/stats/',
		defaults: {
			reported_today: -1,
			reported_week: -1,
			closed_week: -1,
			closed_month: -1
		}
	});

	return StatsModel;
});