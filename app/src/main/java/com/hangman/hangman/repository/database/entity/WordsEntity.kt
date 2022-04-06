package com.hangman.hangman.repository.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words_table")
data class WordsEntity(

    @PrimaryKey
    @ColumnInfo(name = "column_word_name")
    val wordName: String
)
