define([
	'jquery',
	'underscore',
	'backbone'
], function ($, _, Backbone){

	function GapiManager (options) { 
		this.options = options;
	};

	function GapiManagerFactory () { 
		var hostname = location.hostname;
		var scopes = 'https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.profile',
			oauthUrl = 'https://accounts.google.com/o/oauth2/auth?',
			clientId = '470117942962-j5gu65h01nveqh60v2h8klppvblna5gf.apps.googleusercontent.com',
			type = 'token',
			redirectUrl = 'https://' + hostname + '/participation/oauth';

		var options = {
			redirect: redirectUrl,
        	url: oauthUrl + 'scope=' + scopes + '&client_id=' + clientId + '&redirect_uri=' + redirectUrl + '&response_type=' + type,
			validUrl: 'https://www.googleapis.com/oauth2/v1/tokeninfo?access_token='
		};

		return new GapiManager(options);
	};

	_.extend(GapiManager.prototype, Backbone.Events);

	GapiManager.prototype.signIn = function (callback) {
		var self = this;
		var win = window.open(self.options.url, "google-oauth", 'width=800, height=600'); 

	    var pollTimer = window.setInterval(function() { 
	        try {
	            if (win.document.URL.indexOf(self.options.redirect) != -1) {
	                window.clearInterval(pollTimer);
	                var url = win.document.URL;
	                acToken = gup(url, 'access_token');
	                tokenType = gup(url, 'token_type');
	                expiresIn = gup(url, 'expires_in');
	                win.close();

	                validateToken(acToken, callback);
	            }
	        } catch(e) {

	        }
	    }, 500);
	
	    function validateToken (token, callback) {
	    	self.token = token;

	        $.ajax({
	            url: self.options.validUrl + token,
	            data: null,
	            success: function(info) {
	            	var details = { 
						user: info.user_id, 
						token: token, 
						type: 'google'
					};
	                callback(details);
	            },  
	        
	            dataType: "jsonp"  
	        });
	    }
	}

	function gup (url, name) {
        name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
        var regexS = "[\\#&]"+name+"=([^&#]*)";
        var regex = new RegExp( regexS );
        var results = regex.exec( url );
        if( results == null )
            return "";
        else
            return results[1];
    }

	GapiManager.prototype.revoke = function (token, callback) {
		if (token) {
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

	return GapiManagerFactory;
});