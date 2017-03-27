(function () {
    'use strict';
     angular.module('cmadBlog').service('UserService', function ($http, $window) {
        var service = {};
 
        service.GetUserInfo = GetUserInfo;
        service.CreateUser = CreateUser;
        service.UpdateUser = UpdateUser;

        service.Login = Login;
        service.SetUserInfo = SetUserInfo;
        service.SetCredentials = SetCredentials;
        service.ClearCredentials = ClearCredentials;
 
        return service;
 
        function GetUserInfo(username) {
            console.log("GetUserInfo");
            return $http.get('https://35.185.201.222:443/api/user/' + username).then(handleSuccess, handleError);
        }
 
        function CreateUser(user) {
            console.log("CreateUser");
            
            return $http.post('https://35.185.201.222:443/api/user/', user).then(handleSuccess, handleError);
        }
 
        function UpdateUser(user) {
            console.log("UpdateUser");
            return $http.put('https://35.185.201.222:443/api/user/', user).then(handleSuccess, handleError);
        }

        function Login(user) {
            console.log("Login");
            return $http.post('https://35.185.201.222:443/api/user/login', user).then(handleSuccess, handleError);
        }

        function SetCredentials(response) {
            console.log("SetCredentials");
            // var authdata = Base64.encode(email + ':' + );
            $window.localStorage.setItem("token", response);
            $window.localStorage.setItem("authenticated", true);
        }
        
        function SetUserInfo(response) {
            console.log("SetUserInfo");
            $window.localStorage.setItem("currentUser", JSON.stringify(response));
        }
 
        function ClearCredentials() {
            console.log("ClearCredentials");
            $window.localStorage.removeItem("currentUser");
            $window.localStorage.removeItem("authenticated");
            $window.localStorage.removeItem("token");
        }

        // private functions 
        function handleSuccess(res) {
            console.log("handleSuccess");
            return res.data;
        }
 
        function handleError(error) {
            console.log("handleError");
                return { failure: true, message: error };
        }
    });
})();

