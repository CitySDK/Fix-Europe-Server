define([
	'jquery',
	'underscore',
	'backbone',
	'oauth/gapi-custom',
	'text!views/layout/google-signin.html'
], function ($, _, Backbone, GapiManagerFactory, googleTemplate){
	var GoogleSigninView = Backbone.View.extend({
		el: '#signin-oauth',
		
		events: {
            "click #signin-google.signin": "signIn",
        },

        initialize: function (options) {
        	this.pubSub = options.pubSub;
        },

		render: function () {
			var compiledTemplate = _.template(googleTemplate, { });
			$(this.el).append(compiledTemplate);
			return this;
		},

		signIn: function () {
			var self = this;

			var gapi = new GapiManagerFactory()
			gapi.signIn(function (token) {
				console.log(token);

				$.post('rest/auth', JSON.stringify(token), function(data, textStatus, xhr) {
					self.pubSub.trigger('sign-in', Base64.encode(token.user + ':' + token.token), token.user, 'google');
					localStorage.setItem('user', token.user);
					//self.pubSub.trigger('sign-in', Base64.encode(token.user + ':' + token.token), 'google');
				}).fail(function (jqXHR, textStatus, errorThrown) {
					self.pubSub.trigger('sign-out');
				});
			});
		}
	});

	return GoogleSigninView;
});