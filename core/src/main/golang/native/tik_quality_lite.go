//go:build tsglite

package main

//#include "bridge.h"
import "C"
import (
	"unsafe"
)

//export tcpTestCancel
func tcpTestCancel(tag C.int) {

}

//export tcpTest
func tcpTest(host C.c_string, timeout C.int, maxCount C.int, tag C.int, send64Bytes C.int, callback unsafe.Pointer) {
	C.release_object(callback)
}

//export stopDiscoverHost
func stopDiscoverHost(tag C.int) {

}

//export discoverHost
func discoverHost(hosts C.c_string, timeout C.int, maxCount C.int, tag C.int, callback unsafe.Pointer) {
	C.release_object(callback)
}

//export tcpPing
func tcpPing(host C.c_string, pingCount C.int, timeout C.int, interval C.int, groupCount C.int, checkAlive C.int) *C.char {
	return marshalString("")
}

//export udpPing
func udpPing(addr C.c_string, count C.int, timeout C.int, packetLength C.int) *C.char {
	return marshalJson("{}")
}
