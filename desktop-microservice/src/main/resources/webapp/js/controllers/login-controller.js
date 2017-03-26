(function() {
	'use strict';
	angular
	.module('cmadBlog')
	.controller('LoginController', ['UserService',  '$location' , '$scope', '$timeout', '$window', function (UserService, $location, $scope, $timeout, $window) {
		var lc = this;
		lc.login = login;
		$scope.loginError = "";

		(function initController() {
            // reset session status
            console.log("Clearing the session storage if any data is there");
            UserService.ClearCredentials();
        })();

        function login() {
        	lc.dataLoading = true;
        	console.log("Login entered");

        	UserService.Login(lc.user).then(function(response) {
        	//	console.log("matchFound : "+response.matchFound);
        		console.log("response : "+response.success);
        		if (response.failure) {
        			console.log("match not found");
        			$scope.loginError = "Username or password doesn't match. Please try again...";
        			lc.dataLoading = false;
        			
        			
        		} else {
        			UserService.SetCredentials(response);
        			console.log("credentials set in session storage");
        		}
        	});
        	
        	UserService.GetUserInfo(lc.user.username).then(function(response) {
        	//	console.log("matchFound : "+response.matchFound);
        		console.log("response : "+response.success);
        		if (response.failure) {
        			console.log("match not found");
        			$scope.loginError = "Unable to fetch User Details. Please try again...";
        			UserService.ClearCredentials();
        			lc.dataLoading = false;
        			
        		} else {
        			UserService.SetUserInfo(response);
        			console.log("User Info set in session storage");
        			$location.path('/home');
        			$timeout(function() {$window.location.reload();}, 100);
        		}
        	});
        }
    }]);
})();
