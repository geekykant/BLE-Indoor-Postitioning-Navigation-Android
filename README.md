# Beacon Scanner App
Sample Service App for Bluetooth LE Scanner

## Description
- This application will start a Service to scan for Beacons through an ASyncTask. 
- The Service will then reschedule itself for future runs and will unregister once the maximum number of runs have been reached or the application is paused through the MainActivity's onPause() method.

## Dependencies

This is intended to be used in conjunction with  "Bluetooth Library for Android" library project which can be found here:
https://github.com/MikeFot/Android--Beacon-Scanner-Service
