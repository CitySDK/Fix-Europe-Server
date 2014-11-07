define([
	'jquery',
	'underscore',
	'backbone',
	'blockui',
	'base64',
	'views/layout/message',
	'views/layout/google-signin',
	'views/layout/registration',
	'text!views/layout/signin.html'
], function ($, _, Backbone, BlockUI, Base64, MessageView, GoogleSignIn, RegistrationView, loginTemplate){
	var SigninView = Backbone.View.extend({
		tagName: 'div id="signin-container"',

		initialize: function (options) {
			this.pubSub = options.pubSub;

        	_.bindAll(this, "triggerSignIn");
    		this.pubSub.bind("cancel", this.triggerSignIn);
    		this.pubSub.bind("reg-done", this.triggerSignIn);
		},

		events: {
			"click #signin-button": "performSignIn",
            "click .registration": "triggerRegistration"
        },

		render: function () {
			var compiledTemplate = _.template(loginTemplate);
			$(this.el).html(compiledTemplate);
			this.signin = $(this.el).find('#signin');

			return this;
		},

		renderOAuth: function (options) {
			new GoogleSignIn(options).render();
		},

		renderRegistration: function (options) {
			new RegistrationView(options).render();
		},

		triggerRegistration: function () {
			this.signin.block({ 
				overlayCSS: { 
					backgroundColor: '#dcdcdc', 
					cursor: 'normal', 
					opacity: 0.5 
				}, 
				message: null 
			});

			this.pubSub.trigger("registration", { message: 'basic' });
		},

		triggerSignIn: function () {
			this.signin.unblock();
		},

		performSignIn: function () {
			var data = true, fields = [ '#email', '#password' ];
			
			clearFields(fields);
			data = verifyFields(fields);

			if (data == false)
				return;

			var details = { 
				user: data[0], 
				password: data[1], 
				type: 'basic'
			};

			var self = this;
			$.post('rest/auth', JSON.stringify(details), 
				function (data, textStatus, jqXHR) {
					console.log(data);
					var message = new MessageView();
					message.renderMessage('Signed In');
					
					self.pubSub.trigger('sign-in', Base64.encode(data.user_id + ':' + data.user_token.access_token), data, 'basic');
				}, 
			'json').fail( 
				function(xhr, textStatus, errorThrown) {
					var response = eval("(" + xhr.responseText + ")");
        			$('#error-message').text(response.message).show();
    			});

			function clearFields (fields) {
				for (var i = 0; i < fields.length; i++) {
					var field = fields[i];
					$(field + '-label').removeClass('error');
				}

				$('#error-message').hide();
			}

			function verifyFields (fields) {
				var field, data = [];
				for (var i = 0; i < fields.length; i++) {
					field = $(fields[i]).val();
					
					if(field == '') {
						$(fields[i] + '-label').addClass('error');
						return false;
					} else {
						data.push(field);
					}
				}

				return data;
			}
		}
	});

	return SigninView;
});