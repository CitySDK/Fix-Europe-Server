define([
	'jquery',
	'underscore',
	'backbone',
	'blockui',
	'models/stats/stats_model',
	'collections/requests/requests_list',
	'views/requests/requests_view',
	'views/stats/stats_view',
	'views/layout/loading'
], function ($, _, Backbone, BlockUI, StatsModel, RequestCollection, RequestListView, StatsView, LoadingView){
	var HomeView = Backbone.View.extend({
		tagName: 'div id="home"',

		initialize: function (options) {
			this.pubSub = options.pubSub;
		},

		render: function () {
			var loadingView = new LoadingView();
			loadingView.render();

			var self = this;
			var requestCollection = new RequestCollection();
			requestCollection.fetch({
				data: $.param({ limit: 6 }),
				success: function (response, collection) {
					var requestListView = new RequestListView(collection.requests);
					$('#content').append('<h2>Recently reported incidents</h2>');
					$('#content').append(requestListView.render().el);

					var statsView = new StatsView(collection._embedded.statistics);
					$('#content').append(statsView.render().el);
					
					loadingView.remove();
				},
				error: function (model, xhr) {
					if (xhr.status == 401) {
						loadingView.remove();
						localStorage.removeItem('token');
						self.pubSub.trigger('sign-out');
					}
				}
			});

			return this;
		}
	});

	return HomeView;
});