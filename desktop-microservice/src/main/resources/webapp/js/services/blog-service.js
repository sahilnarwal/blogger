(function () {
    'use strict';
     angular.module('cmadBlog').service('BlogService', function ($http) {
        var service = {};
 
        service.GetAllPosts = GetAllPosts;
        service.getPosts = getPosts;
        service.getPost = getPost;

        service.createPost = createPost;
        service.getAllPostsCount = getAllPostsCount;
        service.addComment = addComment;
        service.GetAllFavBlogs= GetAllFavBlogs;
        service.GetAllComments= GetAllComments;
        return service;
 
        function GetAllPosts() {
            console.log("GetAllPosts");
            return $http.get('http://localhost:9002/api/blogs').then(handleSuccess, handleError);
        }
        function GetAllFavBlogs(areaOfInterest) {
            console.log("GetAllPosts"+'http://localhost:9002/api/'+areaOfInterest+'/blog');
            return $http.get('http://localhost:9002/api/'+areaOfInterest+'/blog').then(handleSuccess, handleError);
        }

        function GetAllComments(blogId) {
            console.log("GetAllcomments "+'http://localhost:9002/api/'+blogId+'/comment');
            return $http.get('http://localhost:9002/api/comment/'+blogId).then(handleSuccess, handleError);
        }
        function getAllPostsCount() {
            console.log("getAllPostsCount");
            return $http.get('online/blog/allPostsCount').then(handleSuccess, handleError);
        }
 
        function getPosts(searchStringJson) {
            console.log("getPosts : "+ searchStringJson.searchString);
            var searchString = searchStringJson.searchString;
            return $http.get('http://localhost:9002/api/blog/'+searchString).then(handleSuccess, handleError);
        }
 
        function getPost(blogId) {
        	console.log(blogId);
        	console.log('http://localhost:9002/api/'+blogId+'/blog');
            return $http.get('http://localhost:9002/api/blog/id/'+blogId).then(handleSuccess, handleError);
        }

        function createPost(blog) {
            console.log("create Post");
            return $http.post('http://localhost:9002/api/blog', blog).then(handleSuccess, handleError);
        }

        function addComment(comment) {
            console.log("addComment");
            return $http.post('http://localhost:9002/api/comment', comment).then(handleSuccess, handleError);
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

