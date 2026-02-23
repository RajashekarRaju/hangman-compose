# Contributing Guide

Thanks for contributing to Hangman Compose.

## Development Workflow

1. Create a branch from `master`.
2. Make focused changes.
3. Run relevant checks locally.
4. Open a pull request with a clear summary.

## Word Catalog Contribution (DSL)

Words are managed in `game-core` using a validated DSL catalog.

### Files to edit

- `/game/core/CountryWords.kt`
- `/game/core/LanguageWords.kt`
- `/game/core/CompanyWords.kt`

Do not edit gameplay logic just to add words.

### Rules

- Use lowercase words.
- Use letters and spaces only. Hyphens are accepted in source and normalized to spaces.
- Avoid duplicates inside the same category.
- Keep data realistic for gameplay length buckets.

### Centralized difficulty ranges

Difficulty letter-count ranges are defined once in:

- `/Users/raj/kmp/hangman-compose/game-core/src/commonMain/kotlin/com/developersbreach/game/core/GameConfigs.kt`

Current mapping:

- `EASY`: `4..5`
- `MEDIUM`: `6..7`
- `HARD`: `8..10`

### Validation

Run:

```bash
GRADLE_USER_HOME=.gradle-local ./gradlew :game-core:compileKotlinMetadata :feature:game:compileDebugKotlinAndroid
```

The word DSL validator will fail the build if:

- any category is empty
- invalid characters exist
- duplicates exist
- any category does not have enough words for each difficulty bucket

## Pull Request Checklist

- Build passes locally.
- New/changed behavior is documented.
- No unrelated formatting or refactors included.
