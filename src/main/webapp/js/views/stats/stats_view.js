define([
	'jquery',
	'underscore',
	'backbone',
	'models/stats/stats_model',
	'text!views/stats/stats.html'
], function ($, _, Backbone, StatsModel, statsTemplate){
	var StatsView = Backbone.View.extend({
		tagName: 'div id="statistics"', 

		initialize: function (stats) {
			this.stats = stats;
		},

		render: function () {
			var compiledTemplate = _.template(statsTemplate, this.stats);
			$(this.el).html(compiledTemplate);
			return this;
		}
	});

	return StatsView;
});