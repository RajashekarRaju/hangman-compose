# :video_game: Hangman - Built With Compose :paintbrush:

### Thanks to [Cicero Hellmann](https://github.com/cicerohellmann) For giving this cool idea to build Hangman game with Compose for Android.

![Game Banner](/assets/banner_hangman.png)

## :arrow_down: Download

<a href="https://play.google.com/store/apps/details?id=com.developersbreach.hangman&hl=en_IN&gl=US" target="_blank">
<img src="https://play.google.com/intl/en_gb/badges/static/images/badges/en_badge_web_generic.png" width=240 />
</a>

## :sparkler: What's New: v0.1.0

- [x] Players will be challenged with unique words only.
- [x] Players can view their score/game history.
- [x] Players should know how many unique words can still guess.
- [x] Players can see how many attempts they have to guess the word.
- [x] Players can try out letters in any order they want.
- [x] Players can to know how many points made when I completed a word.
- [x] Players can to know how many points scored by completing all levels.
- [x] Persist the game state at any point of the game.

## :roller_coaster: Roadmap: v0.2.0

- [ ] Support game in landscape mode.
- [ ] Add more guessing categories.
- [ ] Players can have hints for each level.
- [ ] Improve game user experience.
- [ ] Fix issues for smaller screen devices.
- [ ] Replace all Shared Preferences with DataStore.

## :dango: App Overview


| Screen | Red Theme | Teal Theme |
| :----- | :-: | :--: |
| **OnBoarding Screen** <br><br> • Default destination for the app.<br> • Players can choose different game categories from dialog.<br> • Players can choose difficulty mode through slider.<br> • Highest score will automatically update from game history data.<br> • Players can mute/unmute game background music.<br> • Players can read the game instructions from dialog.<br> • Shared preferences are used to save game category and difficulty.<br> | <img src="/assets/onboarding_red.png" width="200" /> | <img src="/assets/onboarding_teal.png" width="200" /> |
| **Game Screen** <br><br> • Destination for playing the game.<br> • Game loads with default game preferences last saved by the player.<br> • Players can check their points and current level.<br> • Players can view the number of attempts left to guess the word.<br> • Once player completes all levels, a dialog will show up with total points scored.<br> • At any level if player loses the game, dialog will be shown with word which isn't guessed correctly.<br> • Can access the game instructions dialog to see which game mode is running.<br> • Players cannot reuse the same letter which is already guessed.<br> • Players cannot directly exit the game, a modal sheet will confirm the exit.<br> • Players can view which letters are correctly being guessed.<br> | <img src="/assets/game_red.png" width="200" /> | <img src="/assets/game_teal.png" width="200" /> |
| **History Screen** <br><br> • Players can see their whole game history.<br> • Made use of room database to store all the history data.<br> • Each game history item has properties overall score, category, difficulty, level, won/lost info, time/date of play will be saved.<br> • Any specific game item history can be deleted from database by swiping.<br> • When no history is available a simple text message will be shown.<br> • Game progress will be saved only when player loses or wins but not when exits.<br> | <img src="/assets/history_red.png" width="200" /> | <img src="/assets/history_teal.png" width="200" /> |


| Adjust Difficulty Mode | Game Instructions | Game Exit Modal Sheet |
| :--------------------: | :---------------: | :-------: |
| <img src="/assets/difficulty_red.png" width="200"/> | <img src="/assets/categories_red.png" width="200" /> | <img src="/assets/instructions_red.png" width="200" /> |
| Game Won Dialog | Game Lost Dialog | Game Exit |
| <img src="/assets/game_won_red.png" width="200"/> | <img src="/assets/game_lost_red.png" width="200" /> | <img src="/assets/exit_red.png" width="200" /> |

## :hourglass_flowing_sand: Experiencing slow game performance ?

Rendering times with Compose is slower compared to XML. I've added few references below which could
help you improve app performance.

- Change app module to choose build variant Release mode.
- Enable R8 and disable debug to speed up the performance

#### References :bookmark_tabs:

- Article by William Shelor - [Measuring Render Performance with Jetpack Compose](https://engineering.premise.com/measuring-render-performance-with-jetpack-compose-c0bf5814933).
- Article by Chris Banes - [Composable Metrics](https://chris.banes.dev/composable-metrics/)

## :jack_o_lantern: Teal/Red Theme Colors

```kotlin
val TealColorPalette = lightColors(
  primary = tealPrimary,
  background = tealBackground,
  onBackground = tealOnBackground,
  surface = tealSurface,
  onSurface = tealOnSurface
)

val RedColorPalette = darkColors(
  primary = redPrimary,
  background = redBackground,
  onBackground = redOnBackground,
  surface = redSurface,
  onSurface = redOnSurface
)
```

## :bulb: Motivation and Context

`Jetpack Compose` is Android’s modern toolkit for building native UI. It enables you to quickly
bring your app to life with less code, powerful tools, and intuitive Kotlin APIs.

Understanding to implement own `Theme` `Shape` `Typography` `Color` has became bit easier by
referring to lot of official jetpack compose samples which are available in GitHub.

Best of all we got to do this in `Kotlin` way. Excited and long way to go from here.

## :loudspeaker: What To Contribute ?

* Have a cool idea or feature
  request? [Start a discussion](https://github.com/RajashekarRaju/hangman-compose/discussions).
* Want to contribute or add new feature to app? Send your pull request.
* Game crashes while playing? Open a new issue, describe the problem.

## :trophy: Credits

### :loud_sound: Game Sounds - [Pixabay.com](https://pixabay.com/)

```
PIXABAY LICENSE CERTIFICATE

All sounds used in game are only for user experience purpose.
I do not own any of those audio content. All credit belongs to Music Author
uploaded to Pixabay.
```

I hold the licenses for audio files used in this app.
[Verify all license certificates](https://github.com/RajashekarRaju/hangman-compose/tree/master/pixabay-license).

### :black_nib: Creepster Font

This fonts is licensed under the Open Font License, this is the only font I've used in my app.

You can refer to this font from [Google Fonts](https://fonts.google.com/specimen/Creepster#about).

### :space_invader: Icons

For the background images I have used in game, I have modified and used few icons from
[unDraw](https://undraw.co/) and [iconscout](https://iconscout.com/).

## :scroll: License

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
