# Copyright 2017-present Facebook. All Rights Reserved.

SUMMARY = "Platform Service for OpenBMC"
DESCRIPTION = "This deamon takes input from Json file and builds platform tree. It pushes data to respective dbus services (for eg SensorService)"
SECTION = "base"
PR = "r1"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://PlatformSvcd.cpp;beginline=4;endline=16;md5=5f8ba3cd0f216026550dbcc0186d5599"

SRC_URI =+ "file://Makefile \
           file://org.openbmc.PlatformService.conf \
           file://Sensor.h \
           file://PlatformObjectTree.cpp \
           file://PlatformObjectTree.h \
           file://PlatformJsonParser.cpp \
           file://PlatformService.h \
           file://PlatformJsonParser.h \
           file://PlatformSvcd.cpp \
           file://FRU.h \
           file://SensorService.h \
           file://SensorService.cpp \
           file://DBusPlatformSvcInterface.cpp \
           file://DBusPlatformSvcInterface.h \
          "

S = "${WORKDIR}"

LDFLAGS =+ " -lpthread -lgobject-2.0 -lobject-tree -lgflags -lglog -lgio-2.0 -lglib-2.0 -ldbus-utils"
DEPENDS =+ "nlohmann-json libipc object-tree dbus-utils glog gflags"
RDEPENDS_${PN} += "dbus"

export SINC = "${STAGING_INCDIR}"
export SLIB = "${STAGING_LIBDIR}"

binfiles = "platform-svcd"
pkgdir = "platform-svc"

do_install() {
  dst="${D}/usr/local/fbpackages/${pkgdir}"
  bin="${D}/usr/local/bin"
  install -d $dst
  install -d $bin
  for f in ${binfiles}; do
    install -m 755 $f ${dst}/$f
    ln -snf ../fbpackages/${pkgdir}/$f ${bin}/$f
  done

  install -d ${D}${sysconfdir}/dbus-1
  install -d ${D}${sysconfdir}/dbus-1/system.d
  install -m 644 org.openbmc.PlatformService.conf ${D}${sysconfdir}/dbus-1/system.d/org.openbmc.PlatformService.conf
}

FBPACKAGEDIR = "${prefix}/local/fbpackages"

FILES_${PN} = "${FBPACKAGEDIR}/platform-svc ${prefix}/local/bin ${sysconfdir}"
