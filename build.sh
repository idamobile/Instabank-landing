#!/bin/bash
unset SSH_ASKPASS
set -x
PLAY_VERSION=1.2.5
PLAY_COMPILER=/usr/local/play-$PLAY_VERSION/play
TMP_FOLDER=play_tmp

git pull

$PLAY_COMPILER war ./ -o ../$TMP_FOLDER
$PLAY_COMPILER clean

cd ../$TMP_FOLDER
zip -r root.war WEB-INF
mv root.war ../
cd ../
rm -rf $TMP_FOLDER
