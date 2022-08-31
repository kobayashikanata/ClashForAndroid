#include <jni.h>
#include <stdint.h>
#include <stddef.h>
#include <string.h>

#include "bridge_helper.h"
#include "libclash.h"
#include "jni_helper.h"
#include "trace.h"

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeInit(JNIEnv *env, jobject thiz,
                                                          jstring home,
                                                          jstring version_name, jint sdk_version) {
    TRACE_METHOD();

    scoped_string _home = get_string(home);
    scoped_string _version_name = get_string(version_name);

    coreInit(_home, _version_name, sdk_version);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeReset(JNIEnv *env, jobject thiz) {
    TRACE_METHOD();

    reset();
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeForceGc(JNIEnv *env, jobject thiz) {
    TRACE_METHOD();

    forceGc();
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeSuspend(JNIEnv *env, jobject thiz,
                                                             jboolean suspended) {
    TRACE_METHOD();

    suspend((int) suspended);
}


JNIEXPORT jstring JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeQueryTunnelState(JNIEnv *env, jobject thiz) {
    TRACE_METHOD();

    scoped_string response = queryTunnelState();

    return new_string(response);
}

JNIEXPORT jlong JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeQueryTrafficNow(JNIEnv *env, jobject thiz) {
    TRACE_METHOD();

    uint64_t upload = 0l, download = 0l;

    queryNow(&upload, &download);

    return (jlong) (down_scale_traffic(upload) << 32u | down_scale_traffic(download));
}

JNIEXPORT jlong JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeQueryTrafficTotal(JNIEnv *env, jobject thiz) {
    TRACE_METHOD();

    uint64_t upload = 0l, download = 0l;

    queryTotal(&upload, &download);

    return (jlong) (down_scale_traffic(upload) << 32u | down_scale_traffic(download));
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeNotifyDnsChanged(JNIEnv *env, jobject thiz,
                                                                      jstring dns_list) {
    TRACE_METHOD();

    scoped_string _dns_list = get_string(dns_list);

    notifyDnsChanged(_dns_list);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeNotifyTimeZoneChanged(JNIEnv *env, jobject thiz,
                                                                           jstring name, jint offset) {
    TRACE_METHOD();

    scoped_string _name = get_string(name);

    notifyTimeZoneChanged(_name, offset);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeNotifyInstalledAppChanged(JNIEnv *env,
                                                                               jobject thiz,
                                                                               jstring uid_list) {
    TRACE_METHOD();

    scoped_string _uid_list = get_string(uid_list);

    notifyInstalledAppsChanged(_uid_list);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeStartTun(JNIEnv *env, jobject thiz,
                                                              jint fd,
                                                              jstring gateway,
                                                              jstring portal,
                                                              jstring dns,
                                                              jobject cb) {
    TRACE_METHOD();

    scoped_string _gateway = get_string(gateway);
    scoped_string _portal = get_string(portal);
    scoped_string _dns = get_string(dns);
    jobject _interface = new_global(cb);

    startTun(fd, _gateway, _portal, _dns, _interface);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeStopTun(JNIEnv *env, jobject thiz) {
    TRACE_METHOD();

    stopTun();
}

JNIEXPORT jstring JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeStartHttp(JNIEnv *env, jobject thiz,
                                                               jstring listen_at) {
    TRACE_METHOD();

    scoped_string _listen_at = get_string(listen_at);

    scoped_string listened = startHttp(_listen_at);

    if (listened == NULL)
        return NULL;

    return new_string(listened);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeStopHttp(JNIEnv *env, jobject thiz) {
    TRACE_METHOD();

    stopHttp();
}

JNIEXPORT jstring JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeQueryGroupNames(JNIEnv *env, jobject thiz,
                                                                     jboolean exclude_not_selectable) {
    TRACE_METHOD();

    scoped_string response = queryGroupNames((int) exclude_not_selectable);

    return new_string(response);
}

JNIEXPORT jstring JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeQueryGroup(JNIEnv *env, jobject thiz,
                                                                jstring name, jstring mode) {
    TRACE_METHOD();

    scoped_string _name = get_string(name);
    scoped_string _mode = get_string(mode);

    scoped_string response = queryGroup(_name, _mode);

    if (response == NULL)
        return NULL;

    return new_string(response);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeHealthCheck(JNIEnv *env, jobject thiz,
                                                                 jobject completable,
                                                                 jstring name) {
    TRACE_METHOD();

    jobject _completable = new_global(completable);
    scoped_string _name = get_string(name);

    healthCheck(_completable, _name);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeHealthCheckAll(JNIEnv *env, jobject thiz) {
    TRACE_METHOD();

    healthCheckAll();
}

JNIEXPORT jboolean JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativePatchSelector(JNIEnv *env, jobject thiz,
                                                                   jstring selector, jstring name) {
    TRACE_METHOD();

    scoped_string _selector = get_string(selector);
    scoped_string _name = get_string(name);

    return (jboolean) patchSelector(_selector, _name);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeLoad(JNIEnv *env, jobject thiz,
                                                          jobject completable, jstring path) {
    TRACE_METHOD();

    jobject _completable = new_global(completable);
    scoped_string _path = get_string(path);

    load(_completable, _path);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeFetchAndValid(JNIEnv *env, jobject thiz,
                                                                   jobject callback,
                                                                   jstring path,
                                                                   jstring url, jboolean force) {
    TRACE_METHOD();

    jobject _completable = new_global(callback);
    scoped_string _path = get_string(path);
    scoped_string _url = get_string(url);

    fetchAndValid(_completable, _path, _url, force);
}

JNIEXPORT jstring JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeQueryProviders(JNIEnv *env, jobject thiz) {
    TRACE_METHOD();

    scoped_string response = queryProviders();

    return new_string(response);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeUpdateProvider(JNIEnv *env, jobject thiz,
                                                                    jobject completable,
                                                                    jstring type,
                                                                    jstring name) {
    TRACE_METHOD();

    jobject _completable = new_global(completable);
    scoped_string _type = get_string(type);
    scoped_string _name = get_string(name);

    updateProvider(_completable, _type, _name);
}

JNIEXPORT jstring JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeReadOverride(JNIEnv *env, jobject thiz,
                                                                  jint slot) {
    TRACE_METHOD();

    scoped_string response = readOverride(slot);

    return new_string(response);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeWriteOverride(JNIEnv *env, jobject thiz,
                                                                   jint slot,
                                                                   jstring content) {
    TRACE_METHOD();

    scoped_string _content = get_string(content);

    writeOverride(slot, _content);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeClearOverride(JNIEnv *env, jobject thiz,
                                                                   jint slot) {
    TRACE_METHOD();

    clearOverride(slot);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeInstallSideloadGeoip(JNIEnv *env, jobject thiz,
                                                                          jbyteArray data) {
    TRACE_METHOD();

    if (data == NULL) {
        installSideloadGeoip(NULL, 0);

        return;
    }

    jbyte *bytes = (*env)->GetByteArrayElements(env, data, NULL);
    int size = (*env)->GetArrayLength(env, data);

    scoped_string err = installSideloadGeoip(bytes, size);

    (*env)->ReleaseByteArrayElements(env, data, bytes, JNI_ABORT);

    if (err != NULL) {
        (*env)->ThrowNew(
                env,
                find_class("com/github/kr328/clash/core/bridge/ClashException"),
                err
        );
    }
}

JNIEXPORT jstring JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeQueryConfiguration(JNIEnv *env, jobject thiz) {
    TRACE_METHOD();

    scoped_string response = queryConfiguration();

    return new_string(response);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeSubscribeLogcat(JNIEnv *env, jobject thiz,
                                                                     jobject callback) {
    TRACE_METHOD();

    jobject _callback = new_global(callback);

    subscribeLogcat(_callback);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_patchAddBypassIp(JNIEnv *env, jobject thiz, jstring ip) {

    TRACE_METHOD();

    scoped_string _ip = get_string(ip);

    patchAddBypassIp(_ip);
}


JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeTcpTest(JNIEnv *env, jobject thiz,
                                                             jstring host,
                                                             jint timeout,
                                                             jint maxCount,
                                                             jint tag,
                                                             jboolean send64Bytes,
                                                             jobject callback) {

    TRACE_METHOD();

    jobject _callback = new_global(callback);
    scoped_string _host = get_string(host);

    tcpTest(_host, timeout, maxCount, tag, send64Bytes, _callback);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeTcpTestCancel(JNIEnv *env, jobject thiz, jint tag) {

    TRACE_METHOD();

    tcpTestCancel(tag);
}

JNIEXPORT jstring JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeTcpPing(JNIEnv *env, jobject thiz,
                                                             jstring host,
                                                             jint pingCount,
                                                             jint timeout,
                                                             jint interval,
                                                             jint groupCount,
                                                             jboolean checkAlive) {

    TRACE_METHOD();

    scoped_string _host = get_string(host);
    scoped_string response = tcpPing(_host, pingCount, timeout, interval, groupCount, checkAlive);
    if (response == NULL)
        return NULL;

    return new_string(response);
}

JNIEXPORT jstring JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeUdpPing(JNIEnv *env, jobject thiz,
                                                             jstring addr,
                                                             jint count,
                                                             jint timeout,
                                                             jint packetLength) {

    TRACE_METHOD();

    scoped_string _addr = get_string(addr);
    scoped_string response = udpPing(_addr, count, timeout, packetLength);
    if (response == NULL)
        return NULL;

    return new_string(response);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_patchStartTrojanByJson(JNIEnv *env, jobject thiz,jstring json) {

    TRACE_METHOD();

    scoped_string _json = get_string(json);
    patchStartTrojanWithJson(_json);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_patchStopTrojan(JNIEnv *env, jobject thiz) {

    TRACE_METHOD();

    patchStopTrojan();
}

JNIEXPORT jstring JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_patchStartSSWithJson(JNIEnv *env, jobject thiz, jstring json) {

    TRACE_METHOD();

    scoped_string _json = get_string(json);
    scoped_string response = patchStartSSWithJson(_json);
    if (response == NULL)
        return NULL;

    return new_string(response);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_patchStopSS(JNIEnv *env, jobject thiz, jint tag) {

    TRACE_METHOD();

    patchStopSS(tag);
}

JNIEXPORT jstring JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_patchStartGtsWithFd(JNIEnv *env, jobject thiz,
                                                                   jstring configJson,
                                                                   jint fd,
                                                                   jstring toolsJson,
                                                                   jobject packetFlow) {

    TRACE_METHOD();

    scoped_string _configJson = get_string(configJson);
    scoped_string _toolsJson = get_string(toolsJson);
    jobject _callback = new_global(packetFlow);

    scoped_string response = patchStartGtsWithFd(_configJson, fd, _toolsJson, _callback);
    if (response == NULL)
        return NULL;

    return new_string(response);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_patchStopGts(JNIEnv *env, jobject thiz) {

    TRACE_METHOD();

    patchStopGts();
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_patchSetGCPercent(JNIEnv *env, jobject thiz, jint percent) {

    TRACE_METHOD();

    patchSetGCPercent(percent);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_patchGC(JNIEnv *env, jobject thiz) {

    TRACE_METHOD();

    patchGC();
}

JNIEXPORT jstring JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_patchFinishLog(JNIEnv *env, jobject thiz) {

    TRACE_METHOD();

    scoped_string response = patchFinishLog();
    if (response == NULL)
        return NULL;

    return new_string(response);
}

JNIEXPORT jint JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_patchGetAllocMem(JNIEnv *env, jobject thiz) {

    TRACE_METHOD();

    return patchGetAllocMem();
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeSubscribeEvent(JNIEnv *env, jobject thiz,
                                                                    jobject callback) {
    TRACE_METHOD();

    jobject _callback = new_global(callback);

    subscribeEvent(_callback);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeDiscoverHost(JNIEnv *env, jobject thiz,
                                                                  jstring hosts,
                                                                  jint timeout,
                                                                  jint maxCount,
                                                                  jint tag,
                                                                  jobject callback) {

    TRACE_METHOD();

    scoped_string _hosts = get_string(hosts);
    jobject _callback = new_global(callback);
    discoverHost(_hosts, timeout, maxCount, tag, _callback);
}

JNIEXPORT void JNICALL
Java_com_github_kr328_clash_core_bridge_Bridge_nativeStopDiscoverHost(JNIEnv *env, jobject thiz, jint tag) {
    TRACE_METHOD();

    stopDiscoverHost(tag);
}

static jmethodID m_tun_interface_mark_socket;
static jmethodID m_tun_interface_query_socket_uid;
static jmethodID m_completable_complete;
static jmethodID m_completable_complete_exceptionally;
static jmethodID m_logcat_interface_received;
static jmethodID m_clash_exception;
static jmethodID m_fetch_callback_report;
static jmethodID m_fetch_callback_complete;
static jmethodID m_open;
static jmethodID m_get_message;
static jclass c_clash_exception;
static jclass c_content;
static jobject o_unit;

static jmethodID java_i_gts_packet_flow_m_output_packet;
static jmethodID java_i_gts_packet_flow_m_update_fd;
static jmethodID java_i_gts_packet_flow_m_can_reconnect;
static jmethodID java_i_host_api_test_consumer_m_call;
static jmethodID java_i_string_func_m_bool_call;
static jmethodID java_i_string_action_m_void_call;

static void call_tun_interface_mark_socket_impl(void *tun_interface, int fd) {
    TRACE_METHOD();

    ATTACH_JNI();

    (*env)->CallVoidMethod(env, (jobject) tun_interface,
                           (jmethodID) m_tun_interface_mark_socket,
                           (jint) fd);
}

static int call_tun_interface_query_socket_uid_impl(void *tun_interface, int protocol,
                                                    const char *source, const char *target) {
    TRACE_METHOD();

    ATTACH_JNI();

    return (*env)->CallIntMethod(env, (jobject) tun_interface,
                                 (jmethodID) m_tun_interface_query_socket_uid,
                                 (jint) protocol,
                                 (jstring) new_string(source),
                                 (jstring) new_string(target));
}

static void call_completable_complete_impl(void *completable, const char *exception) {
    TRACE_METHOD();

    ATTACH_JNI();

    if (exception == NULL) {
        (*env)->CallBooleanMethod(env,
                                  (jobject) completable,
                                  (jmethodID) m_completable_complete,
                                  (jobject) o_unit);
    } else {
        jthrowable _exception = (jthrowable)
                (*env)->NewObject(env,
                                  (jclass) c_clash_exception,
                                  (jmethodID) m_clash_exception,
                                  (jstring) new_string(exception)
                );

        (*env)->CallBooleanMethod(env,
                                  (jobject) completable,
                                  (jmethodID) m_completable_complete_exceptionally,
                                  (jobject) _exception);
    }
}

static void call_fetch_callback_report_impl(void *fetch_callback, const char *status_json) {
    TRACE_METHOD();

    ATTACH_JNI();

    jstring _status_json = new_string(status_json);

    (*env)->CallVoidMethod(env,
                           (jobject) fetch_callback,
                           (jmethodID) m_fetch_callback_report,
                           (jstring) _status_json);
}

static void call_fetch_callback_complete_impl(void *fetch_callback, const char *error) {
    TRACE_METHOD();

    ATTACH_JNI();

    jstring _error = NULL;

    if (error != NULL)
        _error = new_string(error);

    (*env)->CallVoidMethod(env,
                           (jobject) fetch_callback,
                           (jmethodID) m_fetch_callback_complete,
                           (jstring) _error);
}

static void invoke_java_i_string_action_void_call_impl(void *callback, const char *payload) {
    TRACE_METHOD();

    ATTACH_JNI();

    jstring _payload = NULL;
    if (payload != NULL)
        _payload = new_string(payload);

    (*env)->CallVoidMethod(env,
                           (jobject) callback,
                           (jmethodID) java_i_string_action_m_void_call,
                           (jstring) _payload);
}

static int invoke_java_i_string_func_bool_call_impl(void *callback, const char *payload) {
    TRACE_METHOD();

    ATTACH_JNI();

    jstring _payload = NULL;
    if (payload != NULL)
        _payload = new_string(payload);

    jboolean result = (*env)->CallBooleanMethod(env,
                           (jobject) callback,
                           (jmethodID) java_i_string_func_m_bool_call,
                           (jstring) _payload);
    if(result == JNI_TRUE)
        return 1;
    return 0;
}

static void invoke_java_i_gts_packet_flow_output_packet_impl(void *callback, const char *payload) {
    TRACE_METHOD();

    ATTACH_JNI();

    (*env)->CallVoidMethod(env,
                           (jobject) callback,
                           (jmethodID) java_i_gts_packet_flow_m_output_packet,
                           (jbyteArray) payload);
}

static void invoke_java_i_gts_packet_flow_update_fd_impl(void *callback, int fd) {
    TRACE_METHOD();

    ATTACH_JNI();

    (*env)->CallVoidMethod(env,
                           (jobject) callback,
                           (jmethodID) java_i_gts_packet_flow_m_update_fd,
                           (jint) fd);
}

static int invoke_java_i_gts_packet_flow_can_reconnect_impl(void *callback) {
    TRACE_METHOD();

    ATTACH_JNI();

    jboolean result = (*env)->CallBooleanMethod(env,
                           (jobject) callback,
                           (jmethodID) java_i_gts_packet_flow_m_can_reconnect);
    if(result == JNI_TRUE)
        return 1;
    return 0;
}

static int invoke_java_i_host_api_test_consumer_call_impl(void *callback, const char *host, int time,
                                                          const char *error, const char *result) {
    TRACE_METHOD();

    ATTACH_JNI();

    jstring _host = NULL;
    if (host != NULL)
        _host = new_string(host);

    jstring _error = NULL;
    if (error != NULL)
        _error = new_string(error);

    jstring _result = NULL;
    if (result != NULL)
        _result = new_string(result);

    if (callback == NULL) {
        return 0;
    }

    jboolean returnResult = (*env)->CallBooleanMethod(env, (jobject) callback,
                                                      (jmethodID) java_i_host_api_test_consumer_m_call,
                                                      _host, (jint) time, _error, _result);
    if(returnResult == JNI_TRUE)
        return 1;
    return 0;
}

static int call_logcat_interface_received_impl(void *callback, const char *payload) {
    TRACE_METHOD();

    ATTACH_JNI();

    (*env)->CallVoidMethod(env,
                           (jobject) callback,
                           (jmethodID) m_logcat_interface_received,
                           (jstring) new_string(payload));

    if (jni_catch_exception(env)) {
        return 1;
    }

    return 0;
}

static int open_content_impl(const char *url, char *error, int error_length) {
    TRACE_METHOD();

    ATTACH_JNI();

    int fd = (*env)->CallStaticIntMethod(env, c_content, m_open, new_string(url));

    if ((*env)->ExceptionCheck(env)) {
        jthrowable exception = (*env)->ExceptionOccurred(env);

        (*env)->ExceptionClear(env);

        jstring message = (jstring) (*env)->CallObjectMethod(
                env,
                (jthrowable) exception,
                (jmethodID) m_get_message
        );

        if (message == NULL) {
            strncpy(error, "unknown", error_length - 1);
        } else {
            scoped_string _message = get_string(message);

            strncpy(error, _message, error_length - 1);
        }

        return -1;
    }

    return fd;
}

static void release_jni_object_impl(void *obj) {
    TRACE_METHOD();

    ATTACH_JNI();

    del_global((jobject) obj);
}

JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *vm, void *reserved) {
    TRACE_METHOD();

    JNIEnv *env = NULL;

    if ((*vm)->GetEnv(vm, (void **) &env, JNI_VERSION_1_6) != JNI_OK)
        return JNI_ERR;

    initialize_jni(vm, env);

    jclass c_tun_interface = find_class("com/github/kr328/clash/core/bridge/TunInterface");
    jclass c_completable = find_class("kotlinx/coroutines/CompletableDeferred");
    jclass c_fetch_callback = find_class("com/github/kr328/clash/core/bridge/FetchCallback");
    jclass c_logcat_interface = find_class("com/github/kr328/clash/core/bridge/LogcatInterface");
    jclass _c_clash_exception = find_class("com/github/kr328/clash/core/bridge/ClashException");
    jclass _c_content = find_class("com/github/kr328/clash/core/bridge/Content");
    jclass c_throwable = find_class("java/lang/Throwable");
    jclass c_unit = find_class("kotlin/Unit");

    m_tun_interface_mark_socket = find_method(c_tun_interface, "markSocket",
                                              "(I)V");
    m_tun_interface_query_socket_uid = find_method(c_tun_interface, "querySocketUid",
                                                   "(ILjava/lang/String;Ljava/lang/String;)I");
    m_completable_complete = find_method(c_completable, "complete",
                                         "(Ljava/lang/Object;)Z");
    m_fetch_callback_report = find_method(c_fetch_callback, "report",
                                          "(Ljava/lang/String;)V");
    m_fetch_callback_complete = find_method(c_fetch_callback, "complete",
                                            "(Ljava/lang/String;)V");
    m_completable_complete_exceptionally = find_method(c_completable, "completeExceptionally",
                                                       "(Ljava/lang/Throwable;)Z");
    m_logcat_interface_received = find_method(c_logcat_interface, "received",
                                              "(Ljava/lang/String;)V");
    m_clash_exception = find_method(_c_clash_exception, "<init>",
                                    "(Ljava/lang/String;)V");
    m_get_message = find_method(c_throwable, "getMessage",
                                "()Ljava/lang/String;");
    m_open = (*env)->GetStaticMethodID(env, _c_content, "open",
                                       "(Ljava/lang/String;)I");

    o_unit = (*env)->GetStaticObjectField(env, c_unit,
                                          (*env)->GetStaticFieldID(env, c_unit, "INSTANCE",
                                                                   "Lkotlin/Unit;"));

    c_clash_exception = (jclass) new_global(_c_clash_exception);
    c_content = (jclass) new_global(_c_content);
    o_unit = new_global(o_unit);

    mark_socket_func = &call_tun_interface_mark_socket_impl;
    query_socket_uid_func = &call_tun_interface_query_socket_uid_impl;
    complete_func = &call_completable_complete_impl;
    fetch_report_func = &call_fetch_callback_report_impl;
    fetch_complete_func = &call_fetch_callback_complete_impl;
    logcat_received_func = &call_logcat_interface_received_impl;
    open_content_func = &open_content_impl;
    release_object_func = &release_jni_object_impl;
    //### IGtsPacketFlow
    //java defines
    jclass c_gts_packet_flow = find_class("com/github/kr328/clash/core/bridge/IGtsPacketFlow");
    java_i_gts_packet_flow_m_output_packet = find_method(c_gts_packet_flow, "outputPacket", "([B)V");
    java_i_gts_packet_flow_m_update_fd = find_method(c_gts_packet_flow, "updateFD", "(I)V");
    java_i_gts_packet_flow_m_can_reconnect = find_method(c_gts_packet_flow, "canReconnect", "()Z");
    //c defines
    i_gts_packet_flow_output_packet_pt = &invoke_java_i_gts_packet_flow_output_packet_impl;
    i_gts_packet_flow_update_fd_pt = &invoke_java_i_gts_packet_flow_update_fd_impl;
    i_gts_packet_flow_can_reconnect_pt = &invoke_java_i_gts_packet_flow_can_reconnect_impl;

    //### StringAction
    jclass class_string_action = find_class("com/github/kr328/clash/core/bridge/StringAction");
    java_i_string_action_m_void_call = find_method(class_string_action, "call","(Ljava/lang/String;)V");
    //### StringFunc
    jclass class_string_func = find_class("com/github/kr328/clash/core/bridge/StringFunc");
    java_i_string_func_m_bool_call = find_method(class_string_func, "call","(Ljava/lang/String;)Z");
    //c method pointer
    i_string_action_void_call_pt = &invoke_java_i_string_action_void_call_impl;
    i_string_action_bool_call_pt = &invoke_java_i_string_func_bool_call_impl;
    //### HostApiTestConsumer
    jclass class_host_test_consumer = find_class("com/github/kr328/clash/core/bridge/HostApiTestConsumer");
    java_i_host_api_test_consumer_m_call = find_method(class_host_test_consumer, "call",
                        "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;)Z");
    i_host_api_test_consumer_call_pt = &invoke_java_i_host_api_test_consumer_call_impl;

    return JNI_VERSION_1_6;
}