
(function() {
	'use strict';

	var app = angular.module('cmadBlog', ["ngRoute"]);
	app.config(function($routeProvider) {
		$routeProvider
		.when("/", {
			templateUrl : "./partials/main.html",
			controller : 'MainPageController',
			controllerAs : 'mpc',
			resolve: {
                    blogsList : ['$route', 'BlogService', function ($route, BlogService) {
                        return BlogService.GetAllPosts();
                    }],
                    favBlogsList : ['BlogService','$window', function (BlogService, $window) {
                        var user = JSON.parse($window.localStorage.getItem("currentUser"));
                        console.log(user);
                        return BlogService.GetAllFavBlogs(user.areaOfInterest);
                     }]
                }
		})
		.when("/signupForm", {
			templateUrl : "./partials/signup.html",
			controller : 'RegisterController',
			controllerAs : 'rc'
		})
		.when ("/login", {
			templateUrl : "./partials/login.html",
			controller : 'LoginController',
			controllerAs : 'lc'
		})
		.when ("/newPost", {
			templateUrl : "./partials/new-blog-post.html",
			controller : 'NewPostController',
			controllerAs : 'npc'
		})
		.when ("/singlePost/:postId", {
			templateUrl : "./partials/single-page-blog.html",
			controller : 'SinglePostController',
			controllerAs : 'spc',
			resolve: {
                singleBlog : ['$route', 'BlogService', function ($route, BlogService) {
                	console.log('postId : '+$route.current.params);
                    console.log('postId : '+JSON.stringify($route.current.params));
                    var post=$route.current.params;
                    console.log('stringified postid: '+post.postId)
                    return BlogService.getPost(post.postId);
               	}],
                commentList : ['$route', 'BlogService', function ($route, BlogService) {
                	var post=$route.current.params;
                    console.log('stringified  commentslistpostid: '+post.postId)
                   // return BlogService.getPost(post.postId);
                   return BlogService.GetAllComments(post.postId);
                }]
            }
		})
		.when ("/searchResults/:searchString", {
			templateUrl : "./partials/search-results.html",
			controller : 'SearchResultController',
			controllerAs : 'src',
			resolve: {
                searchblogsList : ['$route', 'BlogService', function ($route, BlogService) {
                	console.log('postId : '+$route.current.params);
                    console.log('postId : '+JSON.stringify($route.current.params));
                    return BlogService.getPosts($route.current.params);
               	}]
         	}
		})
		.when ("/updateProfile", {
			templateUrl : "./partials/update-profile.html",
			controller : 'UpdateProfileController',
			controllerAs : 'upc'
		})
		.otherwise ({
			redirectTo : "/"
		});
	});

	app.run(function($rootScope) {
    	$rootScope.currentUser = {};
        $rootScope.authenticated = false;
	});

})();
