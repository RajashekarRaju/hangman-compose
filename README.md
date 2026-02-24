# :video_game: Hangman - Built With Compose :paintbrush:

## Multiplatform Hangman built with Compose Multiplatform.

[![Deploy Web (Production)](https://github.com/RajashekarRaju/hangman-compose/actions/workflows/pages-production.yml/badge.svg)](https://github.com/RajashekarRaju/hangman-compose/actions/workflows/pages-production.yml)
[![Build Packages](https://github.com/RajashekarRaju/hangman-compose/actions/workflows/packages.yml/badge.svg)](https://github.com/RajashekarRaju/hangman-compose/actions/workflows/packages.yml)
[![Tests](https://github.com/RajashekarRaju/hangman-compose/actions/workflows/tests.yaml/badge.svg)](https://github.com/RajashekarRaju/hangman-compose/actions/workflows/tests.yaml)

![Game Banner](/assets/banner_hangman.png)

## Play / Download

- Web: [https://rajashekarraju.github.io/hangman-compose/](https://rajashekarraju.github.io/hangman-compose/)
- Desktop installers :
  - macOS DMG: [Download](https://github.com/RajashekarRaju/hangman-compose/releases/download/desktop-latest/hangman-macos.dmg)
  - Windows MSI: [Download](https://github.com/RajashekarRaju/hangman-compose/releases/download/desktop-latest/hangman-windows.msi)
  - Linux DEB: [Download](https://github.com/RajashekarRaju/hangman-compose/releases/download/desktop-latest/hangman-linux.deb)
  - Linux RPM: [Download](https://github.com/RajashekarRaju/hangman-compose/releases/download/desktop-latest/hangman-linux.rpm)
- Android APK : [Download](https://github.com/RajashekarRaju/hangman-compose/releases/download/android-latest/hangman-android.apk)

### Thanks to [Cicero Hellmann](https://github.com/cicerohellmann) For giving this cool idea to build Hangman game with Compose for Android.

## :sparkler: What's New: v0.2.0

- [x] Kotlin Multiplatform architecture (`app`, `composeApp`, `game-core`, `core/*`, `feature/*`, `navigation`)
- [x] Platform support: Android
- [x] Platform support: Web (WASM + GitHub Pages)
- [x] Platform support: Desktop (JVM: macOS/Windows/Linux)
- [x] Platform support: iOS (Xcode project + simulator flow)
- [x] Gameplay: 60-second timer per level
- [x] Gameplay: hints (`Reveal`, `Eliminate`) with cooldown
- [x] Gameplay: persistent game settings + history
- [x] Gameplay: scoring improvements
- [x] Difficulty: Introduced `VERY_HARD` difficulty for words >= 11+ chars
- [x] Categories: Introduced new categories and extended vocabulary.
- [x] Responsive game layout (mobile/desktop/web) with previews and shared creepy design system.
- [x] Cross-platform sound support (Android/Web/Desktop/iOS)
- [x] Navigation3 update

## Initial Version (v0.1.0)

- [x] Players will be challenged with unique words only.
- [x] Players can view their score/game history.
- [x] Players should know how many unique words can still guess.
- [x] Players can see how many attempts they have to guess the word.
- [x] Players can try out letters in any order they want.
- [x] Players can to know how many points made when I completed a word.
- [x] Players can to know how many points scored by completing all levels.
- [x] Persist the game state at any point of the game.

## Local Development

### Common commands

- Run Android app:
  - `./gradlew :app:assembleDebug`
- Run Desktop app:
  - `./gradlew :composeApp:desktopRun`
- Run Web locally:
  - `./gradlew :composeApp:wasmJsBrowserDevelopmentRun`
- Build Web distribution:
  - `./gradlew :composeApp:wasmJsBrowserDistribution`
- Run iOS simulator (from repo root, macOS + Xcode required):
  - `bash scripts/run-ios-sim.sh`

### Tests

- CI-equivalent local test run:
  - `./gradlew :app:testDebugUnitTest :game-core:desktopTest :feature:onboarding:desktopTest :feature:game:desktopTest :feature:history:desktopTest`
- Broader KMP module tests:
  - `./gradlew :composeApp:allTests :core:data:allTests :core:designsystem:allTests :feature:common-ui:allTests :feature:game:allTests :feature:history:allTests :feature:onboarding:allTests :game-core:allTests :navigation:allTests`

## CI / Releases

- `.github/workflows/tests.yaml`
  - Runs on pull requests
  - Runs JVM unit + desktop tests
  - Runs Android instrumentation on push/workflow_dispatch
- `.github/workflows/pages-production.yml`
  - Deploys web app to GitHub Pages
- `.github/workflows/packages.yml`
  - On `master` push:
    - builds Android APK
    - builds desktop installers (DMG/MSI/DEB/RPM)
    - updates release artifacts

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

- Open an issue for bugs.
- Open a discussion for feature ideas.
- Send a PR with focused changes and passing checks.

## Credits

- Game sounds: [Pixabay](https://pixabay.com/)
- Font: [Creepster](https://fonts.google.com/specimen/Creepster#about)
- Icons/background assets: modified assets from [unDraw](https://undraw.co/) and [iconscout](https://iconscout.com/)
- Sound license documents: [pixabay-license](https://github.com/RajashekarRaju/hangman-compose/tree/master/pixabay-license)

## License

Apache License 2.0. See [LICENSE](LICENSE).

```
Copyright 2022 Rajasekhar K E

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
