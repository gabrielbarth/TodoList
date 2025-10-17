package com.gabrielbarth.todolist.ui.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabrielbarth.todolist.data.TodoRepository
import com.gabrielbarth.todolist.navigation.AddEditRoute
import com.gabrielbarth.todolist.ui.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListViewModel(
    private val repository: TodoRepository,
) : ViewModel() {

    val todos = repository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ListEvent) {
        when (event) {
            is ListEvent.OnDeleteTodoClick -> {
                deleteTodo(event.id)
            }

            is ListEvent.OnCompleteTodoClick -> {
                completeTodo(event.id, event.isCompleted)
            }

            is ListEvent.OnAddEditClick -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(AddEditRoute(id = event.id)))
                }
            }
        }
    }

    private fun deleteTodo(id: Long) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }

    private fun completeTodo(id: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.updateCompleted(id, isCompleted)
        }
    }
}