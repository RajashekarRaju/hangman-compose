# :video_game: Hangman - Built With Compose Multiplatform :paintbrush:

[![Deploy Web (Production)](https://github.com/RajashekarRaju/hangman-compose/actions/workflows/pages-production.yml/badge.svg)](https://github.com/RajashekarRaju/hangman-compose/actions/workflows/pages-production.yml)
[![Build Packages](https://github.com/RajashekarRaju/hangman-compose/actions/workflows/packages.yml/badge.svg)](https://github.com/RajashekarRaju/hangman-compose/actions/workflows/packages.yml)
[![Tests](https://github.com/RajashekarRaju/hangman-compose/actions/workflows/tests.yaml/badge.svg)](https://github.com/RajashekarRaju/hangman-compose/actions/workflows/tests.yaml)

<img src="/assets/banner_hangman_v_three.png" alt="Game Banner" style="border-radius: 20px;" />

## Play / Download

| Platform      | Access |
|---------------| --- |
| 🌐 Web        | [Play in browser](https://rajashekarraju.github.io/hangman-compose/) |
| 🍎 macOS      | [Download DMG](https://github.com/RajashekarRaju/hangman-compose/releases/download/desktop-latest/hangman-macos.dmg) |
| 🪟 Windows    | [Download MSI](https://github.com/RajashekarRaju/hangman-compose/releases/download/desktop-latest/hangman-windows.msi) |
| 🐧 Linux      | [Download DEB](https://github.com/RajashekarRaju/hangman-compose/releases/download/desktop-latest/hangman-linux.deb) · [Download RPM](https://github.com/RajashekarRaju/hangman-compose/releases/download/desktop-latest/hangman-linux.rpm) |
| 🤖️ Android | [Download APK](https://github.com/RajashekarRaju/hangman-compose/releases/download/android-latest/hangman-android.apk) |
| 📱 iOS        | Run locally on macOS + Xcode: `bash scripts/run-ios-sim.sh` |

### Thanks to [Cicero Hellmann](https://github.com/cicerohellmann) For giving this cool idea to build Hangman game with Compose for Android.

## :sparkler: What's New: v0.3.0

- [x] Traditional Hangman visual mode added alongside simple progress mode (user-selectable).
- [x] New in-app settings: difficulty, category, app language, theme palette, theme mode override,
  game visual mode.
- [x] Cursor customization in Settings with multiple cursor styles and previews.
- [x] Full achievements feature rollout with grouped UI, summary, details, unread state and
  notifications.
- [x] Typography/readability refresh with Bubblegum Sans + updated text scales.
- [x] New Game Guide experience with progression visuals and quick instructions.
- [x] Settings extracted into a dedicated feature module (`feature/settings`).
- [x] Existing game configuration moved from menu flows into Settings.
- [x] Audio controls moved to Settings: background music on/off and sound effects on/off.
- [x] Localization improvements with multi-language support and runtime language switching.
- [x] Sentry integration expanded for release builds (events/logs + exception capture across
  supported platforms).
- [x] Many bug fixes related to gameplay.
- [x] Reworked many ui elements to fit properly for platform device types.

### Screenshots (v0.3.0)

#### Foldable

| Main Menu | History | Achievements |
| --- | --- | --- |
| ![Fold Main Menu](/assets/screenshots/fold/main_menu.png) | ![Fold History](/assets/screenshots/fold/history.png) | ![Fold Achievements](/assets/screenshots/fold/achievements.png) |

| Settings | Game | Game Loss |
| --- | --- | --- |
| ![Fold Settings](/assets/screenshots/fold/settings.png) | ![Fold Game](/assets/screenshots/fold/gameplay.png) | ![Fold Game Loss](/assets/screenshots/fold/gameplay_loss.png) |

| Game Over Dialog |
| --- |
| ![Fold Game Over Dialog](/assets/screenshots/fold/game_over_dialog.png) |

#### Pixel Mobile

| Main Menu | History | Achievements |
| --- | --- | --- |
| ![Pixel Mobile Main Menu](/assets/screenshots/pixel-mobile/main_menu.png) | ![Pixel Mobile History](/assets/screenshots/pixel-mobile/history.png) | ![Pixel Mobile Achievements](/assets/screenshots/pixel-mobile/achievements.png) |

| Settings Difficulty | Settings Category | Settings Language |
| --- | --- | --- |
| ![Pixel Mobile Settings Difficulty](/assets/screenshots/pixel-mobile/settings_difficulty.png) | ![Pixel Mobile Settings Category](/assets/screenshots/pixel-mobile/settings_category.png) | ![Pixel Mobile Settings Language](/assets/screenshots/pixel-mobile/settings_language.png) |

| Settings Theme Palette | Settings Game Visual | Settings Audio |
| --- | --- | --- |
| ![Pixel Mobile Settings Theme Palette](/assets/screenshots/pixel-mobile/settings_theme_palette.png) | ![Pixel Mobile Settings Game Visual](/assets/screenshots/pixel-mobile/settings_game_visual.png) | ![Pixel Mobile Settings Audio](/assets/screenshots/pixel-mobile/settings_audio.png) |

| Game | Game Loss | Game Over Dialog |
| --- | --- | --- |
| ![Pixel Mobile Game](/assets/screenshots/pixel-mobile/gameplay.png) | ![Pixel Mobile Game Loss](/assets/screenshots/pixel-mobile/gameplay_loss.png) | ![Pixel Mobile Game Over Dialog](/assets/screenshots/pixel-mobile/game_over_dialog.png) |

#### Desktop

| Main Menu | History | Achievements |
| --- | --- | --- |
| ![Desktop Main Menu](/assets/screenshots/desktop/main_menu.png) | ![Desktop History](/assets/screenshots/desktop/history.png) | ![Desktop Achievements](/assets/screenshots/desktop/achievements.png) |

| Settings | Game | Game Loss |
| --- | --- | --- |
| ![Desktop Settings](/assets/screenshots/desktop/settings.png) | ![Desktop Game](/assets/screenshots/desktop/gameplay.png) | ![Desktop Game Loss](/assets/screenshots/desktop/gameplay_loss.png) |

| Game Over Dialog |
| --- |
| ![Desktop Game Over Dialog](/assets/screenshots/desktop/game_over_dialog.png) |

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
  - `./gradlew :app:testDebugUnitTest :game-core:desktopTest :feature:mainmenu:desktopTest :feature:game:desktopTest :feature:history:desktopTest`
- Broader KMP module tests:
  - `./gradlew :composeApp:allTests :core:data:allTests :core:designsystem:allTests :feature:common-ui:allTests :feature:game:allTests :feature:history:allTests :feature:mainmenu:allTests :game-core:allTests :navigation:allTests`

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
- Fonts: [Creepster](https://fonts.google.com/specimen/Creepster#about), [Bubblegum Sans](https://fonts.google.com/specimen/Bubblegum+Sans) (SIL Open Font License 1.1)
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
