#!/bin/bash
export PROJECT=BusExplorer
export ICONDIR=$PROJECT.iconset

if [ -z $1 ]; then 
  echo -e "Usage: $0 <image.png> [image16x16.png]
\t image.png      - must be 1024x1024 pixels at least
\t image16x16.png - to use a different artwork for this very small icon\n"
  exit 1
fi
if [ -n $2 ]; then ALTERNATIVE_16_ORIGICON=$2; fi;

export ORIGICON=$1

mkdir -p $ICONDIR

# Normal screen icons
if [ -n "$ALTERNATIVE_16_ORIGICON" ]; then
  cp -f $ALTERNATIVE_16_ORIGICON $ICONDIR/icon_16x16.png
else
  SIZE=16
  sips -z $SIZE $SIZE $ORIGICON --out $ICONDIR/icon_${SIZE}x${SIZE}.png ;
fi

for SIZE in 32 64 128 256 512; do
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
