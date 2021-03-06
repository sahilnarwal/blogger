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
            return $http.get('https://localhost:9002/api/blogs').then(handleSuccess, handleError);
        }
        function GetAllFavBlogs(areaOfInterest) {
            console.log("GetAllPosts"+'https://localhost:9002/api/blog/'+areaOfInterest+'?type=tag');
            return $http.get('https://localhost:9002/api/blog/'+areaOfInterest+'?type=tag').then(handleSuccess, handleError);
        }

        function GetAllComments(blogId) {
            console.log("GetAllcomments "+'https://localhost:9002/api/comment/'+blogId);
            return $http.get('https://localhost:9003/api/comment/'+blogId).then(handleSuccess, handleError);
        }
        function getAllPostsCount() {
            console.log("getAllPostsCount");
            return $http.get('online/blog/allPostsCount').then(handleSuccess, handleError);
        }
 
        function getPosts(searchStringJson) {
            console.log("getPosts : "+ searchStringJson.searchString);
            var searchString = searchStringJson.searchString;
            return $http.get('https://localhost:9002/api/blog/'+searchString+'?type=title').then(handleSuccess, handleError);
        }
 
        function getPost(blogId) {
        	console.log(blogId);
        	console.log('https://localhost:9002/api/'+blogId+'/blog');
            return $http.get('https://localhost:9002/api/blog/'+blogId+'?type=id').then(handleSuccess, handleError);
        }

        function createPost(blog) {
            console.log("create Post");
            return $http.post('https://localhost:9002/api/blog', blog).then(handleSuccess, handleError);
        }

        function addComment(comment) {
            console.log("addComment");
            return $http.post('https://localhost:9003/api/comment', comment).then(handleSuccess, handleError);
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

