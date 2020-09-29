//
// Created by admin on 2020/9/25.
//

#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_robin_sdk_ndk_JniManager_test(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
