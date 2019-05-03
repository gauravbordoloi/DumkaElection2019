# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn **
-ignorewarnings

#glide

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#crashlytics
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# Application classes that will be serialized/deserialized over Gson
-keep class com.gadgetsfury.electionindia.announcement.Announcement { <fields>; }
-keep class com.gadgetsfury.electionindia.candidate.Candidate { <fields>; }
-keep class com.gadgetsfury.electionindia.contacts.Contact { <fields>; }
-keep class com.gadgetsfury.electionindia.feeds.Feed { <fields>; }
-keep class com.gadgetsfury.electionindia.poll.PollingStation { <fields>; }
-keep class com.gadgetsfury.electionindia.settings.ServerResponse { <fields>; }
-keep class com.gadgetsfury.electionindia.events.ElectionEvent { <fields>; }
-keep class com.gadgetsfury.electionindia.sveep.rohof.ROHoF { <fields>; }
-keep class com.gadgetsfury.electionindia.sveep.vfeed.VoterFeed { <fields>; }
-keep class com.gadgetsfury.electionindia.sveep.video.Playlist { <fields>; }
-keep class com.gadgetsfury.electionindia.sveep.video.Video { <fields>; }
-keep class com.gadgetsfury.electionindia.sveep.vod.VOD { <fields>; }

#WYSIWYG
-keep class com.google.gson.** { *; }
-dontwarn com.squareup.picasso.**
-dontwarn com.squareup.okhttp.**
-keep public class org.jsoup.** { public *; }