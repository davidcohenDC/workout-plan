#!/usr/bin/env sh
# Smoke run on a connected device or emulator: installs the APK, opens the home
# screen, taps the "+" button to reach the creation wizard, and fails if the app
# dies or logs a fatal exception along the way.
set -eu
apk="${1:-app/build/outputs/apk/debug/app-debug.apk}"
pkg=com.example.workoutplan

check() {
  sleep "$2"
  if ! adb shell pidof "$pkg" >/dev/null; then
    echo "FAIL: $1 is not running"
    adb logcat -d '*:E' | tail -n 100
    exit 1
  fi
  if adb logcat -d '*:E' | grep -q "FATAL EXCEPTION"; then
    echo "FAIL: fatal exception while running $1"
    adb logcat -d '*:E' | grep -A 30 "FATAL EXCEPTION"
    exit 1
  fi
  echo "OK: $1"
}

# Prints "x y" of the centre of the first view whose resource id contains $1.
find_view() {
  adb shell uiautomator dump /sdcard/ui.xml >/dev/null
  adb exec-out cat /sdcard/ui.xml \
    | tr '>' '\n' | grep "resource-id=\"[^\"]*$1" \
    | sed -n 's/.*bounds="\[\([0-9]*\),\([0-9]*\)\]\[\([0-9]*\),\([0-9]*\)\]".*/\1 \2 \3 \4/p' \
    | head -n 1 | awk '{ print int(($1+$3)/2), int(($2+$4)/2) }'
}

adb install -r "$apk"
adb logcat -c
adb shell am start -W -n "$pkg/.MainActivity" >/dev/null
check "home screen" 8

fab=$(find_view add_workout)
[ -n "$fab" ] || { echo "FAIL: add-workout button not found on the home screen"; exit 1; }
adb shell input tap $fab
check "creation wizard" 5

adb shell uiautomator dump /sdcard/ui.xml >/dev/null
adb exec-out cat /sdcard/ui.xml | grep -q workout_input \
  || { echo "FAIL: the wizard did not open"; exit 1; }
echo "OK: wizard opened"
