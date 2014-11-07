require.config({
	paths: {
		jquery: 'libs/jquery/jquery-1.10.2.min',
		blockui: 'libs/jquery/block-ui.jquery',
		underscore: 'libs/underscore/underscore-1.5.1.min',
		backbone: 'libs/backbone/backbone-1.0.0.min',
        gapi: 'https://apis.google.com/js/client.js?onload=define',
        base64: 'libs/sec/base64',
	},
    
	enforceDefine: false,
	generateSourceMaps: false,

	shim: {
		'jquery': {
            exports: '$'
        },
        
        'underscore': {
            exports: '_'
        },
        
        'blockui': {
        	deps: ['jquery'],
        	exports: 'BlockUI'
        },

        'backbone': {
            deps: ['underscore', 'jquery'],
            exports: 'Backbone'
        },

        'gapi': {
            exports: 'gapi'
        },

        'base64': {
            exports: 'Base64'
        }
    }
});

// Load the app module and pass it to the definition function
// The "app" dependency is passed in as "App"
define(['app'], function(App){
	App.initialize();
});
