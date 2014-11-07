define([
	'underscore',
	'backbone',
	'models/layout/option'
], function (_, Backbone, OptionModel){
	var OptionCollection = Backbone.Collection.extend({
		model: OptionModel
	});

	return OptionCollection;
});