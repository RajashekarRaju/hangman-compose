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

- `/game/core/wordCategories/CountryWords.kt`
- `/game/core/wordCategories/LanguageWords.kt`
- `/game/core/wordCategories/CompanyWords.kt`
- `/game/core/wordCategories/AnimalWords.kt`

Do not edit gameplay logic just to add words.

### Rules

- Use lowercase words.
- Use letters and spaces only. Hyphens are accepted in source and normalized to spaces.
- Avoid duplicates inside the same category.
- Keep data realistic for gameplay length buckets.

### Centralized difficulty ranges

Difficulty letter-count ranges are defined once in:

- `game-core/src/commonMain/kotlin/com/developersbreach/game/core/GameConfigs.kt`

Current mapping:

- `EASY`: `4..5`
- `MEDIUM`: `6..7`
- `HARD`: `8..10`
- `VERY_HARD`: `11+`

### Validation

Run:

```bash
./gradlew :game-core:desktopTest :feature:game:desktopTest
```

The word DSL validator will fail the build if:

- any category is empty
- invalid characters exist
- duplicates exist
- any category does not have enough words for required difficulty buckets (`EASY`, `MEDIUM`, `HARD`)

## Pull Request Checklist

- Build passes locally.
- New/changed behavior is documented.
- No unrelated formatting or refactors included.
