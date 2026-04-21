# Maps SDK tutorial

## Overview
Maps SDK is an android library that gives the ability to use map API in 
your application. Such features include map interaction, location markers, and polyline to connect
points on the map.

## Getting Started
First, you will need to add the following lines to your Top-level build.gradle.kts plugins:
```kotlin
plugins {
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
    id("com.google.gms.google-services") version "4.4.4" apply false
}
```

Then you will need to add the following lines to app-level build.gradle.kts dependencies:
```kotlin
dependancies {
    implementation("com.google.maps.android:maps-compose:4.4.1")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
}
```

In AndroidManifest.xml add the following user permissions:
```xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

Then add the following lines in the application field:
```xml
    <appication>
        <meta-data
            android:name="com.google.android.gms.version"
            android:value="@integer/google_play_services_version" />
        <meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="${MAPS_API_KEY}" />
    </appication>
```

Finally get an API key from 

## Step-by-Step Coding Instructions


## Further Discussion and Conclusions


## See Also
