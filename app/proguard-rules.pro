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

-keep class ru.forblitz.statistics.widget.**{ *; }
-keep class ru.forblitz.statistics.dto.**{ *; }

-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response

-keep class org.apache.**{ *; }
-keep interface org.apache.**{ *; }

-dontwarn org.brotli.dec.BrotliInputStream*
-dontwarn org.objectweb.asm.AnnotationVisitor*
-dontwarn org.objectweb.asm.Attribute*
-dontwarn org.objectweb.asm.ClassReader*
-dontwarn org.objectweb.asm.ClassVisitor*
-dontwarn org.objectweb.asm.FieldVisitor*
-dontwarn org.objectweb.asm.Label*
-dontwarn org.objectweb.asm.MethodVisitor*
-dontwarn org.objectweb.asm.Type*
-dontwarn org.tukaani.xz.ARMOptions*
-dontwarn org.tukaani.xz.ARMThumbOptions*
-dontwarn org.tukaani.xz.DeltaOptions*
-dontwarn org.tukaani.xz.FilterOptions*
-dontwarn org.tukaani.xz.FinishableOutputStream*
-dontwarn org.tukaani.xz.FinishableWrapperOutputStream*
-dontwarn org.tukaani.xz.IA64Options*
-dontwarn org.tukaani.xz.LZMA2InputStream*
-dontwarn org.tukaani.xz.LZMA2Options*
-dontwarn org.tukaani.xz.LZMAInputStream*
-dontwarn org.tukaani.xz.LZMAOutputStream*
-dontwarn org.tukaani.xz.MemoryLimitException*
-dontwarn org.tukaani.xz.PowerPCOptions*
-dontwarn org.tukaani.xz.SPARCOptions*
-dontwarn org.tukaani.xz.SingleXZInputStream*
-dontwarn org.tukaani.xz.UnsupportedOptionsException*
-dontwarn org.tukaani.xz.X86Options*
-dontwarn org.tukaani.xz.XZ*
-dontwarn org.tukaani.xz.XZInputStream*
-dontwarn org.tukaani.xz.XZOutputStream*
-dontwarn com.github.luben.zstd.BufferPool*
-dontwarn com.github.luben.zstd.ZstdInputStream*
-dontwarn com.github.luben.zstd.ZstdOutputStream*

-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.