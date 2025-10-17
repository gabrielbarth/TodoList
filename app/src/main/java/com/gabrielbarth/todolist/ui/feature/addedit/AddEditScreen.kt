package com.gabrielbarth.todolist.ui.feature.addedit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gabrielbarth.todolist.data.TodoDatabaseProvider
import com.gabrielbarth.todolist.data.TodoRepositoryImpl
import com.gabrielbarth.todolist.ui.UiEvent
import com.gabrielbarth.todolist.ui.theme.TodoListTheme

@Composable
fun AddEditScreen(
    id: Long? = null,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current.applicationContext
    val database = TodoDatabaseProvider.provide(context)
    val repository = TodoRepositoryImpl(dao = database.todoDao)
    val viewModel = viewModel<AddEditViewModel> {
        AddEditViewModel(
            id = id,
            repository = repository
        )
    }

    val title = viewModel.title
    val description = viewModel.description

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = uiEvent.message
                    )
                }

                UiEvent.NavigateBack -> {
                    navigateBack()
                }

                is UiEvent.Navigate<*> -> {

                }
            }

        }
    }

    AddEditContent(
        title = title,
        description = description,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun AddEditContent(
    title: String,
    description: String?,
    onEvent: (AddEditEvent) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(AddEditEvent.OnSaveTodo)
                }
            ) {
                Icon(Icons.Default.Check, contentDescription = "Save Task")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .consumeWindowInsets(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { title ->
                    onEvent(
                        AddEditEvent.OnTitleChange(title)
                    )
                },
                placeholder = {
                    Text(text = "Task Title")
                }
            )

            Spacer(modifier = Modifier.padding(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = description ?: "",
                onValueChange = { description ->
                    onEvent(
                        AddEditEvent.OnDescriptionChange(description)
                    )
                },
                placeholder = {
                    Text(text = "Task Description")
                }
            )
        }
    }
}

@Preview
@Composable
private fun AddEditContentPreview() {
    TodoListTheme {
        AddEditContent(
            title = "",
            description = null,
            onEvent = {},
            snackbarHostState = SnackbarHostState()
        )
    }
}