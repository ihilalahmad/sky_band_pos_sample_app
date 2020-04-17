#include <jni.h>
#include <stdio.h>
#include <string.h>
#include "SBCoreECR.h"
#include <android/log.h>
#include <malloc.h>

#define APPNAME "Skyband ECR Core"

JNIEXPORT jbyteArray JNICALL
Java_com_girmiti_skybandecr_sdk_CLibraryLoad_pack(JNIEnv *env, jobject obj, jstring requestData,
                                                  jint transactionType, jstring szSignature,
                                                  jstring szEcrBuffer) {

    char *inRequestStr = (*env)->GetStringUTFChars(env, requestData, NULL);
    char *inResponseStr = (*env)->GetStringUTFChars(env, szEcrBuffer, NULL);
    char *inSignStr = (*env)->GetStringUTFChars(env, szSignature, NULL);

    // create request packet from core
    pack(inRequestStr, transactionType, inSignStr, inResponseStr);

    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "The output is %s", inResponseStr);

    int n = strlen(inResponseStr);

    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "The value of n is %d", n);

    jbyteArray arr = (*env)->NewByteArray(env, n);

    // return
    (*env)->SetByteArrayRegion(env, arr, 0, n, (jbyte *) inResponseStr);

    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "arr %d", n);

    free(inRequestStr);
    free(inResponseStr);
    free(inSignStr);

    return arr;
}

JNIEXPORT jbyteArray JNICALL
Java_com_girmiti_skybandecr_sdk_CLibraryLoad_parse(JNIEnv *env, jobject obj, jstring respData,
                                                   jstring respOutData) {

    char *inRequestStr = (*env)->GetStringUTFChars(env, respData, NULL);
    char *inResponseStr = (*env)->GetStringUTFChars(env, respOutData, NULL);

    // parse the request
    parse(inRequestStr, inResponseStr);

    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "The parse request is %s", inRequestStr);
    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "The parse response is %s", inResponseStr);

    int n = strlen(inResponseStr);

    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "The value of n is %d", n);

    jbyteArray arr = (*env)->NewByteArray(env, n);

    (*env)->SetByteArrayRegion(env, arr, 0, n, (jbyte *) inResponseStr);

    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "arr %d", n);

    return arr;
}