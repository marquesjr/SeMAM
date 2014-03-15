'use strict';
// Declare app level module which depends on filters, and services
angular.module('semam', ['semam.filters', 'semam.services', 'semam.directives', 'semam.controllers']).
    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/map', {templateUrl: 'partials/map.html', controller: 'MapCtrl'});
        $routeProvider.when('/calls', {templateUrl: 'partials/call-list.html', controller: 'CallListCtrl'});
        $routeProvider.when('/users', {templateUrl: 'partials/user-list.html', controller: 'UserListCtrl'});
        $routeProvider.otherwise({redirectTo: '/map'});
    }]);
