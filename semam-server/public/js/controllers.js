'use strict';

/* Controllers */

var app = angular.module('semam.controllers', ['google-maps', 'ui.bootstrap']);

// Clear browser cache (in development mode)
//
// http://stackoverflow.com/questions/14718826/angularjs-disable-partial-caching-on-dev-machine
//app.run(function ($rootScope, $templateCache) {
//    $rootScope.$on('$viewContentLoaded', function () {
//        $templateCache.removeAll();
//    });
//});

app.controller('CallsListCtrl', ['$scope', 'CallsFactory',
	function ($scope, CallsFactory) {
	    $scope.calls = CallsFactory.query();

        // callback for ng-click 'deleteUser':
        $scope.handleCall = function (call) {
            CallsFactory.handle(call, function() {
                $scope.calls = CallsFactory.query();
            });

            //$scope.users = UsersFactory.query();
        };


	}]);

app.controller('UserListCtrl', ['$scope', 'UsersFactory', 'UserFactory', '$location','$timeout',
    function ($scope, UsersFactory, UserFactory, $location, $timeout) {

        // callback for ng-click 'editUser':
        $scope.editUser = function (userId) {
            $location.path('/user/' + userId);
            $scope.user = UserFactory.show(userId);
        };

        // callback for ng-click 'deleteUser':
        $scope.deleteUser = function (userId) {
            UserFactory.delete({ id: userId }, function() {
            	$scope.users = UsersFactory.query();
        	});
            
            //$scope.users = UsersFactory.query();
        };

        // callback for ng-click 'createUser':
        $scope.createNewUser = function () {
            $location.path('/new');
        };

        $scope.select = function(userId) {
        	$scope.selected = userId;
        	console.log($scope.selected);
            $location.path('/user/' + userId);
          };
        
//		  (function tick() {
//		      $scope.users = UsersFactory.query(function(){
//		          $timeout(tick, 5000);
//		      });
//		  })();  
          
        $scope.users = UsersFactory.query();
    }]);

app.controller('UserDetailCtrl', ['$scope', '$routeParams', 'UserFactory', '$location',
    function ($scope, $routeParams, UserFactory, $location) {
	
		// callback for ng-click 'updateUser':
        $scope.updateUser = function () {
            UserFactory.update($scope.user, function() {
            	$location.path('/users'); 
        	});
        };

        // callback for ng-click 'cancel':
        $scope.cancel = function () {
            $location.path('/users');
        };

        $scope.user = UserFactory.show({id: $routeParams.id});
    }]);

app.controller('UserCreationCtrl', ['$scope', 'UsersFactory', '$location',
    function ($scope, UsersFactory, $location) {

        // callback for ng-click 'createNewUser':
        $scope.saveNewUser = function () {
            UsersFactory.create($scope.user, function() {
            		$location.path('/users');
        	});
        }
    }]);

app.controller('MapCtrl', ['$scope', 'MapFactory', '$timeout','$log', '$modal',
    function ($scope, MapFactory, $timeout, $log, $modal) {

// Enable the new Google Maps visuals until it gets enabled by default.
        // See http://googlegeodevelopers.blogspot.ca/2013/05/a-fresh-new-look-for-maps-api-for-all.html
        google.maps.visualRefresh = true;

        var ambulance_icon = 'images/ambulance-busy.png';
        var patient_icon = 'images/patient3.png';

        //$scope.ambulances = [{}];
        $scope.markers = [];

        $scope.updatePositions = function(){
            $timeout(function() {

                $scope.ambulances = MapFactory.query();


                $scope.ambulances = MapFactory.query(function (response) {

                    $scope.markers = [];

                    angular.forEach(response, function (ambulance) {
                        if (ambulance.displacement !== undefined) {
                            var decodedPath = google.maps.geometry.encoding.decodePath(ambulance.displacement.path);
                            var poly = new google.maps.Polyline({
                                path: decodedPath
                            });

                            var position = poly.GetPointAtDistance(ambulance.displacement.distance);
                            var marker = {};
                            marker.latitude = position.lat();
                            marker.longitude = position.lng();
                            if (ambulance.type === 'ambulance') {
                                marker.icon = ambulance_icon;
                                marker.title = 'Ambulance ' + ambulance.id;
                            } else {
                                marker.icon = patient_icon;
                                marker.title = 'Patient ' + ambulance.id;
                            }

                            $scope.markers.push(marker);
                        }
                        else {
                            var marker = {};
                            marker.latitude = ambulance.location.latitude;
                            marker.longitude = ambulance.location.longitude;
                            if (ambulance.type === 'ambulance') {
                                marker.icon = ambulance_icon;
                                marker.title = 'Ambulance ' + ambulance.id;
                            } else {
                                marker.icon = patient_icon;
                                marker.title = 'Patient ' + ambulance.id;
                            }
                            $scope.markers.push(marker);
                        }
                    });
                    if(!$scope.$$phase) {
                        $scope.$apply();
                    }
                    $scope.updatePositions();
                });

            }, 3000)
        };

        $scope.newEmergency = function () {

            $modal.open({
                templateUrl: 'partials/emergency-modal.html',
                backdrop: true,
                windowClass: 'modal'
            });

        };

        $scope.dragging = false;

        // Kick off the interval
        angular.extend($scope, {

            position: {
                coords: {
                    latitude: -20.29943282233351,
                    longitude: -40.296565771102905
                }
            },

            // the initial center of the map
            center: {
                latitude: 45,
                longitude: -73
            },

            // the initial zoom level of the map
            zoom: 15,

            // These 2 properties will be set when clicking on the map
            clickedLatitude: null,
            clickedLongitude: null,

            events: {

                idle: function (mapModel, eventName, originalEventArgs) {
                 // 'this' is the directive's scope

                 this.markers = $scope.markers;

                 //$scope.updatePositions();
                 //$log.log("user defined event on map directive with scope 3", this);
                 //$log.log("user defined event: " + eventName, mapModel, originalEventArgs);

                 $scope.updatePositions();

                 $log.log('idle ' + eventName);

                }/*,

                drag: function (mapModel, eventName, originalEventArgs) {

                    //$log.log("user defined event on map directive with scope 2", this);
                    //$log.log("user defined event: " + eventName, mapModel, originalEventArgs);
                    //$log.log(eventName);
                    //$log.
                    $log.log('drag ' + eventName);
                },

                click: function (mapModel, eventName, originalEventArgs) {

                    if (mapModel.idle) {
                        $log.log('idle');
                    } else if (mapModel.zooming) {
                        $log.log('idle');
                    } else if (mapModel.zooming) {
                        $log.log('zooming');
                    } else if (mapModel.dragging) {
                        $log.log('dragging');
                    } else if (!$scope.dragging) {
                        $log.log('click');
                    }

                    $scope.dragging = mapModel.dragging;

                } */

            }
        });

    }]);


