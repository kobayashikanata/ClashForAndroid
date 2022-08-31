package main

//#include "bridge.h"
import "C"
import (
	"github.com/Dreamacro/clash/log"
	"github.com/Dreamacro/clash/tikpatch/event"
	"unsafe"
)

//export subscribeEvent
func subscribeEvent(remote unsafe.Pointer) {
	log.Debugln("Event subscriber subscribed")
	go func(remote unsafe.Pointer) {
		sub := event.Subscribe()
		defer event.UnSubscribe(sub)

		for i := range sub {
			msg := i.(event.Event)

			log.Debugln("OnEvent %s", msg.Name)
			if C.invoke_i_string_bool(remote, marshalString(msg.Name)) != 0 {
				C.release_object(remote)
				log.Debugln("Event subscriber closed")
				break
			}
		}
	}(remote)
}
