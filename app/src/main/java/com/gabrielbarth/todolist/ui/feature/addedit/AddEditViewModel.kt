package com.gabrielbarth.todolist.ui.feature.addedit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabrielbarth.todolist.data.TodoRepository
import com.gabrielbarth.todolist.ui.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditViewModel(
    private val id: Long? = null,
    private val repository: TodoRepository,
) : ViewModel() {

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf<String?>(null)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        id?.let { todoId ->
            viewModelScope.launch {
                val todo = repository.getById(todoId)
                todo?.let {
                    title = it.title
                    description = it.description
                }
            }
        }
    }


    fun onEvent(event: AddEditEvent) {
        when (event) {
            is AddEditEvent.OnTitleChange -> {
                title = event.title
            }

            is AddEditEvent.OnDescriptionChange -> {
                description = event.description
            }

            is AddEditEvent.OnSaveTodo -> {
                saveTodo()
            }
        }
    }

    private fun saveTodo() {
        viewModelScope.launch {
            if (title.isBlank()) {
                _uiEvent.send(UiEvent.ShowSnackbar("The title cannot be empty"))
                return@launch
            }
            repository.insert(title, description, id)
            _uiEvent.send(UiEvent.NavigateBack)
        }
    }
}