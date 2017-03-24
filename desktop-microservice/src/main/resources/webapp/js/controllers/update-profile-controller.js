(function () {
    'use strict';
    angular
    .module('cmadBlog')
    .controller('UpdateProfileController', ['UserService',  '$location' , '$window', '$timeout', function (UserService, $location, $window, $timeout) {
        var upc = this;
        var tempUser = $window.localStorage.getItem("currentUser");
        tempUser = JSON.parse(tempUser);
        upc.user = {};
        upc.user.email = tempUser.email;
        upc.user.username  = tempUser.username;
        upc.user.areaOfInterest  = tempUser.areaOfInterest;
        upc.user.name=tempUser.name;
        upc.user.pwd=tempUser.pwd;
        upc.user.phoneNumber=tempUser.phoneNumber;

        upc.updateUser = updateUser;

        function updateUser() {
            upc.dataLoading = true;
            console.log("going to call : UserService");
            UserService.UpdateUser(upc.user).then(function (response) {
                console.log(response);
                if (response.failure) {
                	upc.dataLoading = false;
                } else {
                	UserService.SetCredentials(response);
                     $location.path('/');
                     $timeout(function() {$window.location.reload();}, 100);
                     console.log('path set');
                    
                }
            });
        }     
    }]);
    
})();
