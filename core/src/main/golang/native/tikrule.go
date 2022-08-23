package main

//#include "bridge.h"
import "C"
import (
	"github.com/Dreamacro/clash/tikpatch/tikrule"
)

//export patchAddBypassIp
func patchAddBypassIp(ip C.c_string) {
	tikrule.AppendNewDirectIp(C.GoString(ip))
}

