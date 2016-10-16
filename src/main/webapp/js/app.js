angular.module('exampleApp', ['ngRoute', 'ngCookies', 'exampleApp.services','xeditable'])
	.config(
		[ '$routeProvider', '$locationProvider', '$httpProvider', function($routeProvider, $locationProvider, $httpProvider) {
			
			$routeProvider.when('/create', {
				templateUrl: 'partials/create.html',
				controller: CreateController
			});
			$routeProvider.when('/newmeasurement', {
                 templateUrl: 'partials/newmeasurement.html',
                 controller: NewMeasurementController
            });

			$routeProvider.when('/edit/:id', {
				templateUrl: 'partials/edit.html',
				controller: EditController
			});

			$routeProvider.when('/login', {
				templateUrl: 'partials/login.html',
				controller: LoginController

			});
			
            $routeProvider.when('/addBody', {
                            templateUrl: 'partials/addBody.html',
                            controller: EditableTableCtrl

                        });

            $routeProvider.when('/addSleeve', {
                             templateUrl: 'partials/addSleeve.html',
                             controller: AddSleeveCtrl

                                    });

			$routeProvider.otherwise(
            			{
                            templateUrl: 'partials/viewmeasurements.html',
                        	controller: ViewMeasurementsController
                        });


			$locationProvider.hashPrefix('!');
			
			/* Register error provider that shows message on failed requests or redirects to login page on
			 * unauthenticated requests */
		    $httpProvider.interceptors.push(function ($q, $rootScope, $location) {
			        return {
			        	'responseError': function(rejection) {
			        		var status = rejection.status;
			        		var config = rejection.config;
			        		var method = config.method;
			        		var url = config.url;
			      
			        		if (status == 401) {
			        			$location.path( "/login" );
			        		} else {
			        			$rootScope.error = method + " on " + url + " failed with status " + status;
			        		}
			              
			        		return $q.reject(rejection);
			        	}
			        };
			    }
		    );
		    
		    /* Registers auth token interceptor, auth token is either passed by header or by query parameter
		     * as soon as there is an authenticated user */
		    $httpProvider.interceptors.push(function ($q, $rootScope, $location) {
		        return {
		        	'request': function(config) {
		        		var isRestCall = config.url.indexOf('rest') == 0;
		        		if (isRestCall && angular.isDefined($rootScope.accessToken)) {
		        			var accessToken = $rootScope.accessToken;
		        			if (exampleAppConfig.useAccessTokenHeader) {
		        				config.headers['X-Access-Token'] = accessToken;
		        			} else {
		        				config.url = config.url + "?token=" + accessToken;
		        			}
		        		}
		        		return config || $q.when(config);
		        	}
		        };
		    }
	    );
		   
		} ]
		
	).run(function($rootScope, $location, $cookieStore, UserService,editableOptions) {

	     editableOptions.theme = 'bs3'; // bootstrap3 theme. Can be also 'bs2', 'default'
		
		/* Reset error when a new view is loaded */
		$rootScope.$on('$viewContentLoaded', function() {
			delete $rootScope.error;
		});
		
		$rootScope.hasRole = function(role) {
			
			if ($rootScope.user === undefined) {
				return false;
			}
			
			if ($rootScope.user.roles[role] === undefined) {
				return false;
			}
			
			return $rootScope.user.roles[role];
		};
		
		$rootScope.logout = function() {
			delete $rootScope.user;
			delete $rootScope.accessToken;
			$cookieStore.remove('accessToken');
			$location.path("/login");
		};
		
		 /* Try getting valid user from cookie or go to login page */
		var originalPath = $location.path();
		$location.path("/login");
		var accessToken = $cookieStore.get('accessToken');
		if (accessToken !== undefined) {
			$rootScope.accessToken = accessToken;
			UserService.get(function(user) {
				$rootScope.user = user;
				$location.path(originalPath);
			});
		}
		
		$rootScope.initialized = true;
	});



function IndexController($scope, BlogPostService) {

	$scope.blogPosts = BlogPostService.query();

	$scope.deletePost = function (blogPost) {
		blogPost.$remove(function () {
			$scope.blogPosts = BlogPostService.query();
		});
	};
}


function NewMeasurementController($scope,$routeParams, $location, NewMeasurementService) {

	$scope.newMeasurementEntry = new NewMeasurementService();
	$scope.newMeasurementEntry = NewMeasurementService.get({id: $routeParams.id});

	 $scope.sizes = [
        {id: 1, s: 'XS'},
        {id: 2, s: 'S'},
        {id: 3, s: 'M'},
        {id: 4, s: 'L'},
        {id: 5, s: 'XL'}
      ];

	$scope.save = function() {
		$scope.newMeasurementEntry.$save(function() {
			$location.path('/');
		});
	};
};


function ViewMeasurementsController($scope, NewMeasurementService) {

	$scope.newMeasurementEntries = NewMeasurementService.query();

	$scope.deleteEntry = function(newMeasurementEntry) {
		newMeasurementEntry.$remove(function() {
			$scope.newMeasurementEntries = NewMeasurementService.query();
		});
	};
};

function EditController($scope, $routeParams, $location, BlogPostService) {

	$scope.blogPost = BlogPostService.get({id: $routeParams.id});
	
	$scope.save = function() {
		$scope.blogPost.$save(function () {
			$location.path('/');
		});
	};
}


function CreateController($scope, $location, BlogPostService) {

	$scope.blogPost = new BlogPostService();
	
	$scope.save = function() {
		$scope.blogPost.$save(function () {
			$location.path('/');
		});
	};
};


function LoginController($scope, $rootScope, $location, $cookieStore, UserService) {
	
	$scope.rememberMe = false;
	
	$scope.login = function() {
		UserService.authenticate($.param({username: $scope.username, password: $scope.password}), function(authenticationResult) {
			var accessToken = authenticationResult.token;
			$rootScope.accessToken = accessToken;
			if ($scope.rememberMe) {
				$cookieStore.put('accessToken', accessToken);
			}
			UserService.get(function(user) {
				$rootScope.user = user;
				$location.path("/");
			});
		});
	};
	};

function EditableTableCtrl($scope, $filter, $http, $q){
 $scope.entries = [
    {id: 1, gmt: 'Garment1',chestWidth: 0, hemWidth:0,cbLength:0,cfLength:0},
    {id: 2, gmt: 'Garment2'},
    {id: 3, gmt: 'Garment3'},
    {id: 4, gmt: 'Garment4'},
    {id: 5, gmt: 'Garment5'}
  ];

  $scope.statuses = [
    {value: 1, text: 'status1'},
    {value: 2, text: 'status2'},
    {value: 3, text: 'status3'},
    {value: 4, text: 'status4'}
  ];

//  $scope.measurements = [
//    {value: 1,text: 'Chest Width'},
//    {value: 2,text: 'Hem Width'},
//    {value: 3,text: 'CB Length'},
//    {value: 4,text: 'CF Length'},
//    {value: 5,text: 'Sleeve Opening'},
//    {value: 1,text: 'Sleeve Length'},
//    {value: 1,text: 'Sleeve Width'},
//  ];


//  $scope.groups = [];
//  $scope.loadGroups = function() {
//    return $scope.groups.length ? null : $http.get('/groups').success(function(data) {
//      $scope.groups = data;
//    });
//  };
//
//  $scope.showGroup = function(entry) {
//    if(entry.group && $scope.groups.length) {
//      var selected = $filter('filter')($scope.groups, {id: entry.group});
//      return selected.length ? selected[0].text : 'Not set';
//    } else {
//      return entry.groupName || 'Not set';
//    }
//  };
//
//  $scope.showStatus = function(entry) {
//    var selected = [];
//    if(entry.status) {
//      selected = $filter('filter')($scope.statuses, {value: entry.status});
//    }
//    return selected.length ? selected[0].text : 'Not set';
//  };

//  $scope.checkName = function(data, id) {
//    if (id === 2 && data !== 'awesome') {
//      return "Username 2 should be `awesome`";
//    }
//  };

  // filter users to show
//  $scope.filterEntry = function(entry) {
//    return entry.isDeleted !== true;
//  };
//
//  // mark user as deleted
//  $scope.deleteEntry = function(id) {
//    var filtered = $filter('filter')($scope.entry, {id: id});
//    if (filtered.length) {
//      filtered[0].isDeleted = true;
//    }
//  };
//
//  // add user
//  $scope.addEntry = function() {
//    $scope.entries.push({
//      id: $scope.entries.length+1,
//      measurement: '',
//      status: null,
//      group: null,
//      isNew: true
//    });
//  };

  // cancel all changes
//  $scope.cancel = function() {
//    for (var i = $scope.entries.length; i--;) {
//      var user = $scope.entries[i];
//      // undelete
//      if (entry.isDeleted) {
//        delete entry.isDeleted;
//      }
//      // remove new
//      if (entry.isNew) {
//        $scope.entries.splice(i, 1);
//      }
//    };
//  };

  // save edits
  $scope.saveTable = function() {
    var results = [];
    console.log($scope.entries);

//    for (var i = $scope.entries.length; i--;) {
//      var user = $scope.entries[i];
//      // actually delete user
//      if (entry.isDeleted) {
//        $scope.entries.splice(i, 1);
//      }
//      // mark as not new
//      if (entry.isNew) {
//        entry.isNew = false;
//      }
//
//      // send on server
//      results.push($http.post('/saveEntry', entry));
//    }
//
//    return $q.all(results);
  };
}


function AddSleeveCtrl($scope, $filter, $http, $q){
 $scope.entries = [
    {id: 1, gmt: 'Sleeve1'},
    {id: 2, gmt: 'Sleeve2'},
    {id: 3, gmt: 'Sleeve3'},
    {id: 4, gmt: 'Sleeve4'},
    {id: 5, gmt: 'Sleeve5'}
  ];

    // save edits
  $scope.saveTable = function() {
    var results = [];
    for (var i = $scope.entries.length; i--;) {
      var user = $scope.entries[i];
      // actually delete user
      if (entry.isDeleted) {
        $scope.entries.splice(i, 1);
      }
      // mark as not new
      if (entry.isNew) {
        entry.isNew = false;
      }

      // send on server
      results.push($http.post('/saveEntry', entry));
    }

    return $q.all(results);
  };
}


var services = angular.module('exampleApp.services', ['ngResource']);

services.factory('UserService', function($resource) {
	
	return $resource('rest/user/:action', {},
			{
				authenticate: {
					method: 'POST',
					params: {'action' : 'authenticate'},
					headers : {'Content-Type': 'application/x-www-form-urlencoded'}
				}
			}
		);
});

services.factory('NewMeasurementService', function($resource) {

	return $resource('rest/new/:id', {id: '@id'});
});

