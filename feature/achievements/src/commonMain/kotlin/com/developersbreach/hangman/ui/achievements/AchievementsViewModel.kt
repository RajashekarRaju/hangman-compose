package com.developersbreach.hangman.ui.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developersbreach.game.core.achievements.AchievementCatalog
import com.developersbreach.game.core.achievements.AchievementId
import com.developersbreach.game.core.achievements.AchievementProgress
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
    private var latestProgress: List<AchievementProgress> = emptyList()

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

            is AchievementsEvent.AchievementClicked -> {
                showAchievementDetails(event.achievementId)
            }

            AchievementsEvent.AchievementDetailsDismissed -> {
                _uiState.update { current -> current.copy(selectedAchievement = null) }
            }

            is AchievementsEvent.GroupToggleClicked -> {
                _uiState.update { current ->
                    val collapsed = current.collapsedGroups.toMutableSet()
                    if (!collapsed.add(event.group)) {
                        collapsed.remove(event.group)
                    }
                    current.copy(collapsedGroups = collapsed)
                }
            }
        }
    }

    private fun observeAchievements() {
        viewModelScope.launch {
            repository.observeAchievementProgress().collect { storedProgress ->
                latestProgress = storedProgress.normalizeProgress()
                val byId = latestProgress.associateBy { value -> value.achievementId }
                val items = AchievementCatalog.definitions.map { definition ->
                    val progress = byId[definition.id] ?: definition.initialProgress()
                    AchievementItemUiState(
                        id = definition.id,
                        group = definition.group,
                        title = definition.title,
                        description = definition.description,
                        isUnlocked = progress.isUnlocked,
                        isUnread = progress.isUnread,
                        progressCurrent = progress.progressCurrent,
                        progressTarget = progress.progressTarget,
                        unlockedAtLabel = progress.unlockedAtEpochMillis?.let(::formatEpochMillis),
                    )
                }
                val summary = latestProgress.toAchievementsSummary()
                _uiState.update { current ->
                    val selectedId = current.selectedAchievement?.id
                    val selectedAchievement = selectedId?.let { id ->
                        items.firstOrNull { item -> item.id == id }?.toDetails()
                    }
                    current.copy(
                        items = items,
                        summary = summary,
                        selectedAchievement = selectedAchievement,
                    )
                }
            }
        }
    }

    private fun showAchievementDetails(achievementId: AchievementId) {
        val selected = _uiState.value.items.firstOrNull { value -> value.id == achievementId } ?: return
        _uiState.update { current ->
            current.copy(selectedAchievement = selected.toDetails())
        }
        if (selected.isUnlocked && selected.isUnread) {
            markAchievementAsRead(achievementId)
        }
    }

    private fun markAchievementAsRead(achievementId: AchievementId) {
        val updatedProgress = latestProgress.map { progress ->
            if (progress.achievementId == achievementId) progress.copy(isUnread = false) else progress
        }
        if (updatedProgress == latestProgress) return
        latestProgress = updatedProgress
        viewModelScope.launch {
            repository.replaceAchievementProgress(updatedProgress)
        }
    }
}
