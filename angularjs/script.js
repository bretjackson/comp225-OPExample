angular.module("myApp", []);
(function() {
  function MyController($scope, myService) {
    $scope.myController = {};
    $scope.myController.sendMultipleRequests = sendMultipleRequests;
    $scope.myController.responseStatus = [];
    $scope.myController.responseData = [];
    
    function sendMultipleRequests() {
      for(var i=0; i < 3; i++) {
         myService.getLatLong().then(function(data) {
          $scope.myController.responseStatus.push("Success");
          $scope.myController.responseData.push(data);
        }, function(error) {
          $scope.myController.responseStatus.push("Error");
          $scope.myController.responseData.push(error);
        });   
      }
    }
  }
  angular.module("myApp").controller("MyController", MyController);
})();