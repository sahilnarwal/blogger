(function() {
    'use strict';

    angular.module('cmadBlog')
    .controller('SinglePostController', ['BlogService', '$timeout', '$window' , '$scope', 'singleBlog', 'commentList',  function (BlogService, $timeout, $window, $scope,  singleBlog, commentList) {
        var spc = this;
        spc.submitComment = submitComment;
        spc.tempComment = {};

        $scope.blog = singleBlog;
        console.log(commentList);
        $scope.blogComments=commentList;
        console.log('length of comments '+commentList.length);
        console.log(' comments '+commentList);
        console.log('$scope.blog : ' + $scope.blog);
        spc.authenticated = $window.localStorage.getItem("authenticated");
        spc.user = $window.localStorage.getItem("currentUser");

        spc.user = JSON.parse(spc.user);


      function submitComment () {
            spc.tempComment.userName = spc.user.username;
            console.log("scope.tempComment.commentAuthor : "+ spc.tempComment.userName);
            console.log("scope.tempComment.commentedOn : "+ spc.tempComment.commentedOn);
            console.log("scope.blog.post.blogID : "+ $scope.blog.id);
            spc.tempComment.blogId=$scope.blog.id;
            console.log("spc.tempComment : "+ spc.tempComment);
            console.log("spc.tempComment.comment : "+spc.tempComment.comment);

            BlogService.addComment(spc.tempComment).then(function(response) {
                if (response.failure) {
                	console.log("match not found");
                   
                } else {
                	 console.log("response after: "+response);
                     console.log("No need of refresh");
                     console.log($scope.blogComments);
                     $scope.blogComments.push(spc.tempComment);
                     spc.tempComment=[];
                }
            });
        }
    }]);

})();