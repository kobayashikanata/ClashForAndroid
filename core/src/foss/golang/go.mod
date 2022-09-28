module foss

go 1.18

require cfa v0.0.0

require (
	cfa/blob v0.0.0 // indirect
	github.com/Dreamacro/clash v1.7.1 // indirect
	github.com/Kr328/tun2socket v0.0.0-20220414050025-d07c78d06d34 // indirect
	github.com/dlclark/regexp2 v1.4.0 // indirect
	github.com/gofrs/uuid v4.2.0+incompatible // indirect
	github.com/gorilla/websocket v1.5.0 // indirect
	github.com/insomniacslk/dhcp v0.0.0-20220504074936-1ca156eafb9f // indirect
	github.com/miekg/dns v1.1.49 // indirect
	github.com/niemeyer/pretty v0.0.0-20200227124842-a10e7caefd8e // indirect
	github.com/oschwald/geoip2-golang v1.7.0 // indirect
	github.com/oschwald/maxminddb-golang v1.9.0 // indirect
	github.com/sirupsen/logrus v1.8.1 // indirect
	github.com/u-root/uio v0.0.0-20210528151154-e40b768296a7 // indirect
	go.etcd.io/bbolt v1.3.6 // indirect
	go.uber.org/atomic v1.9.0 // indirect
	golang.org/x/crypto v0.0.0-20220525230936-793ad666bf5e // indirect
	golang.org/x/mod v0.4.2 // indirect
	golang.org/x/net v0.0.0-20220617184016-355a448f1bc9 // indirect
	golang.org/x/sync v0.0.0-20220601150217-0de741cfad7f // indirect
	golang.org/x/sys v0.0.0-20220615213510-4f61da869c0c // indirect
	golang.org/x/text v0.3.7 // indirect
	golang.org/x/tools v0.1.6-0.20210726203631-07bc1bf47fb2 // indirect
	golang.org/x/xerrors v0.0.0-20200804184101-5ec99f83aff1 // indirect
	gopkg.in/check.v1 v1.0.0-20200227125254-8fa46927fb4f // indirect
	gopkg.in/yaml.v2 v2.4.0 // indirect
	gopkg.in/yaml.v3 v3.0.1 // indirect
)

replace cfa => ../../main/golang

replace github.com/Dreamacro/clash => ./clash

replace cfa/blob => ../../../build/intermediates/golang_blob


//tsgpatch
require (
	github.com/Jeffail/tunny v0.1.4 // indirect
	github.com/Yewenyu/kcp-go/v5 v5.6.3 // indirect
	github.com/Yewenyu/ping-go v0.0.3
	github.com/aead/chacha20 v0.0.0-20180709150244-8b13a72661da // indirect
	github.com/fregie/mpx v0.2.3 // indirect
	github.com/geewan-rd/GTS-go/v2 v2.4.6
	github.com/go-ping/ping v0.0.0-20211130115550-779d1e919534 // indirect
	github.com/go-sql-driver/mysql v1.6.0 // indirect
	github.com/google/btree v1.0.1 // indirect
	github.com/google/netstack v0.0.0-20191123085552-55fcc16cd0eb
	github.com/google/uuid v1.3.0 // indirect
	github.com/juju/ratelimit v1.0.1 // indirect
	github.com/klauspost/cpuid v1.3.1 // indirect
	github.com/klauspost/reedsolomon v1.9.9 // indirect
	github.com/mmcloughlin/avo v0.0.0-20200803215136-443f81d77104 // indirect
	github.com/p4gefau1t/trojan-go v0.10.6
	github.com/patrickmn/go-cache v2.1.0+incompatible // indirect
	github.com/pkg/errors v0.9.1 // indirect
	github.com/refraction-networking/utls v0.0.0-20201210053706-2179f286686b // indirect
	github.com/shadowsocks/go-shadowsocks2 v0.1.5
	github.com/templexxx/cpu v0.0.7 // indirect
	github.com/templexxx/xorsimd v0.4.1 // indirect
	github.com/tjfoc/gmsm v1.3.2 // indirect
	github.com/txthinking/runnergroup v0.0.0-20210326110939-37fc67d0da7c // indirect
	github.com/txthinking/socks5 v0.0.0-20210326104807-61b5745ff346 // indirect
	github.com/txthinking/x v0.0.0-20210326105829-476fab902fbe // indirect
	github.com/xtaci/kcp-go/v5 v5.6.1 // indirect
	github.com/xtaci/smux v1.5.15 // indirect
	golang.org/x/term v0.0.0-20210927222741-03fcf44c2211 // indirect
	golang.org/x/time v0.0.0-20210220033141-f8bda1e9f3ba // indirect
)

replace (
	github.com/Yewenyu/kcp-go/v5 => ./kcp-go
	github.com/geewan-rd/GTS-go/v2 => ./GTS-go
	github.com/p4gefau1t/trojan-go => ./trojan-go
	github.com/shadowsocks/go-shadowsocks2 => ./go-shadowsocks2
)
