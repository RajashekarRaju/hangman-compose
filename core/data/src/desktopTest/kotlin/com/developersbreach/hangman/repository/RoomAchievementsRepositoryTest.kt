package com.developersbreach.hangman.repository

import androidx.room.Room
import com.developersbreach.game.core.achievements.AchievementCatalog
import com.developersbreach.game.core.achievements.AchievementId
import com.developersbreach.game.core.achievements.AchievementStatCounters
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.game.core.achievements.initialProgress
import com.developersbreach.hangman.repository.database.GameDatabase
import com.developersbreach.hangman.repository.database.buildDatabase
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

class RoomAchievementsRepositoryTest {

    @Test
    fun `achievement progress persists and reads back`() = runTest {
        val databaseFile = tempDbFile("achievements-progress")
        val database = createDatabase(databaseFile)
        val repository = RoomAchievementsRepository(database)

        try {
            val firstBlood = AchievementCatalog.definitionFor(AchievementId.FIRST_BLOOD).initialProgress()
            val firstWin = AchievementCatalog.definitionFor(AchievementId.FIRST_WIN).initialProgress()
            val expected = listOf(
                firstBlood.copy(
                    isUnlocked = true,
                    isUnread = true,
                    unlockedAtEpochMillis = 1730000000000,
                    progressCurrent = firstBlood.progressTarget,
                ),
                firstWin.copy(progressCurrent = 1),
            )

            repository.replaceAchievementProgress(expected)

            val actual = repository.observeAchievementProgress().first()
            assertEquals(
                expected.sortedBy { it.achievementId.name },
                actual.sortedBy { it.achievementId.name },
            )
        } finally {
            database.close()
            databaseFile.delete()
        }
    }

    @Test
    fun `achievement stat counters persist and read back`() = runTest {
        val databaseFile = tempDbFile("achievements-stats")
        val database = createDatabase(databaseFile)
        val repository = RoomAchievementsRepository(database)

        try {
            val expected = AchievementStatCounters(
                gamesPlayed = 12,
                gamesWon = 8,
                gamesLost = 4,
                historyEntriesRecordedTotal = 11,
                currentWinStreak = 3,
                bestWinStreak = 5,
                totalHintsUsed = 7,
                gamesWonWithoutHints = 4,
                perfectWins = 2,
                highestScore = 321,
                winsByCategory = mapOf(
                    GameCategory.COUNTRIES to 3,
                    GameCategory.ANIMALS to 2,
                ),
                winsByDifficulty = mapOf(
                    GameDifficulty.EASY to 5,
                    GameDifficulty.HARD to 2,
                ),
            )

            repository.saveAchievementStatCounters(expected)

            val actual = repository.observeAchievementStatCounters().first()
            assertEquals(expected, actual)
        } finally {
            database.close()
            databaseFile.delete()
        }
    }

    @Test
    fun `achievement progress survives database reopen`() = runTest {
        val databaseFile = tempDbFile("achievements-reopen")

        val firstDatabase = createDatabase(databaseFile)
        val firstRepository = RoomAchievementsRepository(firstDatabase)
        val expected = AchievementCatalog.definitionFor(AchievementId.FIRST_BLOOD)
            .initialProgress()
            .copy(
                isUnlocked = true,
                unlockedAtEpochMillis = 1731000000000,
                progressCurrent = 1,
            )

        try {
            firstRepository.replaceAchievementProgress(listOf(expected))
        } finally {
            firstDatabase.close()
        }

        val reopenedDatabase = createDatabase(databaseFile)
        val reopenedRepository = RoomAchievementsRepository(reopenedDatabase)
        try {
            val actual = reopenedRepository.observeAchievementProgress().first()
                .first { it.achievementId == AchievementId.FIRST_BLOOD }
            assertEquals(expected, actual)
        } finally {
            reopenedDatabase.close()
            databaseFile.delete()
        }
    }
}

private fun createDatabase(databaseFile: File): GameDatabase {
    val builder = Room.databaseBuilder<GameDatabase>(
        name = databaseFile.absolutePath,
    )
    return buildDatabase(builder)
}

private fun tempDbFile(prefix: String): File {
    return File(
        System.getProperty("java.io.tmpdir"),
        "$prefix-${System.nanoTime()}.db",
    )
}
