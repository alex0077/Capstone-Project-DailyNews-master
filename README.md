# Capstone-Project-DailyNews-master

# Instructions

You need an API key from newsapi.org. Go to the file build.gradle(Module: app) method and replace the string 'YOUR_API_KEY' for your API key.

....
buildTypes {
      release {
          buildConfigField("String", "NEWS_API_KEY", "\"YOUR_API_KEY\"")
          minifyEnabled true
          shrinkResources true
          proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
          signingConfig signingConfigs.config
      }
      debug {
          buildConfigField("String", "NEWS_API_KEY", "\"YOUR_API_KEY\"")
          minifyEnabled false
          shrinkResources false
          proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
      }
  }
  ...
