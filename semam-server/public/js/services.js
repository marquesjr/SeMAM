'use strict';

/* Services */

/*
 http://docs.angularjs.org/api/ngResource.$resource

 Default ngResources are defined as

 'get':    {method:'GET'},
 'save':   {method:'POST'},
 'query':  {method:'GET', isArray:true},
 'remove': {method:'DELETE'},
 'delete': {method:'DELETE'}

 */

var services = angular.module('semam.services', ['ngResource']);

services.factory('CallsFactory', function ($resource) {
    return $resource('/assets/calls', {}, {
        query: 	{ method: 'GET',  isArray: true },
        handle: { method: 'POST'}
    })
});


services.factory('EmergenciesFactory', function ($resource) {
    return $resource('/assets/emergencies', {}, {
        query: 	{ method: 'GET',  isArray: true }
    })
});

services.factory('UsersFactory', function ($resource) {
    return $resource('/assets/users', {}, {
        query: 	{ method: 'GET',  isArray: true },
        create: { method: 'POST' }
    })
});

services.factory('UserFactory', function ($resource) {
    return $resource('/assets/user/:id', {}, {
        show: { method: 'GET', params: {id: '@id'} },
        update: { method: 'POST', params: {id: '@id'} },
        delete: { method: 'DELETE', params: {id: '@id'} }
    })
});

services.factory('MapFactory', function ($resource) {
    return $resource('/context/map', {}, {
        query: 	{ method: 'GET',  isArray: true }
    })
});
