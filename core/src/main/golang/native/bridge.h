#pragma once

#include <stddef.h>
#include <stdint.h>
#include <malloc.h>
#include <android/log.h>

#define TAG "ClashForAndroid"

typedef const char *c_string;

extern void (*mark_socket_func)(void *tun_interface, int fd);

extern int (*query_socket_uid_func)(void *tun_interface, int protocol, const char *source, const char *target);

extern void (*complete_func)(void *completable, const char *exception);

extern void (*fetch_report_func)(void *fetch_callback, const char *status_json);

extern void (*fetch_complete_func)(void *fetch_callback, const char *error);

extern int (*logcat_received_func)(void *logcat_interface, const char *payload);

extern void (*release_object_func)(void *obj);

extern int (*open_content_func)(const char *url, char *error, int error_length);

// cgo
extern void mark_socket(void *interface, int fd);

extern int query_socket_uid(void *interface, int protocol, char *source, char *target);

extern void complete(void *obj, char *error);

extern void fetch_complete(void *completable, char *exception);

extern void fetch_report(void *fetch_callback, char *status_json);


extern int logcat_received(void *logcat_interface, char *payload);

extern void release_object(void *obj);

extern int open_content(char *url, char *error, int error_length);

extern void log_info(char *msg);

extern void log_error(char *msg);

extern void log_warn(char *msg);

extern void log_debug(char *msg);

extern void log_verbose(char *msg);


extern void (*i_gts_packet_flow_output_packet_pt)(void *callback, const char *packet);
extern void invoke_gts_packet_flow_output_packet(void *callback, char *packet);

extern void (*i_gts_packet_flow_update_fd_pt)(void *callback, int fd);
extern void invoke_gts_packet_flow_update_fd(void *callback, int fd);

extern int (*i_gts_packet_flow_can_reconnect_pt)(void *callback);
extern int invoke_gts_packet_flow_can_reconnect(void *callback);

extern void (*i_string_action_void_call_pt)(void *callback, const char *payload);
extern void invoke_i_string_void(void *callback, char *payload);

extern int (*i_string_action_bool_call_pt)(void *callback, const char *payload);
extern int invoke_i_string_bool(void *callback, char *payload);

extern int (*i_string2_action_bool_call_pt)(void *callback, const char *p1, const char *p2);
extern int invoke_i_string2_bool(void *callback, char *p1, char *p2);

extern int (*i_host_api_test_consumer_call_pt)(void *callback, const char *host, int time,
                                               const char *error, const char *result);
extern int invoke_i_host_api_test_consumer_call(void *callback, char *host, int time,
                                                char *error, char *result);
