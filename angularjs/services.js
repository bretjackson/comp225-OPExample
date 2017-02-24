(function() {
  angular.module("myApp").service("myService", myService);
  function myService($q, $http) {
    this.getLatLong = getLatLong;
    var observers = [];
    var isProgress = false;
    
     function getLatLong() {
      var deferred = $q.defer();
      var requrl = "http://maps.googleapis.com/maps/api/geocode/json?address=saintpaul&sensor=true";
      //check if any request is already in progress
      if(isProgress) {  
        observers.push(deferred)
      }
      else {
        //if the request is not in progress then change the variable and call the server for data
        isProgress = true;
        $http.get(requrl).then(function(successData) {
          isProgress = false;
          //once data received from server, if it is success, resolve all observers 
          angular.forEach(observers, function(item) {
            item.resolve(successData);
          });
          observers = [];
          //finally resolve the original request
          deferred.resolve(successData);
        }, function(error) {
          //once data received from server, if it is failure, reject all observers
          isProgress = false;
          angular.forEach(observers, function(item) {
            item.resolve(error);
          });
          observers = [];
          //finally reject the original request
          deferred.reject(error);
        });
      }
      return deferred.promise;
    }
    
  }
})();
