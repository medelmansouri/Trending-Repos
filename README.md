# Trending-Repos
This is a small app provided to show the most stared repositories in github that were created in the last 30 days, using github API.

## Pre-requisites
*	Android Studio
*	Android SDK 15
*	Android Build Tools v26
*	GenyMotion or physical Android phone

## Installation
* Clone this repository and import into Android Studio
```
$ git://github.com/medelmansouri/Trending-Repos.git
```
* Open Genymotion or connect your your Android phone to lunch the App
* Or just install the apk file in this repository to try the Application.
* Here is the dependencies
```
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'com.android.support:appcompat-v7:26.1.0'
    implementation 'com.android.support.constraint:constraint-layout:1.0.2'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.1'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.1'
    compile group: 'org.apache.httpcomponents' , name: 'httpclient-android' , version: '4.3.5.1'
    compile "com.squareup.picasso:picasso:2.5.2"
    compile 'com.android.support:design:26.1.0'
}
```
## The Application
* The main interface that shows List of the most stared github repositories

![1](https://user-images.githubusercontent.com/35071517/34809031-4c2c8952-f68a-11e7-973f-417f21b295c3.PNG)

* Choose between the repositories That was created last mounth, last week or just today.

![2](https://user-images.githubusercontent.com/35071517/34809048-6fd9f79a-f68a-11e7-9959-20689b54fa2b.PNG)

