## Beacon Indoor Navigation Scanner

We have prototyped an **Indoor navigation system using beacons**, localising to accuracy of 1 meters in a room. Bleutooth Low Energy beacons provide useful in cases of navigating a user easily in commercial buildings like shopping malls and airports.

### Downloads - [Project Report](https://drive.google.com/file/d/1uvf1uUib5PyLKaXXHrbsPJoF0bfdluPQ/view?usp=sharing) / [Slides](https://docs.google.com/presentation/d/187OduFkGIvNpD2OsOlrvrTgP6FQvTOP1CiWbhYoGEjU/edit?usp=sharing)

## Description
- This application uses android class **BluetoothManager**, as a Service to scan for nearby beacons (Bluetooth devices) through an AsyncTask. 
- The service reschedule itself for future runs, in between pauses and also will unregister once the maximum number of runs have been reached.

<img src="https://i.imgur.com/UI1yBqv.png" width="300px"><img width="30px"><img src="https://i.imgur.com/noSL76D.png" height="569px">

## Design Architecture

<img src="https://i.imgur.com/H03qszf.png" width="750px">

## Team
- [@thameemk612](https://github.com/thameemk612)
- [@sid03](https://www.linkedin.com/in/sidheesh-nair-03859516a/)
- [Nakul Pramod](https://www.linkedin.com/in/nakul-s-pramod-6ba2041a0/)

## Dependencies

This is intended to be used in conjunction with  "Bluetooth Library for Android" library project which can be found here:
https://github.com/MikeFot/Android--Beacon-Scanner-Service
