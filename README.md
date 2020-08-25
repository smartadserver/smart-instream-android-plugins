# Smart AdServer — Android Instream SDK Content Player Plugins

This repository contains implementations of the ```SVSContentPlayerPlugin``` interface needed by _Smart Android Instream SDK_
for some majors content video SDKs for Android :

- ExoPlayer ⃰
- JWPlayer ⃰
- Brightcove
- Ooyala
- Android legacy VideoView

They connect the _Smart Android Instream SDK_ with the corresponding content player on which the ad is to be displayed.
Broadly speaking, they respond to callbacks (typically ```adBreakStarted``` and ```adBreakEnded``` methods when the content needs to be paused),
provide playback information (```getContentDuration```, ```getCurrentPosition```, ```isPlaying```) to the instream SDK and also return the ```ViewGroup```
containing the content player for proper ad display on top.

⃰ Note that the JWPlayer SDK is currently supported up to 2.7.13 version.
Later releases (2.8+) integrate Exoplayer v2 that currently conflicts with externally imported Exoplayer v2 required by Smart instream SDK.


## Requirements

* A _Smart AdServer_ account
* _Android Studio 3.0_ or higher
* _Android 4.4 (API Level 19)_ or higher
* For each plugin, the corresponding content player SDK needs to be imported in the application

## Installation

For installation and getting started, please check [the complete documentation](http://documentation.smartadserver.com/instreamSDK/).
You can also check the [samples](https://github.com/smartadserver/smart-instream-android-samples).
