define([
	'jquery',
	'underscore',
	'backbone',
	'blockui',
	'views/layout/message',
	'text!views/layout/registration.html'
], function ($, _, Backbone, BlockUI, MessageView, registrationTemplate){
	var RegistrationView = Backbone.View.extend({
		el: '#signin-container',
		
		events: {
			"click #register-button": "performRegistration",
            "click .cancel": "cancelRegistration",
            "keyup :input#reg-pass": "passStrength",
    		"keypress :input#reg-pass": "passStrength"
        },

		initialize: function (options) {
			this.pubSub = options.pubSub;

			_.bindAll(this, "triggerRegistration");
    		this.pubSub.bind("registration", this.triggerRegistration);
		},

		render: function () {
			var compiledTemplate = _.template(registrationTemplate, { });
			$(this.el).append(compiledTemplate);

			this.registration = $(this.el).find("#registration");
			this.registration.block({ 
				overlayCSS: { 
					backgroundColor: '#dcdcdc', 
					cursor: 'normal', 
					opacity: 0.5 
				}, 
				message: null 
			});

			$(this.el).find('#reg-pass').change(this.passStrength);

			return this;
		},

		triggerRegistration: function (message) {
			this.registration.unblock();

			if (message.message == 'basic')
				return;				// no need to change view

			$(this.el).find('#control-email').hide();
			$(this.el).find('#control-pass').hide();
		},

		passStrength: function () {
			var password = $('#reg-pass').val();

			if (password == '') {
				$('#pass-strength').text('');
				return
			}

			var desc = new Array();
				desc[0] = "Weird";
				desc[1] = "Bad";
				desc[2] = "Ok";
				desc[3] = "Good";
				desc[4] = "Nice";
				desc[5] = "Awesome";

			var score = 0;

			//if password bigger than 6 give 1 point
			if (password.length > 6) score++;

			//if password has both lower and uppercase characters give 1 point 
			if ((password.match(/[a-z]/)) && (password.match(/[A-Z]/))) score++;

			//if password has at least one number give 1 point
			if (password.match(/\d+/)) score++;

			//if password has at least one special caracther give 1 point
			if (password.match(/.[!,@,#,$,%,^,&,*,?,_,~,-,(,)]/)) score++;

			//if password bigger than 12 give another 1 point
			if (password.length > 12) score++;

			var c = $('#pass-strength').attr('class');
			$('#pass-strength').text(desc[score]);
			$('#pass-strength').removeClass(c);
			$('#pass-strength').addClass('strength').addClass("strength" + score);
		},

		performRegistration: function () {
			var data = true, fields = [ '#reg-user', '#reg-email', '#reg-pass' ];

			clearFields(fields);
			data = verifyFields(fields);

			if (data == false)
				return;

			if(validateUser(fields[0]) 
				&& validateEmail(fields[1]) 
				&& validatePass(fields[2])) {
				var details = { 
					user: data[0],
					email: data[1], 
					password: data[2], 
					type: 'reg'
				};

				var self = this;
				$.post('rest/register',JSON.stringify(details), 
					function (data, textStatus, jqXHR) {
						clearFields(fields);
						var message = new MessageView();
						message.renderMessage(data.message);

						self.pubSub.trigger('reg-done');
						self.registration.block({ 
							overlayCSS: { 
								backgroundColor: '#dcdcdc', 
								cursor: 'normal', 
								opacity: 0.5 
							}, 
							message: null 
						});
					}, 
				'json').fail( 
					function(xhr, textStatus, errorThrown) {
						var response = eval("(" + xhr.responseText + ")");
						clearFields(fields);
	        			$('#reg-error-message').text(response.message).show();
	    			});
			}

			function validateEmail(field) {
				var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;

    			if (!filter.test($(field).val())) {
    				$('#reg-error-message').text('Please provide a valid email address').show();
   					$(field).focus();
   					$(field + '-label').addClass('error');

    				return false;
    			}

    			return true;
			}

			function validateUser(field) {
				var filter = /^[0-9a-zA-Z_\.\-]+$/
				var val = $(field).val();

				if (val.length < 6) {
					$('#reg-error-message').text('Username must be 6 letters long').show();
   					$(field).focus();
   					$(field + '-label').addClass('error');

    				return false;
				}

				if (!filter.test(val)) {
    				$('#reg-error-message').text('Username must be alphanumeric and can only contain either: . _ or -').show();
   					$(field).focus();
   					$(field + '-label').addClass('error');

    				return false;
    			}

    			return true;
			}

			function validatePass (field) {
				var password = $(field).val();

				if (password.length < 6) {
					$('#reg-error-message').text('Password must be at least 6 characters long').show();
					$(field).focus();
   					$(field + '-label').addClass('error');
					return false;
				}
				
				if (!((password.match(/[a-z]/)) && (password.match(/[A-Z]/)))) {
					$('#reg-error-message').text('Password must contain both lower and uppercase letters').show();
					$(field).focus();
   					$(field + '-label').addClass('error');
					return false;
				}

				if (!password.match(/\d+/)) {
					$('#reg-error-message').text('Password must contain at least one number').show();
					$(field).focus();
   					$(field + '-label').addClass('error');
					return false;
				}
			
				return true;	
			}

			function clearFields (fields) {
				for (var i = 0; i < fields.length; i++) {
					var field = fields[i];
					$(field + '-label').removeClass('error');
				}

				$('#reg-error-message').hide();
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
		},

		cancelRegistration: function () {
			this.registration.block({ 
				overlayCSS: { 
					backgroundColor: '#dcdcdc', 
					cursor: 'normal', 
					opacity: 0.5 
				}, 
				message: null 
			});

			this.pubSub.trigger('cancel');
		}
	});

	return RegistrationView;
});