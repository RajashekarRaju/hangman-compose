package com.developersbreach.hangman.ui.common.notification

import com.developersbreach.game.core.achievements.AchievementId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface AchievementNotificationCoordinator {
    val bannerState: StateFlow<AchievementBannerUiState>

    fun enqueueUnlocked(unlockedIds: List<AchievementId>)
}

class DefaultAchievementNotificationCoordinator(
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate),
    private val visibleMillis: Long = 2_500L,
    private val exitMillis: Long = 250L,
) : AchievementNotificationCoordinator {

    private val _bannerState = MutableStateFlow(AchievementBannerUiState())
    override val bannerState: StateFlow<AchievementBannerUiState> = _bannerState.asStateFlow()

    private val queue = ArrayDeque<AchievementBannerPayload>()
    private val queueMutex = Mutex()
    private var loopJob: Job? = null

    override fun enqueueUnlocked(unlockedIds: List<AchievementId>) {
        if (unlockedIds.isEmpty()) return

        scope.launch {
            queueMutex.withLock {
                queue.addAll(unlockedIds.map(AchievementId::toBannerPayload))
                if (loopJob?.isActive != true) {
                    loopJob = launchBannerLoop()
                }
            }
        }
    }

    private fun launchBannerLoop(): Job {
        return scope.launch {
            while (isActive) {
                val nextPayload = queueMutex.withLock {
                    queue.removeFirstOrNull()
                } ?: break

                _bannerState.value = AchievementBannerUiState(
                    payload = nextPayload,
                    isVisible = true,
                )
                delay(visibleMillis)

                _bannerState.value = AchievementBannerUiState(
                    payload = nextPayload,
                    isVisible = false,
                )
                delay(exitMillis)
            }

            _bannerState.value = AchievementBannerUiState()
        }
    }
}
