(function () {
    'use strict';
    angular
    .module('cmadBlog')
    .factory('sessionInjector', function($window) {  
        var sessionInjector = {
            request: function(config) {
                config.headers['Authorization'] = 'Bearer '+ $window.localStorage.getItem("token");
                return config;
            }
        };
        return sessionInjector;
    });
    
    angular
    .module('cmadBlog')
    .config(['$httpProvider', function($httpProvider) {  
        $httpProvider.interceptors.push('sessionInjector');
    }]);
    
})();