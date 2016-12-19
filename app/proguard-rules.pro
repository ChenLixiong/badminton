# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\developerClass\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
 #如果有引用v4包可以添加下面这行
    -keep public class * extends android.support.v4.app.Fragment
 #忽略警告
    -ignorewarning
     #如果引用了v4或者v7包
        -dontwarn android.support.**
        -keep public class * extends android.view.View {
                public <init>(android.content.Context);
                public <init>(android.content.Context, android.util.AttributeSet);
                public <init>(android.content.Context, android.util.AttributeSet, int);
                public void set*(...);
            }
             #保持 native 方法不被混淆
                -keepclasseswithmembernames class * {
                    native <methods>;
                }
                 -keepclasseswithmembers class * {
                        public <init>(android.content.Context, android.util.AttributeSet);
                    }
                    -keepclassmembers class * extends android.app.Activity {
                            public void *(android.view.View);
                         }
 -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }

    #保持 Serializable 不被混淆
    -keepnames class * implements java.io.Serializable

    #保持 Serializable 不被混淆并且enum 类也不被混淆
    -keepclassmembers class * implements java.io.Serializable {
        static final long serialVersionUID;
        private static final java.io.ObjectStreamField[] serialPersistentFields;
        !static !transient <fields>;
        !private <fields>;
        !private <methods>;
        private void writeObject(java.io.ObjectOutputStream);
        private void readObject(java.io.ObjectInputStream);
        java.lang.Object writeReplace();
        java.lang.Object readResolve();
    }
-keep public class * extends android.view.View {
        public <init>(android.content.Context);
        public <init>(android.content.Context, android.util.AttributeSet);
        public <init>(android.content.Context, android.util.AttributeSet, int);
        public void set*(...);
    }