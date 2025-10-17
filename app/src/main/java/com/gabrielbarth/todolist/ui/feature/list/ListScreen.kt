package com.gabrielbarth.todolist.ui.feature.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gabrielbarth.todolist.data.TodoDatabaseProvider
import com.gabrielbarth.todolist.data.TodoRepositoryImpl
import com.gabrielbarth.todolist.domain.Todo
import com.gabrielbarth.todolist.domain.todo1
import com.gabrielbarth.todolist.domain.todo2
import com.gabrielbarth.todolist.domain.todo3
import com.gabrielbarth.todolist.navigation.AddEditRoute
import com.gabrielbarth.todolist.ui.UiEvent
import com.gabrielbarth.todolist.ui.components.TodoItem
import com.gabrielbarth.todolist.ui.theme.TodoListTheme

@Composable
fun ListScreen(
    navigateToAddEditScreen: (id: Long?) -> Unit,
) {
    val context = LocalContext.current.applicationContext
    val database = TodoDatabaseProvider.provide(context)
    val repository = TodoRepositoryImpl(dao = database.todoDao)
    val viewModel = viewModel<ListViewModel> {
        ListViewModel(repository = repository)
    }

    val todos = viewModel.todos.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.Navigate<*> -> {
                    when (uiEvent.route) {
                        is AddEditRoute -> {
                            navigateToAddEditScreen(uiEvent.route.id)
                        }
                    }
                }

                UiEvent.NavigateBack -> {}
                is UiEvent.ShowSnackbar -> {}
            }
        }
    }

    ListContent(
        todos = todos,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun ListContent(
    todos: List<Todo>,
    onEvent: (ListEvent) -> Unit,
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(ListEvent.OnAddEditClick(id = null))
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.consumeWindowInsets(paddingValues),
            contentPadding = PaddingValues(16.dp),
        ) {
            itemsIndexed(todos) { index, todo ->
                TodoItem(
                    todo = todo,
                    onCompletedChange = {
                        onEvent(ListEvent.OnCompleteTodoClick(id = todo.id, isCompleted = it))
                    },
                    onItemClick = {
                        onEvent(ListEvent.OnAddEditClick(id = todo.id))
                    },
                    onDeleteClick = {
                        onEvent(ListEvent.OnDeleteTodoClick(id = todo.id))
                    },
                )

                if (index < todos.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun ListContentPreview() {
    TodoListTheme {
        ListContent(
            todos = listOf(
                todo1,
                todo2,
                todo3
            ),
            onEvent = {}
        )
    }
}