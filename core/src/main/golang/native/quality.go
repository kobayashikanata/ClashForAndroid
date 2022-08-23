package main

//#include "bridge.h"
import "C"
import (
	"time"
	"unsafe"

	packetping "github.com/Dreamacro/clash/tikpatch/benchmark"
)

//export tcpTestCancel
func tcpTestCancel(tag C.int) {
	packetping.TCPTestCancel(int(tag))
}

//export tcpTest
func tcpTest(host C.c_string, timeout C.int, maxCount C.int, tag C.int, send64Bytes C.int, callback unsafe.Pointer) {
	packetping.TCPTest(C.GoString(host), int(timeout), int(maxCount), int(tag), send64Bytes != 0, func(msg string) {
		C.callback_string(callback, marshalString(msg))
	})
}

//export tcpPing
func tcpPing(host C.c_string, pingCount C.int, timeout C.int, interval C.int, groupCount C.int, checkAlive C.int) *C.char {
	result := packetping.TCPPing(C.GoString(host), int(pingCount), int(timeout), int(interval), int(groupCount), checkAlive != 0)
	return marshalString(result)
}

//export udpPing
func udpPing(addr C.c_string, count C.int, timeout C.int, packetLength C.int) *C.char {
	re, err := packetping.UdpPing(C.GoString(addr), int(count), time.Duration(int(timeout))*time.Second, int(packetLength))
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
