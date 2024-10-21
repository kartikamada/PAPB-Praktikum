package com.tifd.projectcomposed.viewmodel
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tifd.projectcomposed.Profile
import com.tifd.projectcomposed.ProfileRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*

class ProfileViewModel : ViewModel() {
    private val profileRepository = ProfileRepository()

    private val _user = MutableStateFlow<Profile?>(null)
    val user: StateFlow<Profile?> = _user.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

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
