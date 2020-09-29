#include <android/log.h>

#define LOG_TAG "NativeRobin"
#define DEBUG

#ifdef __ANDROID__

#  include <android/log.h>

#  define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__))
#  define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__))
#  ifdef DEBUG
#    define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__))
#  else
#    define LOGD(...)
#  endif
#else
#  define LOGE(...)
#  define LOGD(...)
#endif
#define LOG_LINE LOGE("%s: %s:%d", __func__, __FILE__, __LINE__)
