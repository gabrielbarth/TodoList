package com.gabrielbarth.todolist.ui.feature.addedit

sealed interface AddEditEvent {
    data class OnTitleChange(val title: String) : AddEditEvent
    data class OnDescriptionChange(val description: String?) : AddEditEvent
    data object OnSaveTodo : AddEditEvent
}