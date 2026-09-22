#!/usr/bin/env sh
# Builds the APK for a given version: ./scripts/build-apk.sh 1.2.3 -> workout-plan-1.2.3.apk
# versionCode is derived from the version so that every release installs over the previous one.
set -eu
version="$1"
major=${version%%.*}; rest=${version#*.}
minor=${rest%%.*}; patch=${rest#*.}
code=$((major * 10000 + minor * 100 + patch))

./gradlew assembleDebug -PappVersion="$version" -PappVersionCode="$code" --no-daemon
cp app/build/outputs/apk/debug/app-debug.apk "workout-plan-$version.apk"
echo "built workout-plan-$version.apk (versionCode $code)"
