package com.gabrielbarth.todolist.data

import com.gabrielbarth.todolist.domain.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    suspend fun insert(title: String, description: String?)

    suspend fun updateCompletion(id: Long, isCompleted: Boolean)

    suspend fun delete(id: Long)

    fun getAll(): Flow<List<Todo>>

    suspend fun getById(id: Long): Todo?
}