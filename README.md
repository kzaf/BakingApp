# BakingApp
Udacity Android Developer Nanodegree, project 3

## General app usage
 - App display recipes from provided network resource. [json file](https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json)
 - App Navigation. App navigaties between individual recipes and recipe steps.
 - Utilization of RecyclerView. App uses RecyclerView and handle recipe steps that include videos or images.
 - App conforms to common standards found in the Android [Nanodegree General Project Guidelines.](http://udacity.github.io/android-nanodegree-guidelines/core.html)

## Components and Libraries
 - Application uses Master Detail Flow to display recipe steps and navigation between them.
 - Application uses Exoplayer to display videos.
 - Application properly initializes and releases video assets when appropriate.
 - Application should properly retrieve media assets from the provided network links. It should properly handle network requests.
 - Application makes use of Espresso to test aspects of the UI.
 - Application sensibly utilizes a third-party library to enhance the app's features. That could be helper library to interface with Content Providers if you choose to store the recipes, a UI binding library to avoid writing findViewById a bunch of times, or something similar.
 
## Used Libraries
 - [Retrofit2 with Gson converter](https://github.com/square/retrofit)
 - [OkHttp](https://github.com/square/okhttp)
 - [ButterKnife](https://github.com/JakeWharton/butterknife)
 - [Picasso](https://github.com/square/picasso)
 - [Gson](https://github.com/google/gson)
 - [ExoPlayer](https://github.com/google/ExoPlayer)

## Screenshot
![BeFunky-collage (1)](https://user-images.githubusercontent.com/11368889/59105147-fb145400-893b-11e9-9e40-c83eb04845a9.jpg)
