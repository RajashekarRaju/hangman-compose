#!/usr/bin/env bash
set -euo pipefail

DEVICE_NAME="${1:-${DEVICE_NAME:-iPhone 17 Pro}}"
CONFIGURATION="${CONFIGURATION:-Debug}"
DERIVED_DATA_PATH="${DERIVED_DATA_PATH:-/tmp/hangman-ios-derived}"
PROJECT_PATH="${PROJECT_PATH:-iosApp/iosApp.xcodeproj}"
SCHEME="${SCHEME:-iosApp}"
BUNDLE_ID="${BUNDLE_ID:-com.developersbreach.hangman.iosApp}"

if ! command -v xcodebuild >/dev/null 2>&1; then
  echo "xcodebuild is not installed. Install Xcode and command line tools first."
  exit 1
fi

if ! command -v xcrun >/dev/null 2>&1; then
  echo "xcrun is not installed. Install Xcode command line tools first."
  exit 1
fi

if [ ! -d "$PROJECT_PATH" ]; then
  echo "Xcode project not found at: $PROJECT_PATH"
  exit 1
fi

UDID="$({ xcrun simctl list devices available | grep -F "$DEVICE_NAME (" || true; } | head -n 1 | sed -E 's/.*\(([A-F0-9-]+)\) \((Booted|Shutdown)\).*/\1/')"

if [ -z "$UDID" ]; then
  echo "No available simulator found for device name: $DEVICE_NAME"
  echo "Available simulators:"
  xcrun simctl list devices available
  exit 1
fi

echo "Using simulator: $DEVICE_NAME ($UDID)"
open -a Simulator
xcrun simctl boot "$UDID" >/dev/null 2>&1 || true
xcrun simctl bootstatus "$UDID" -b

echo "Building iOS app..."
xcodebuild \
  -project "$PROJECT_PATH" \
  -scheme "$SCHEME" \
  -configuration "$CONFIGURATION" \
  -destination "id=$UDID" \
  -derivedDataPath "$DERIVED_DATA_PATH" \
  build

APP_PATH="$DERIVED_DATA_PATH/Build/Products/${CONFIGURATION}-iphonesimulator/iosApp.app"
if [ ! -d "$APP_PATH" ]; then
  echo "Built app not found at: $APP_PATH"
  exit 1
fi

echo "Installing app..."
xcrun simctl install "$UDID" "$APP_PATH"

echo "Launching app..."
xcrun simctl launch "$UDID" "$BUNDLE_ID"

echo "Done."
