//go:build !tsglite

package main

//#include "bridge.h"
import "C"
import (
	"strings"
	"time"
	"unsafe"

	benchmark "github.com/Dreamacro/clash/tsgpatch/benchmark"
)

//export tcpTestCancel
func tcpTestCancel(tag C.int) {
	benchmark.TCPTestCancel(int(tag))
}

//export tcpTest
func tcpTest(host C.c_string, timeout C.int, maxCount C.int, tag C.int, send64Bytes C.int, callback unsafe.Pointer) {
	benchmark.TCPTest(C.GoString(host), int(timeout), int(maxCount), int(tag), send64Bytes != 0, func(msg string) {
		C.invoke_i_string_void(callback, marshalString(msg))
	})
	C.release_object(callback)
}

//export stopDiscoverHost
func stopDiscoverHost(tag C.int) {
	benchmark.StopDiscover(int(tag))
}

//export discoverHost
func discoverHost(hosts C.c_string, timeout C.int, maxCount C.int, tag C.int, callback unsafe.Pointer) {
	hostsArray := strings.Split(C.GoString(hosts), ",")
	benchmark.DiscoverHost(hostsArray, int(timeout), int(maxCount), int(tag), func(r benchmark.RacingResult) bool {
		return C.invoke_i_host_api_test_consumer_call(callback,
			marshalString(r.Host), C.int(r.Time),
			marshalString(r.Error), marshalString(r.Result)) != 0
	})
	C.release_object(callback)
}

//export tcpPing
func tcpPing(host C.c_string, pingCount C.int, timeout C.int, interval C.int, groupCount C.int, checkAlive C.int) *C.char {
	result := benchmark.TCPPing(C.GoString(host), int(pingCount), int(timeout), int(interval), int(groupCount), checkAlive != 0)
	return marshalString(result)
}

//export udpPing
func udpPing(addr C.c_string, count C.int, timeout C.int, packetLength C.int) *C.char {
	re, err := benchmark.UdpPing(C.GoString(addr), int(count), time.Duration(int(timeout))*time.Second, int(packetLength))
	response := &struct {
		SendCount int    `json:"sendCount"`
		RecvCount int    `json:"recvCount"`
		MinRTT    int    `json:"minRTT"`
		MaxRTT    int    `json:"maxRTT"`
		AvgRTT    int    `json:"avgRTT"`
		Err       string `json:"err"`
	}{
		SendCount: re.SendCount,
		RecvCount: re.RecvCount,
		MinRTT:    re.MinRTT,
		MaxRTT:    re.MaxRTT,
		AvgRTT:    re.AvgRTT,
		Err:       re.Err,
	}
	if err != nil && len(re.Err) <= 0 {
		re.Err = err.Error()
	}
	return marshalJson(response)
}
