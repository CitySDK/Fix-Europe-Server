define([
	'underscore',
	'backbone'
], function (_, Backbone){
	var OptionModel = Backbone.Model.extend({
		defaults: {
			href: '',
			name: ''
		}
	});

	return OptionModel;
});