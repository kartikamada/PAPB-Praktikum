package com.tifd.projectcomposed.data.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1)
abstract class TaskDB : RoomDatabase() {
    abstract fun taskDao() : TaskDAO

    companion object {
        @Volatile
        private var INSTANCE: TaskDB? = null

        @JvmStatic
        fun getDatabase(context: Context): TaskDB {
            if (INSTANCE == null) {
                synchronized(TaskDB::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        TaskDB::class.java,
                        "task_database",
                    ).build()
                }
            }
            return INSTANCE as TaskDB
        }
    }
}