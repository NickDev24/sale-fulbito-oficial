# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# Hide original source file names
-renamesourcefileattribute SourceFile

# Keep Room Database and Entity classes
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keep interface * extends androidx.room.RoomDatabase { *; }
-keep class * extends androidx.room.Dao { *; }
-dontwarn androidx.room.**

# Keep our custom JSON Entities and Models from being renamed or optimized
# since we serialize/deserialize them using reflection/names.
-keep class com.example.data.UserProfile { *; }
-keep class com.example.data.Court { *; }
-keep class com.example.data.Booking { *; }
-keep class com.example.data.MatchSlot { *; }
-keep class com.example.data.CourtReview { *; }
-keep class com.example.data.UserVideoProgress { *; }
-keep class com.example.data.CourtVisit { *; }

# Keep AdMob classes
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# Keep Firebase classes
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Keep OSMDroid classes
-keep class org.osmdroid.** { *; }
-dontwarn org.osmdroid.**

# Keep OkHttp & Okio rules to avoid network issues in release
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**

