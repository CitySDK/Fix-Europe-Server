define([
	'jquery',
	'underscore',
	'backbone',
	'collections/layout/options',
	'text!views/layout/menu.html'
], function ($, _, Backbone, OptionsCollection, menuTemplate){
	var MenuView = Backbone.View.extend({
		tagName: 'div',

		initialize: function (options, collection) {
			this.highlighted = null;
			this.collection = collection;

			_.bindAll(this, "triggerSignIn");
			_.bindAll(this, "triggerSignOut");
			_.bindAll(this, "highlightOption");

    		options.pubSub.bind("sign-in", this.triggerSignIn);
    		options.pubSub.bind("sign-out", this.triggerSignOut);
    		options.pubSub.bind("highlight", this.highlightOption);
		},

		render: function () {
			var compiledTemplate = _.template(menuTemplate, { options: this.collection.models });
			$(this.el).html(compiledTemplate);
			return this;
		},

		triggerSignIn: function () {
			$(this.el).find('.signin').attr("href", "#/signout").removeClass('signin').addClass('signout').text('Sign Out');
		},

		triggerSignOut: function () {
			$(this.el).find('.signout').attr("href", "#/signin").removeClass('signout').addClass('signin').text('Sign In');
		},

		highlightOption: function (option) {
			if (this.highlighted != null)
				this.highlighted.toggleClass('highlight');

			var option = $(this.el).find(option);
			option.toggleClass('highlight');
			this.highlighted = option;
		}
	});

	return MenuView;
});