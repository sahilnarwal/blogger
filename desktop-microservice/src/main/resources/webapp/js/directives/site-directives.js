(function() {


	angular.module('cmadBlog')
	.directive('siteHeader', function($rootScope) {
		return {
			restrict: 'E',
			templateUrl : 'site-header.html',
			controller : 'NavbarController',
			controllerAs : 'nc'
		}
	})
	.directive('siteFooter', function() {
		return {
			restrict: 'E',
			templateUrl : 'site-footer.html'
		}
	});
})();