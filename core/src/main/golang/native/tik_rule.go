package main

//#include "bridge.h"
import "C"
import (
	"github.com/Dreamacro/clash/tsgpatch/tsgrule"
)

//export patchAddBypassIp
func patchAddBypassIp(ip C.c_string) {
	tsgrule.AppendNewDirectIp(C.GoString(ip))
}

