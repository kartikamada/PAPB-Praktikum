package com.tifd.projectcomposed.viewmodel
import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tifd.projectcomposed.data.model.network.Profile
import com.tifd.projectcomposed.data.model.network.ProfileRepository
import com.tifd.projectcomposed.data.model.local.Task
import com.tifd.projectcomposed.data.model.local.TaskRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*

class MainViewModel(application: Application) : ViewModel() {
    private val taskRepository = TaskRepository(application)
    private val profileRepository = ProfileRepository()

    private val _tasksList = taskRepository.getAllTasks()
    val tasksList: LiveData<List<Task>> get() = _tasksList

    private val _user = MutableStateFlow<Profile?>(null)
    val user: StateFlow<Profile?> = _user.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchAllTasks()
    }

    private fun fetchAllTasks() {
        viewModelScope.launch {
            taskRepository.getAllTasks()
        }
    }

    fun addTask(subject: String, taskDetails: String, imageUri: Uri?) {
        val task = Task(subject = subject, taskDetails = taskDetails, imageUri = imageUri)
        viewModelScope.launch {
            taskRepository.insertTask(task)
        }
    }

    fun toggleDone(taskId: Int) {
        viewModelScope.launch {
            taskRepository.toggleDone(taskId)
        }
    }

    fun getProfileUser(username: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val profile = profileRepository.getProfile(username)
                _user.value = profile
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
