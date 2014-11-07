define([
	'jquery',
	'underscore',
	'backbone'
], function ($, _, Backbone) {

	function GapiManager () { };

	function GapiManagerFactory (ready) { 
		loadGapi(ready);
	};

	_.extend(GapiManager.prototype, Backbone.Events);

	function loadGapi (ready) {
		// Don't load gapi if it's already present
	  	if (typeof gapi !== 'undefined') {
	    	ready(new GapiManager());
	    	return;
	  	}

	  	require(['https://apis.google.com/js/client.js?onload=define'], function() {
	    	// Poll until gapi is ready
	    	function checkGAPI() {
	      		if (gapi && gapi.client && gapi.auth) {
	        		ready(new GapiManager());
	      		} else {
	        		setTimeout(checkGAPI, 100);
	      		}
	    	}
	    
	    	checkGAPI();
	  	});
	}

	GapiManager.prototype.signIn = function (callback) {
		var self = this;
		self.callback = callback;

		function handleLoad () {
			setTimeout(checkAuth, 100);
		}

		function checkAuth () {
			var parameters = {
				client_id: '470117942962-j5gu65h01nveqh60v2h8klppvblna5gf.apps.googleusercontent.com',
				scope: 'https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email',
				immediate: false
			};

			gapi.auth.authorize(parameters, function (authResult) {
				if (authResult) {
					gapi.auth.setToken('token', authResult);
					self.callback(authResult);
				} else {
					console.log('no token received', authResult);
				}
			});
		}

		handleLoad();
	}

	GapiManager.prototype.revoke = function (callback) {
		var token = gapi.auth.getToken('token', true);

		console.log(token);
		if (token != null) {
			var revokeUrl = 'https://accounts.google.com/o/oauth2/revoke?token=' + token.access_token;
			$.ajax({
			    type: 'GET',
			    url: revokeUrl,
			    async: false,
			    contentType: 'application/json',
			    dataType: 'jsonp',

			    success: function(nullResponse) {
			      	callback();
			    },
			    
			    error: function(e) {
			     	// Handle the error
			      	console.log(e);
			      	callback();
			    }
			});
		} else {
			callback();
		}
	}

	GapiManager.prototype.getToken = function () {
		return gapi.auth.getToken();
	}

	return GapiManagerFactory;
});