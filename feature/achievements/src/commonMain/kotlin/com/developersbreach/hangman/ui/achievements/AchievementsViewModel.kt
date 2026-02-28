package com.developersbreach.hangman.ui.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developersbreach.game.core.achievements.AchievementCatalog
import com.developersbreach.game.core.achievements.initialProgress
import com.developersbreach.hangman.repository.AchievementsRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AchievementsViewModel(
    private val repository: AchievementsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AchievementsUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<AchievementsEffect>()
    val effects: SharedFlow<AchievementsEffect> = _effects.asSharedFlow()

    init {
        observeAchievements()
    }

    fun onEvent(event: AchievementsEvent) {
        when (event) {
            AchievementsEvent.NavigateUpClicked -> {
                viewModelScope.launch {
                    _effects.emit(AchievementsEffect.NavigateUp)
                }
            }
        }
    }

    private fun observeAchievements() {
        viewModelScope.launch {
            repository.observeAchievementProgress().collect { storedProgress ->
                val normalizedProgress = storedProgress.normalizeProgress()
                val byId = normalizedProgress.associateBy { value -> value.achievementId }
                val items = AchievementCatalog.definitions.map { definition ->
                    val progress = byId[definition.id] ?: definition.initialProgress()
                    AchievementItemUiState(
                        id = definition.id,
                        group = definition.group,
                        title = definition.title,
                        description = definition.description,
                        isUnlocked = progress.isUnlocked,
                        progressCurrent = progress.progressCurrent,
                        progressTarget = progress.progressTarget,
                        unlockedAtLabel = progress.unlockedAtEpochMillis?.let(::formatEpochMillis),
                    )
                }
                val summary = normalizedProgress.toAchievementsSummary()
                _uiState.update { current ->
                    current.copy(
                        items = items,
                        summary = summary,
                    )
                }
            }
        }
    }
}
