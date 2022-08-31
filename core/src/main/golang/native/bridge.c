#include "bridge.h"
#include "trace.h"

void (*mark_socket_func)(void *tun_interface, int fd);

int (*query_socket_uid_func)(void *tun_interface, int protocol, const char *source, const char *target);

void (*complete_func)(void *completable, const char *exception);

void (*fetch_report_func)(void *fetch_callback, const char *status_json);

void (*fetch_complete_func)(void *fetch_callback, const char *error);

void (*i_string_action_void_call_pt)(void *callback, const char *payload);
int (*i_string_action_bool_call_pt)(void *callback, const char *payload);

void (*i_gts_packet_flow_output_packet_pt)(void *callback, const char *packet);
void (*i_gts_packet_flow_update_fd_pt)(void *callback, int fd);
int (*i_gts_packet_flow_can_reconnect_pt)(void *callback);

int (*i_host_api_test_consumer_call_pt)(void *callback, const char *host, int time,
                                        const char *error, const char *result);


int (*logcat_received_func)(void *logcat_interface, const char *payload);

int (*open_content_func)(const char *url, char *error, int error_length);

void (*release_object_func)(void *obj);

void mark_socket(void *interface, int fd) {
    TRACE_METHOD();

    mark_socket_func(interface, fd);
}

int query_socket_uid(void *interface, int protocol, char *source, char *target) {
    TRACE_METHOD();

    int result = query_socket_uid_func(interface, protocol, source, target);

    free(source);
    free(target);

    return result;
}

void complete(void *obj, char *error) {
    TRACE_METHOD();

    complete_func(obj, error);

    free(error);
}

void fetch_complete(void *fetch_callback, char *exception) {
    TRACE_METHOD();

    fetch_complete_func(fetch_callback, exception);

    free(exception);
}

void fetch_report(void *fetch_callback, char *json_status) {
    TRACE_METHOD();

    fetch_report_func(fetch_callback, json_status);

    free(json_status);
}

int logcat_received(void *logcat_interface, char *payload) {
    TRACE_METHOD();

    int result = logcat_received_func(logcat_interface, payload);

    free(payload);

    return result;
}

int open_content(char *url, char *error, int error_length) {
    TRACE_METHOD();

    int result = open_content_func(url, error, error_length);

    free(url);

    return result;
}

void release_object(void *obj) {
    TRACE_METHOD();

    release_object_func(obj);
}

void log_info(char *msg) {
    __android_log_write(ANDROID_LOG_INFO, TAG, msg);

    free(msg);
}

void log_error(char *msg) {
    __android_log_write(ANDROID_LOG_ERROR, TAG, msg);

    free(msg);
}

void log_warn(char *msg) {
    __android_log_write(ANDROID_LOG_WARN, TAG, msg);

    free(msg);
}

void log_debug(char *msg) {
    __android_log_write(ANDROID_LOG_DEBUG, TAG, msg);

    free(msg);
}

void log_verbose(char *msg) {
    __android_log_write(ANDROID_LOG_VERBOSE, TAG, msg);

    free(msg);
}


void invoke_i_string_void(void *callback, char *payload) {
    TRACE_METHOD();

    i_string_action_void_call_pt(callback, payload);

    free(payload);
}

int invoke_i_string_bool(void *callback, char *payload) {
    TRACE_METHOD();

    int result = i_string_action_bool_call_pt(callback, payload);
    free(payload);

    return result;
}

void invoke_gts_packet_flow_output_packet(void *callback, char *packet) {
    TRACE_METHOD();

    i_gts_packet_flow_output_packet_pt(callback, packet);

    free(packet);
}

void invoke_gts_packet_flow_update_fd(void *callback, int fd) {
    TRACE_METHOD();

    i_gts_packet_flow_update_fd_pt(callback, fd);
}

int invoke_gts_packet_flow_can_reconnect(void *callback) {
    TRACE_METHOD();

    return i_gts_packet_flow_can_reconnect_pt(callback);
}


int invoke_i_host_api_test_consumer_call(void *callback, char *host, int time,
                                         char *error, char *result) {
    TRACE_METHOD();

    int consumed = i_host_api_test_consumer_call_pt(callback, host, time, error, result);
    free(host);
    free(error);
    free(result);
    return consumed;
}

