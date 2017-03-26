(function () {
    'use strict';
    angular
    .module('cmadBlog')
    .controller('RegisterController', ['UserService',  '$location' , function (UserService, $location) {
        var rc = this;

        rc.register = register;

        function register(isValid) {
            console.log("isValid : "+isValid);
            if (isValid) {
                rc.dataLoading = true;
                console.log("going to call : UserService");

                // Create user
                UserService.CreateUser(rc.user).then(function (response) {
                    console.log(response);
                   // console.log('got the response : response.writtenToDb : '+response.writtenToDb);
                    if (response.failure) {
                   	 rc.dataLoading = false;
                   }else{
                	   UserService.SetCredentials(response);
                   	 $location.path('/home');
                        console.log('path set');
                   }
                });
            }
        }
    }]);
    
})();