(function() {
	'use strict';
 
    angular.module('cmadBlog')
	.controller('SearchResultController', ['$scope', 'searchblogsList',  function ($scope, searchblogsList) {
		console.log('searchblogsList : ' + searchblogsList);
		var tempbloglist = searchblogsList;
		$scope.blogs = [];

		var i = 0;
		for (;i<tempbloglist.length; i++) {
			$scope.blogs[i] = tempbloglist[i];
			console.log('$scope.blogs [i] ID: ' + $scope.blogs[i].id);
		}
	}]);

})();