package com.gabrielbarth.todolist.ui.feature.list

sealed interface ListEvent {
    data class OnDeleteTodoClick(val id: Long) : ListEvent
    data class OnCompleteTodoClick(val id: Long, val isCompleted: Boolean) : ListEvent
    data class OnAddEditClick(val id: Long?) : ListEvent
}