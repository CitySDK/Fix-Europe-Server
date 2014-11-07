define([
	'jquery',
	'underscore',
	'backbone',
	'oauth/gapi-custom',
	'collections/requests/requests_list',
	'views/requests/requests_view',
	'collections/layout/options',
	'views/layout/message',
	'views/layout/menu',
	'views/layout/signin',
	'views/layout/home'
], function ($, _, Backbone, GapiManagerFactory, RequestCollection, RequestListView, OptionsCollection, MessageView, MenuView, SigninView, HomeView){
		var AppRouter = Backbone.Router.extend({
			routes: {
				'': 'signIn',
				'signin': 'signIn',
				'home': 'showHome',
				'requests?t=my': 'showRequests',
				'requests?t=all': 'allRequests',
				'signout': 'signOut'
			},

			initialize: function (options) {
				this.options = options;

				_.bindAll(this, "triggerSignIn");
				_.bindAll(this, "triggerSignOut");
				this.options.pubSub.bind("sign-in", this.triggerSignIn);
				this.options.pubSub.bind("sign-out", this.triggerSignOut);
			},

			showRequests: function () {
				requests(localStorage.getItem('user'));
				this.options.pubSub.trigger('highlight', '.my');
			},

			allRequests: function () {
				requests();
				this.options.pubSub.trigger('highlight', '.all');
			},

			triggerSignIn: function (token, data, tokenType) {
				localStorage.setItem('token', token + "|" + tokenType);
				
				if(data == null || data == undefined) {
					var encodedData = window.atob(token).split(":")[0];
					localStorage.setItem('user', encodedData);

				} else {
					localStorage.setItem('user', data.user_id);
				}

				$.ajaxSetup({
					beforeSend: function(xhr, settings) {
					    xhr.setRequestHeader('Authorization', token);
					}
				});

				this.navigate('#/home');
			},

			triggerSignOut: function () {
				this.navigate('#/signin');
			},

			showHome: function () {
				var self = this;
				before (
					function () {
						var collection = new OptionsCollection();
						collection.add({ href: '#/home', name: 'Home', id:'home' });
						collection.add({ href: '#/requests?t=all', name: 'All Requests', id: 'all' });
						collection.add({ href: '#/requests?t=my', name: 'My Requests', id: 'my' });
						collection.add({ href: '#/signout', name: 'Sign Out', id: 'signout' });

						showView("#menu", new MenuView(self.options, collection));
						showView('#content', new HomeView(self.options));

						self.options.pubSub.trigger('highlight', '.home');
					}
				);
			},

			signIn: function () {
				if (localStorage.getItem('token')) {
					this.navigate('#/home');
					return;
				}

				showView("#menu", new MenuView(this.options, new OptionsCollection()));

				var signInView = new SigninView(this.options);
				showView("#content", signInView);
				signInView.renderOAuth(this.options);
				signInView.renderRegistration(this.options);
			},

			signOut: function () {
				var self = this;

				var token = localStorage.getItem('token').split("|");

				$.ajax({
					url: 'rest/auth', 
					type: 'PUT',
					success: function (data, textStatus, jqXHR) {
						$.ajaxSetup({
							beforeSend: function (xhr, settings) {
								xhr.setRequestHeader('Authorization', '');
							}
						});

						var gapi = new GapiManagerFactory();
						if (token[1] == 'google') {
								gapi.revoke(token[0], function () {
								self.options.pubSub.trigger('sign-out');
							});
						} else {
							self.options.pubSub.trigger('sign-out');
						}

						localStorage.removeItem('token');
						localStorage.removeItem('user');
						var message = new MessageView();
						message.renderMessage('Signed out');
					}
				});
			}		
		});
		
		var self = this;
		self.currentView = new Array();

		var requests = function(userId) {
			var data = {
				limit: -1
			};

			if (userId)
				data.user = userId;

			before (
				function () {
					var collection = new RequestCollection();
					collection.fetch({
						data: $.param(data),
						success: function (response, collection) {
							console.log(collection);
							var requestListView = new RequestListView(collection.requests);
							showView('#content', requestListView);
							requestListView.renderStats(collection._embedded.statistics);
						},
						error: function (model, xhr) {
							if (xhr.status == 401) {
								localStorage.removeItem('token');
								self.pubSub.trigger('sign-out');
							}
						}
					});
				}
			);
		}

		var before = function (callback) {
			var token = localStorage.getItem('token');
			if (token) {
				callback();
				return;
			}

			var message = new MessageView();
			message.renderMessage('You must sign in first');

			self.navigate('#/signin');
		}

		var showView = function (selector, view) {
		    if (self.currentView[selector])
		        self.currentView[selector].close();

		    $(selector).html(view.render().el);
		    self.currentView[selector] = view;
		    return view;
		}

		var initialize = function (options) {
			var appRouter = new AppRouter(options);
			Backbone.history.start();
		}

		return {
			initialize: initialize
		};
	});
