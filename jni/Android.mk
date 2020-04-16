APP_STL := gnustl_static
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := jniWrapper

LOCAL_SRC_FILES := jniWrapper.c \
                   SBCoreECR.c \
                   ECRSrc.c \
                   Utilities.c
LOCAL_LDLIBS := -llog
include $(BUILD_SHARED_LIBRARY)

