define([
	'jquery',
	'underscore',
	'backbone',
	'models/requests/requests_model',
	'collections/requests/requests_list',
	'views/stats/stats_view',
	'text!views/requests/requests.html'
], function ($, _, Backbone, RequestModel, RequestCollection, StatsView, requestListTemplate){
	var RequestListView = Backbone.View.extend({
		tagName: 'div id="reported"',

		initialize: function (requests) {
			this.requests = requests;
		},

		render: function () {
			var compiledTemplate = _.template(requestListTemplate, { requests: this.requests });
			$(this.el).html(compiledTemplate);
			return this;
		},

		renderStats: function (stats) {
			var statsView = new StatsView(stats);
			$('#reported').append(statsView.render().el);
		}
	});

	return RequestListView;
});