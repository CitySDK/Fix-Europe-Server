define([
	'jquery',
	'underscore',
	'backbone'
], function ($, _, Backbone){
	var UserModel = Backbone.Model.extend({
		urlRoot: '/participation/rest/signin',

		defaults: {
			user: '',
			password: '',
			token: '',
			type: 0
		},
	});

	return UserModel;
});