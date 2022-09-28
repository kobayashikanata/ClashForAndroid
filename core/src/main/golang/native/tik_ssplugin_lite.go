//go:build tsglite

package main

//#include "bridge.h"
import "C"
import (
	"runtime"
	"runtime/debug"
	"unsafe"
)

//export patchStartTrojanWithJson
func patchStartTrojanWithJson(configJson C.c_string) {

}

//export patchStopTrojan
func patchStopTrojan() {

}

//export patchStartSSWithJson
func patchStartSSWithJson(configJson C.c_string) *C.char {
	return marshalString("unimplemented")
}

//export patchStopSS
func patchStopSS(tag C.int) {

}

//export patchStartGtsWithFd
func patchStartGtsWithFd(configJson C.c_string, fd C.int, toolsJson C.c_string, packetFlow unsafe.Pointer) *C.char {
	C.release_object(packetFlow)
	return marshalString("unimplemented")
}

//export patchStopGts
func patchStopGts() {

}

//export patchSetGCPercent
func patchSetGCPercent(percent C.int) {
	debug.SetGCPercent(int(percent))
}

//export patchGC
func patchGC() {
	debug.FreeOSMemory()
}

//export patchFinishLog
// finishLog 停止记录日志，关闭对应文件
func patchFinishLog() *C.char {
	return marshalString("unimplemented")
}

//export patchGetAllocMem
// getAllocMem 获取内存已申请且仍在使用的字节数
func patchGetAllocMem() C.int {
	var memstats runtime.MemStats
	runtime.ReadMemStats(&memstats)
	return C.int(int(memstats.Alloc))
}
