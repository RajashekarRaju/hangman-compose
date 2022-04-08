package com.hangman.hangman.ui.history

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hangman.hangman.repository.GameRepository
import com.hangman.hangman.repository.database.entity.HistoryEntity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class HistoryViewModel(
    application: Application,
    private val repository: GameRepository
) : AndroidViewModel(application) {

    var history by mutableStateOf(emptyList<HistoryEntity>())
    var date by mutableStateOf("")
    var time by mutableStateOf("")

    init {
        viewModelScope.launch {
            history = repository.getCompleteGameHistory()

            val dateAndTimeFormat = SimpleDateFormat("dd MMM,hh:mm a", Locale.getDefault())
            val dateAndTime: String = dateAndTimeFormat.format(Calendar.getInstance().time)
            date = dateAndTime.substringBefore(delimiter = ",")
            time = dateAndTime.substringAfter(delimiter = ",")
        }
    }

    companion object {

        fun provideFactory(
            application: Application,
            repository: GameRepository,
        ): ViewModelProvider.AndroidViewModelFactory {
            return object : ViewModelProvider.AndroidViewModelFactory(application) {
                @Suppress("unchecked_cast")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
                        return HistoryViewModel(application, repository) as T
                    }
                    throw IllegalArgumentException("Cannot create Instance for HistoryViewModel class")
                }
            }
        }
    }
}