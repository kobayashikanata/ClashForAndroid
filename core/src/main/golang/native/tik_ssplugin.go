//go:build !tsglite

package main

//#include "bridge.h"
import "C"
import (
	"github.com/Dreamacro/clash/tsgpatch/ssplugin"
	"unsafe"
)

var currentGtsCallback unsafe.Pointer

//export patchStartTrojanWithJson
func patchStartTrojanWithJson(configJson C.c_string) {
	ssplugin.StartTrojanWithString(C.GoString(configJson))
}

//export patchStopTrojan
func patchStopTrojan() {
	ssplugin.TrojanStop()
}

//export patchStartSSWithJson
func patchStartSSWithJson(configJson C.c_string) *C.char {
	if err := ssplugin.StartSSWith(C.GoString(configJson)); err != nil {
		return marshalString(err)
	}
	return nil
}

//export patchStopSS
func patchStopSS(tag C.int) {
	ssplugin.StopSS(int(tag))
}

//export patchStartGtsWithFd
func patchStartGtsWithFd(configJson C.c_string, fd C.int, toolsJson C.c_string, packetFlow unsafe.Pointer) *C.char {
	if currentGtsCallback != nil {
		C.release_object(currentGtsCallback)
	}
	currentGtsCallback = packetFlow
	configBytes := []byte(C.GoString(configJson))
	callback := GtsFlowImpl{Pointer: packetFlow}
	if err := ssplugin.StartGtsWithFD(configBytes, int(fd), C.GoString(toolsJson), callback); err != nil {
		return marshalString(err)
	}
	return nil
}

//export patchStopGts
func patchStopGts() {
	ssplugin.CloseGts()
	if currentGtsCallback != nil {
		C.release_object(currentGtsCallback)
	}
}

//export patchSetGCPercent
func patchSetGCPercent(percent C.int) {
	ssplugin.SetGCPercent(int(percent))
}

//export patchGC
func patchGC() {
	ssplugin.GC()
}

//export patchFinishLog
// finishLog 停止记录日志，关闭对应文件
func patchFinishLog() *C.char {
	if err := ssplugin.FinishLog(); err != nil {
		return marshalString(err)
	}
	return nil
}

//export patchGetAllocMem
// getAllocMem 获取内存已申请且仍在使用的字节数
func patchGetAllocMem() C.int {
	return C.int(ssplugin.GetMem())
}

type GtsFlowImpl struct {
	Pointer unsafe.Pointer
}

func (r GtsFlowImpl) OutputPacket(packet []byte) {
	C.invoke_gts_packet_flow_output_packet(r.Pointer, (*C.char)(unsafe.Pointer(&packet[0])))
}

func (r GtsFlowImpl) UpdateFD(fd int) {
	C.invoke_gts_packet_flow_update_fd(r.Pointer, C.int(fd))
}

func (r GtsFlowImpl) CanReconn() bool {
	return C.invoke_gts_packet_flow_can_reconnect(r.Pointer) != 0
}
