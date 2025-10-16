package com.gabrielbarth.todolist.domain

data class Todo(
    val id: Long,
    val title: String,
    val description: String?,
    val isCompleted: Boolean,
)

// fake object for testing
val todo1 = Todo(
    id = 1,
    title = "Buy groceries",
    description = "Milk, Bread, Eggs, Butter",
    isCompleted = false,
)

val todo2 = Todo(
    id = 2,
    title = "Walk the dog",
    description = "Take Fido for a walk in the park",
    isCompleted = true,
)

val todo3 = Todo(
    id = 3,
    title = "Read a book",
    description = "Finish reading 'The Great Gatsby'",
    isCompleted = false,
)
