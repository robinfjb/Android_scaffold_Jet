
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <jni.h>
#include "logger.h"

char* RELEASE_SIGN="308202e2308201ca020101300d06092a864886f70d010105050030373116301406035504030c0d416e64726f69642044656275673110300e060355040a0c07416e64726f6964310b3009060355040613025553301e170d3139303930353035353535395a170d3439303832383035353535395a30373116301406035504030c0d416e64726f69642044656275673110300e060355040a0c07416e64726f6964310b300906035504061302555330820122300d06092a864886f70d01010105000382010f003082010a0282010100a5af8fde01c5ca1967747582ee8776292bbfe5c7d0f3a0b44e185fabd394780cb14339115452ecadab518df37754bbc6597259d11b207d7c5c15562b9e5967147589076b461fa7a17e669076718d067718967569e91491e72a90ca28dc96248113d839055d922145bc9140695a0f53aac6635c8f87b137898b7b0b7ab764ee8910ceac43c6492c56629f12b3f74528119fe165b1226079429b2c8b79f12917d862b4160a092cdb365f1cc1ff3e24442b62ed07307cf6ee6565b529b78b66cba28150f7e558f68ac5901722e5d27966a5129d0c833d3697a265bf9c0bf7872a46f2dc8d22e5d0e82e44de91196013abf767e321d9a5e619589922de3623aee68b0203010001300d06092a864886f70d010105050003820101004a43ff0209efdae83eea00b9712d1c59cacfe3e89beffd57379b04ce9d591643033cb7fa37d3deadd2a8a19db639412393e060b1dde9398f2133e4cb255d55144a463e4434661e262a5c9e3d2b7ff9706f6a59fd64ae968a6ef166bcabdba6bc23d6351f03f7dc44787be6e969046b1059040e6fd2cb58d77549e937343865f4208be64a69d85641fffc9b7b08f2e43da19eb1abde900a45bd55562e1f3c28658d3ef5801ef51a960f686c09e1b90efde976da6b3295dfd5ca0d20a533ba458154967aa28872dbbf498a27d8e0791db0f47b82fbb4a3bbf20ab0ba9f536d9864db21bea4e3a9afb05829d986db56e368e5d6a92514d70e5ba8067edf62565b8d";
char* PUBLIC_KEY = "NNJX9Py7LHmPn109Sp2XqmOVu77OKlgK/x7dK3YKa/qNN93bUF9vnoare6jMscqZPC1WDP9By2Aq\nzA3CZS6p5U62XIxI2TPl41DWDFPY6LBKaFAaQeyrEna0r9hYwSPTg/2lHbZ60LLN2NVdmxY5bRlF\nD6Cfs/drlS132d+mtTn3Bg9ncvrQFDfZ7sn4eSM+Hg5zjI2UDO3v6ihch2wbhnLZq5UeQXy/mnJL\nuiBqZl2ENVIdtBlFlAa4BA/kyfD1rSxi9SvYLtzlNS4i2n31uTmwQCwPHahoSuf413CaPiE=";
char* SECRET = "123456";
char* DES_KEY = "UJAS$_ON921asd1np%LJ5adw";
char* APK_SIGN_ERROR = "签名不一致";

const char *getSignString(JNIEnv *pEnv, jobject pJobject);

extern "C" JNIEXPORT jstring JNICALL
Java_robin_sdk_ndk_JniManager_getPublicKey(JNIEnv *env, jobject obj, jobject contextObject) {
    const char* signStrng =  getSignString(env,contextObject);
    if(strcasecmp(signStrng,RELEASE_SIGN)==0)//签名一致  返回合法的 api key，否则返回错误
    {
        return (env)->NewStringUTF(PUBLIC_KEY);
    }else
    {
     LOGE(APK_SIGN_ERROR,"");
           return NULL;
    }
}

extern "C" JNIEXPORT jstring JNICALL
Java_robin_sdk_ndk_JniManager_getSecret(JNIEnv *env, jobject obj, jobject contextObject) {
    const char* signStrng =  getSignString(env,contextObject);
    if(strcasecmp(signStrng,RELEASE_SIGN)==0)//签名一致  返回合法的 api key，否则返回错误
    {
        return (env)->NewStringUTF(SECRET);
    }else
    {
     LOGE(APK_SIGN_ERROR,"");
           return NULL;
    }
}

extern "C" JNIEXPORT jstring JNICALL
Java_robin_sdk_ndk_JniManager_getDesKey(JNIEnv *env, jobject obj, jobject contextObject) {
    const char* signStrng =  getSignString(env,contextObject);
    if(strcasecmp(signStrng,RELEASE_SIGN)==0)//签名一致  返回合法的 api key，否则返回错误
    {
        return (env)->NewStringUTF(DES_KEY);
    }else
    {
     LOGE(APK_SIGN_ERROR,"");
           return NULL;
    }
}

const char* getSignString(JNIEnv *env,jobject context_object) {
        jclass context_class = env->GetObjectClass(context_object);

        //context.getPackageManager()
        jmethodID methodId = env->GetMethodID(context_class, "getPackageManager", "()Landroid/content/pm/PackageManager;");
        jobject package_manager_object = env->CallObjectMethod(context_object, methodId);
        if (package_manager_object == NULL) {
            __android_log_print(ANDROID_LOG_INFO, "JNITag","getPackageManager() Failed!");
            return NULL;
        }

        //context.getPackageName()
        methodId = env->GetMethodID(context_class, "getPackageName", "()Ljava/lang/String;");
        jstring package_name_string = (jstring)env->CallObjectMethod(context_object, methodId);
        if (package_name_string == NULL) {
            __android_log_print(ANDROID_LOG_INFO, "JNITag","getPackageName() Failed!");
            return NULL;
        }

        env->DeleteLocalRef(context_class);

        //PackageManager.getPackageInfo(Sting, int)
        jclass pack_manager_class = env->GetObjectClass(package_manager_object);
        methodId = env->GetMethodID(pack_manager_class, "getPackageInfo", "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
        env->DeleteLocalRef(pack_manager_class);
        jobject package_info_object = env->CallObjectMethod(package_manager_object, methodId, package_name_string, 64);
        if (package_info_object == NULL) {
            __android_log_print(ANDROID_LOG_INFO, "JNITag","getPackageInfo() Failed!");
            return NULL;
        }

        env->DeleteLocalRef(package_manager_object);

        //PackageInfo.signatures[0]
        jclass package_info_class = env->GetObjectClass(package_info_object);
        jfieldID fieldId = env->GetFieldID(package_info_class, "signatures", "[Landroid/content/pm/Signature;");
        env->DeleteLocalRef(package_info_class);
        jobjectArray signature_object_array = (jobjectArray)env->GetObjectField(package_info_object, fieldId);
        if (signature_object_array == NULL) {
            __android_log_print(ANDROID_LOG_INFO, "JNITag","PackageInfo.signatures[] is null");
            return NULL;
        }
        jobject signature_object = env->GetObjectArrayElement(signature_object_array, 0);

        env->DeleteLocalRef(package_info_object);

        //Signature.toCharsString()
        jclass signature_class = env->GetObjectClass(signature_object);
        methodId = env->GetMethodID(signature_class, "toCharsString", "()Ljava/lang/String;");
        env->DeleteLocalRef(signature_class);
        jstring signature_string = (jstring) env->CallObjectMethod(signature_object, methodId);

        return (env)->GetStringUTFChars(signature_string, 0);

//                jclass tem_class;
//                 jmethodID tem_method;
//                 jclass class_context = env->GetObjectClass(context);
//                 //      PackageInfo localPackageInfo = context.getPackageManager()
//                //              .getPackageInfo(context.getPackageName(), 64);
//                 tem_method = env->GetMethodID(class_context, "getPackageManager", "()Landroid/content/pm/PackageManager;");
//               jobject obj_package_manager = env->CallObjectMethod(context, tem_method);
//              // getPackageName
//                tem_method = env->GetMethodID(class_context, "getPackageName", "()Ljava/lang/String;");
//                jobject obj_package_name = env->CallObjectMethod(context, tem_method);
//                // getPackageInfo
//                tem_class = env->GetObjectClass(obj_package_manager);
//                tem_method = env->GetMethodID(tem_class, "getPackageInfo", "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
//                jobject obj_package_info = env->CallObjectMethod(obj_package_manager, tem_method, obj_package_name, 64);
//
//                // Signature[] arrayOfSignature = localPackageInfo.signatures;
//                // Signature localSignature = arrayOfSignature[0];
//                tem_class = env->GetObjectClass(obj_package_info);
//                jfieldID fieldID_signatures = env->GetFieldID(tem_class, "signatures", "[Landroid/content/pm/Signature;");
//                jobjectArray signatures = (jobjectArray)env->GetObjectField(obj_package_info, fieldID_signatures);
//                jobject signature = env->GetObjectArrayElement(signatures, 0);
//                // localSignature.toByteArray()
//                tem_class = env->GetObjectClass(signature);
//                tem_method = env->GetMethodID(tem_class, "toByteArray", "()[B");
//                jobject obj_sign_byte_array = env->CallObjectMethod(signature, tem_method);// 这个就是拿到的签名byte数组
//
//                //      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
//                jclass class_MessageDigest = env->FindClass("java/security/MessageDigest");
//                tem_method = env->GetStaticMethodID(class_MessageDigest, "getInstance", "(Ljava/lang/String;)Ljava/security/MessageDigest;");
//                jobject obj_md5 = env->CallStaticObjectMethod(class_MessageDigest, tem_method, env->NewStringUTF("md5"));
//                //      localMessageDigest.update(localSignature.toByteArray());
//                //tem_class = env->GetObjectClass(env, obj_md5);
//                tem_method = env->GetMethodID(class_MessageDigest, "update", "([B)V");// 这个函数的返回值是void，写V
//                env->CallVoidMethod(obj_md5, tem_method, obj_sign_byte_array);
//                // localMessageDigest.digest()
//                tem_method = env->GetMethodID(class_MessageDigest, "digest", "()[B");
//                // 这个是md5以后的byte数组，现在只要将它转换成16进制字符串，就可以和之前的比较了
//                jbyteArray obj_array_sign = (jbyteArray)env->CallObjectMethod(obj_md5, tem_method);// jni中有强转类型的概念吗
//                //      // 这个就是签名的md5值
//                //      String str2 = toHex(localMessageDigest.digest());
//
//                // 尝试用c写一下：http://blog.csdn.net/pingd/article/details/41945417
//                jsize int_array_length = env->GetArrayLength(obj_array_sign);
//                jbyte* byte_array_elements = env->GetByteArrayElements(obj_array_sign, JNI_FALSE);
//                char* char_result = (char*) malloc(int_array_length*2+1);// 开始没有+1，在有的情况下会越界产生问题，还是在后面补上\0比较好
//                // 将byte数组转换成16进制字符串，发现这里不用强转，jbyte和unsigned char应该字节数是一样的
//                ByteToHexStr((char*)byte_array_elements, char_result, int_array_length);
//                *(char_result+int_array_length*2) = '\0';// 在末尾补\0
//                jstring string_result = env->NewStringUTF(char_result);
//                // release
//                env->ReleaseByteArrayElements(obj_array_sign, byte_array_elements, JNI_ABORT);
//                // 释放指针使用free
//                free(char_result);
//                return (env)->GetStringUTFChars(string_result, 0);


}



void ByteToHexStr(char* source, char* dest, int sourceLen)
{
    short i;
    unsigned char highByte, lowByte;

    for (i = 0; i < sourceLen; i++)
    {
        highByte = source[i] >> 4;
        lowByte = source[i] & 0x0f;

        highByte += 0x30;

        if (highByte > 0x39)
                dest[i * 2] = highByte + 0x07;
        else
                dest[i * 2] = highByte;

        lowByte += 0x30;
        if (lowByte > 0x39)
            dest[i * 2 + 1] = lowByte + 0x07;
        else
            dest[i * 2 + 1] = lowByte;
    }
    return;
}
