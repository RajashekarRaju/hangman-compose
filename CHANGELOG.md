# Changelog

## [v0.3.0]

### Added

- Full achievements feature rollout:
    - achievements module and grouped UI
    - achievement storage/repository models for all platforms
    - unread achievement state
    - achievement details dialog
    - achievement summary card
    - achievement notifications with coordinator
- Multiplatform logging module.
- Multi-language support and language switching.
- Game progress visual preference:
    - `Traditional Hangman` visual
    - `Simple` level/points/attempts visual
- New Settings capabilities:
    - difficulty/category/language/theme
    - background music and sound effects toggles
    - cursor style selection

### Changed

- Main UX refactor:
    - `OnBoarding` renamed to `MainMenu`
    - Settings extracted into its own feature module
    - Existing game configuration moved from menu flows into Settings
- Game instructions updated:
    - dialog flow replaced with progression visuals and quick instructions
- Achievement title/description ownership moved from `game-core` to string resources in
  `feature/common-ui`.
- Typography/readability improved with updated font configuration.

### Fixed

- Hint reveal correctness bug:
    - alphabet is marked guessed only when no unrevealed occurrences remain in the active word.
- Background/effect audio settings handling aligned with Settings-based configuration.

### Technical

- Logging initialization flow unified with `LoggingInitializationConfig` + `initializeLogging`.
- Sentry integration support added in current branch line.

## [v0.2.0]

### Added

- Kotlin Multiplatform architecture:
    - `app`, `composeApp`, `game-core`, `core/*`, `feature/*`, `navigation`
- Platform support:
    - Android
    - Web (WASM + GitHub Pages)
    - Desktop JVM (macOS/Windows/Linux)
    - iOS simulator flow
- Gameplay:
    - 60-second timer per level
    - hints (`Reveal`, `Eliminate`) with cooldown
    - persistent settings/history and scoring improvements
- Difficulty and content:
    - `VERY_HARD` difficulty for longer words
    - expanded categories and vocabulary
- Responsive game layouts and creepy design system consistency.
- Cross-platform sound support.

### Changed

- Navigation upgraded and project structure prepared for KMP scaling.

