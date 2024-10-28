package com.tifd.projectcomposed.data.model.local

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TaskRepository(application: Application) {
    private val mTaskDAO: TaskDAO
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = TaskDB.getDatabase(application)
        mTaskDAO = db.taskDao()
    }

    fun getAllTasks(): LiveData<List<Task>> = mTaskDAO.getAllTasks()

    fun insertTask(task: Task) {
        executorService.execute {
            mTaskDAO.insertTask(task)
        }
    }

    fun toggleDone(taskId: Int) {
        executorService.execute {
            mTaskDAO.toggleDone(taskId)
        }
    }
}