# Spacetime Energy Futures ProGuard Rules
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# Retrofit
-keepattributes Signature
-keepattributes Exceptions
-keep class com.spacetime.evcharging.core.network.dto.** { *; }
-keep class com.spacetime.evcharging.data.remote.dto.** { *; }

# Kotlinx Serialization
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keep class com.spacetime.evcharging.** { *; }
-keep,includedescriptorclasses class com.spacetime.evcharging.**$$serializer { *; }

# osmdroid Maps
-dontwarn org.osmdroid.**
-keep class org.osmdroid.** { *; }
