var app = angular.module('app', ['ngRoute', 'ngCookies']).
config(['$routeProvider', function ($routeProvider){
	$routeProvider.when('/login', {templateUrl: '/angularjs/login', controller: LoginAngularJS}).otherwise({redirectTo: '/login'});;
	//.when('/rooms', {templateUrl: 'angularjs/roomsAvailableangularjs', controller: RoomsCtrlAngularJS})
	$routeProvider.when('/search', {templateUrl: '/angularjs/search', controller: SearchAngularJS})	
	$routeProvider.when('/booking', {templateUrl: '/angularjs/booking', controller: BookingAngularJS})
}]).run(function() {

})
 
var LoginAngularJS = function($scope, $http, $location, $window){
	
	  $scope.login = function(){
		  
		     // Define the string
		     var string = {authorization : "Basic "
		         + btoa($scope.username + ":" +  $scope.password)
		     };
		     
		     $http.get('/angularjs/user', {headers: string })
		     .success(function(data, status){
		      $window.location.href="#/search";
		     }).error(function(){
		         console.log("Error while received data.");
		        
		     })};
	    
	
	}

app.myDropdown = function($scope, $http){
	
	
}

var hotelSelected="";
var initialDate="";
var finalDate="";

var SearchAngularJS = function($scope, $http, $window){
	
	 $scope.search = function(){
		 hotelSelected = $( "#repeatSelect option:selected" ).text();
		 console.log(hotelSelected);
		 initialDate = $scope.initial;
		 console.log(initialDate);
		 finalDate = $scope.end;
    	 console.log(finalDate);		 
		  //var searchInfo = {hotel : $( "#repeatSelect option:selected" ).text(), 
		  //		  		 initial : $scope.initial,
		  //		  		 end : $scope.end, 
		  //		  		 };
	     
	      $window.location.href="#/booking";
	     };
	
	}

var BookingAngularJS = function($scope, $http){

	
}



app.controller('SearchCtrl', function ($scope){
	 
});

app.controller('HotelList', function($scope, $http ) {
	$http.get('angularjs/allHotels')
	.success(function(data) {
	console.log(data);
	var hotels = [];
	for(var i = 0; i < data.length; i++)
		hotels[i] = {id : i, name : data[i].name};
		$scope.availableOptions = hotels;
	})
   }) 
 
	 var searchInfo = {hotel : hotelSelected, 
	  		 initial : initialDate,
	  		 end : finalDate, 
	  		 };	

app.controller('roomList', function($scope, $http, $window ) {
	 var searchInfo = {hotel : hotelSelected, 
	  		 initial : initialDate,
	  		 end : finalDate, 
	  		 };	
	 console.log(searchInfo);
			$http.get(('angularjs/allRooms'),{headers: searchInfo})
			.success(function(data) {
				console.log(data);
				$scope.hotel =  hotelSelected;
				$scope.initial =  initialDate;
				$scope.end =  finalDate;
				$scope.rooms = data;
	})
	
	$scope.doBookingForThisRoom = function(id) {
				console.log("entrei!!!");
			    var headers = {hotel : hotelSelected, initialdate : initialDate, finaldate : finalDate, roomID :id};
				 console.log(id);
			    $http.get('/angularjs/bookingSuccessful', {headers : headers})
			    .success(function(data){
			    	$window.location.href="#/bookingSuccessful"
			     });
			    };
}) ;
	

	
