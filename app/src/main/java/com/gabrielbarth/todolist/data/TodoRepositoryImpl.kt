package com.gabrielbarth.todolist.data

import com.gabrielbarth.todolist.domain.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoRepositoryImpl(
    private val dao: TodoDAO
) : TodoRepository {
    override suspend fun insert(title: String, description: String?, id: Long?) {
        val entity = id?.let { todoId ->
            dao.getById(todoId)?.copy(
                title = title,
                description = description,
            )
        } ?: TodoEntity(
            title = title,
            description = description,
            isCompleted = false,
        )
        dao.insert(entity)
    }

    override suspend fun updateCompleted(id: Long, isCompleted: Boolean) {
        val existingEntity = dao.getById(id) ?: return
        val updatedEntity = existingEntity.copy(isCompleted = isCompleted)
        dao.insert(updatedEntity)
    }

    override suspend fun delete(id: Long) {
        val existingEntity = dao.getById(id) ?: return
        dao.delete(existingEntity)
    }

    override fun getAll(): Flow<List<Todo>> {
        return dao.getAll().map { entities ->
            entities.map { entity ->
                Todo(
                    id = entity.id,
                    title = entity.title,
                    description = entity.description,
                    isCompleted = entity.isCompleted,
                )
            }
        }
    }

    override suspend fun getById(id: Long): Todo? {
        return dao.getById(id)?.let { entity ->
            Todo(
                id = entity.id,
                title = entity.title,
                description = entity.description,
                isCompleted = entity.isCompleted,
            )
        }
    }
}