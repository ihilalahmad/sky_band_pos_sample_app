#include <jni.h>
#include <stdio.h>
#include <string.h>
#include<SBCoreECR.h>
#include <android/log.h>
#define APPNAME "MyApp"

JNIEXPORT jbyteArray JNICALL Java_com_girmiti_skybandecr_sdk_CLibraryLoad_pack(JNIEnv *env, jobject obj,jstring requestData,jint transactionType,jstring szSignature, jstring szEcrBuffer) {

char *inCStr = (*env)->GetStringUTFChars(env, requestData,NULL);
char *inCStr1 = (*env)->GetStringUTFChars(env, szEcrBuffer,NULL);
char *inCStr2 = (*env)->GetStringUTFChars(env, szSignature,NULL);

pack(inCStr, transactionType,inCStr2,inCStr1);
__android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "The output is %s", inCStr1);

int n=strlen(inCStr1);
    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "The value of n is %d",n);
    jbyteArray arr = (*env)->NewByteArray(env, n);
    (*env)->SetByteArrayRegion(env,arr,0,n, (jbyte*)inCStr1);
        __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "arr %d",n);
    return arr;
}

/*JNIEXPORT jbyteArray JNICALL Java_com_girmiti_skybandecr_sdk_CLibraryLoad_parse(JNIEnv *env, jobject obj,jstring respData,jstring respOutData) {

 char *inCStr = (*env)->GetStringUTFChars(env,respData,NULL);
 char *inCStr1 = (*env)->GetStringUTFChars(env,respOutData,NULL);

 parse(inCStr, inCStr1);
 __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "The parse request is %s", inCStr);
 __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "The parse response is %s", inCStr1);

 int n=strlen(inCStr1);
     __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "The value of n is %d",n);
     jbyteArray arr = (*env)->NewByteArray(env, n);
     (*env)->SetByteArrayRegion(env,arr,0,n, (jbyte*)inCStr1);
         __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "arr %d",n);
     return arr;
 }*/