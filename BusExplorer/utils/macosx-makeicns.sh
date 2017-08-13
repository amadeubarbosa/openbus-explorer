#!/bin/bash
export PROJECT=BusExplorer
export ICONDIR=$PROJECT.iconset
if [ -z $1 ]; then echo -e "Usage: $0 <image.png>\n\timage.png must be 1024x1024 pixels at least\n"; exit 1; fi;

export ORIGICON=$1

mkdir -p $ICONDIR

# Normal screen icons
for SIZE in 16 32 64 128 256 512; do
  sips -z $SIZE $SIZE $ORIGICON --out $ICONDIR/icon_${SIZE}x${SIZE}.png ;
done

# Retina display icons
for SIZE in 32 64 256 512 1024; do
  sips -z $SIZE $SIZE $ORIGICON --out $ICONDIR/icon_$(expr $SIZE / 2)x$(expr $SIZE / 2)@2x.png ;
done

# Make a multi-resolution Icon
iconutil -c icns -o $PROJECT.icns $ICONDIR
rm -rf $ICONDIR #it is useless now

echo "File $PROJECT.icns was generated"
