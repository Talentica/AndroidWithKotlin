# AndroidWithKotlin [![Build Status](https://travis-ci.org/Talentica/AndroidWithKotlin.svg?branch=master)](https://travis-ci.org/Talentica/AndroidWithKotlin) [![Apache 2.0 License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0.html) [![GitHub version](https://badge.fury.io/gh/Talentica%2FAndroidWithKotlin.svg)](https://badge.fury.io/gh/Talentica%2FAndroidWithKotlin) [![Issue Count](https://codeclimate.com/github/Talentica/AndroidWithKotlin/badges/issue_count.svg)](https://codeclimate.com/github/Talentica/AndroidWithKotlin) [![Kotlin](https://img.shields.io/badge/Kotlin-1.1.2-blue.svg)](http://kotlinlang.org)

These are android samples projects which are written in kotlin.
The project contains the following apps:

#### 1. app(default) : Google IO 2017 Android Architecture Components Sample: Room, ViewModel(MVVM), LiveData and Lifecycle.  
#### 2. audioplayer: An audio player app which plays mp3 file and custom seek bar to seek.  
#### 3. locationmanager: An on-demand location fetching app which uses google's latest fused location provider. If using emulator then send the location from Extended Controls of Emulator.
#### 4. sqlitedatabase: A simple sqlite database app with mock server which handles user(friend) search and logs the queries into local database. A good app to understand RxAndroid in kotlin.
#### 5. videostreaming: A live RTSP video streaming app with custom media controls.(Better run this app on physical device)
#### 6. customcamera: This is a MVP pattern, custom camera app with flash support. The home screen is a gridview which shows clicked photos. Dagger 2 implementation added.
#### 7. o_notifications: This is a MVP pattern, android-O notifications. The home screen provides simple way to create notification channels and notifications. By default there are 2 notification groups “Personal” and “Business”.
#### 8. sensors: Explore how to make a compass app using magnetic field sensor and accelerometer. 
 
More to come in future :octocat: :star2:

<img src="http://i.imgur.com/HzmmBvZ.jpg" />&nbsp;


## Minimum Requirements

 * Android Studio 3.0 Canery 3
 * Kotlin compiler and runtime version 1.1.2-4
 * Android O
 * Android sdk tools 26.0.2
 * Android sdk build-tools 26 rc2
 * Android sdk plateform-toools 26.0.0 rc2
 

Projects
===================================================================
Name | Demo                                                         
--- | ---                                                          
Google io17 Architecture ViewModels Livedata Sample | <img src="/gifs/googleio2017.gif" width="59%">
Audio MP3 Player | <img src="/gifs/audioplayer.gif" width="59%">
Location (If using emulator then send the location from Extended Controls of Emulator.) | <img src="/gifs/location.gif" width="59%">
Sqlite | <img src="/gifs/database.gif" width="59%">
Video Streaming (Always use device to run this App since emulator has rendering issues) | <img src="/gifs/videostreaming.gif" width="59%">
Custom Camera | <img src="/gifs/customcamera.gif" width="59%">
Android-O Notifications | <img src="/gifs/o_notification_003.gif" width="59%">
Compass | <img src="/gifs/compass.gif" width="59%">
