#!/bin/bash

if [ -z "$1" ]; then echo -e "Usage: $0 <version>\n"; exit 1; fi

VERSION=$1
APPVERSION=$(echo $VERSION| sed -e "s/-SNAPSHOT/\.$(date '+%Y%m%d')/")
JARFILE=$(echo $(pwd)/target/busexplorer-2.1-$VERSION-jar-with-dependencies.jar)

if [ ! -f $JARFILE ]; then echo "$JARFILE not found"; exit 1; fi

cd src/main/deploy > /dev/null
jdk=$(/usr/libexec/java_home)
$jdk/bin/javapackager -deploy -native dmg \
   -Bicon=package/macosx/BusExplorer.icns -BappVersion=$APPVERSION \
   -srcfiles $JARFILE -appclass busexplorer.Application -name BusExplorer \
   -outdir ../../../target/macosx-deploy -outfile BusExplorer -v
cd - > /dev/null
