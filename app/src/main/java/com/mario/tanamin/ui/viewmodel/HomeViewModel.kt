package com.mario.tanamin.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mario.tanamin.data.container.TanamInContainer
import com.mario.tanamin.data.repository.TanamInRepository
import com.mario.tanamin.data.session.InMemorySessionHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: TanamInRepository = TanamInContainer().tanamInRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Profile data
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _coins = MutableStateFlow(0)
    val coins: StateFlow<Int> = _coins.asStateFlow()

    private val _streak = MutableStateFlow(0)
    val streak: StateFlow<Int> = _streak.asStateFlow()

    // Current level (from levels list)
    private val _currentLevel = MutableStateFlow("Level 1")
    val currentLevel: StateFlow<String> = _currentLevel.asStateFlow()

    // Pockets data for Investment Rate - menggunakan Long seperti WalletViewModel
    private val _mainWalletBalance = MutableStateFlow(0L)
    val mainWalletBalance: StateFlow<Long> = _mainWalletBalance.asStateFlow()

    private val _investmentWalletBalance = MutableStateFlow(0L)
    val investmentWalletBalance: StateFlow<Long> = _investmentWalletBalance.asStateFlow()

    private val _totalBalance = MutableStateFlow(0L)
    val totalBalance: StateFlow<Long> = _totalBalance.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                // Load profile
                loadProfile()
                // Load pockets
                loadPockets()
                // Load current level
                loadCurrentLevel()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error"
                Log.e("HomeViewModel", "Error loading home data", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun loadProfile() {
        try {
            val result = repository.getProfile()
            result.fold(
                onSuccess = { profile ->
                    _userName.value = profile.name
                    _coins.value = profile.coin
                    _streak.value = profile.streak
                    Log.d("HomeViewModel", "Profile loaded: ${profile.name}, coins=${profile.coin}, streak=${profile.streak}")
                },
                onFailure = { t ->
                    _errorMessage.value = t.message ?: "Failed to load profile"
                    Log.e("HomeViewModel", "Failed to load profile", t)
                }
            )
        } catch (e: Exception) {
            _errorMessage.value = e.message ?: "Failed to load profile"
            Log.e("HomeViewModel", "Exception loading profile", e)
        }
    }

    private suspend fun loadPockets() {
        try {
            // Convert userId String to Int seperti di WalletViewModel
            val userIdStr = InMemorySessionHolder.userId
            val userId = userIdStr?.toIntOrNull()
            if (userId == null) {
                _errorMessage.value = "No user id available"
                Log.e("HomeViewModel", "No user id available in memory")
                return
            }

            val result = repository.getPocketsByUser(userId)
            result.fold(
                onSuccess = { pockets ->
                    // Calculate main wallet balance (active main pockets) - menggunakan Long
                    val mainTotal = pockets.filter {
                        it.isActive && it.walletType.trim().equals("Main", ignoreCase = true)
                    }.sumOf { it.total }

                    // Calculate investment wallet balance - menggunakan Long
                    val investTotal = pockets.filter {
                        it.walletType.trim().equals("Investment", ignoreCase = true)
                    }.sumOf { it.total }

                    _mainWalletBalance.value = mainTotal
                    _investmentWalletBalance.value = investTotal
                    _totalBalance.value = mainTotal + investTotal

                    Log.d("HomeViewModel", "Pockets loaded: main=$mainTotal, investment=$investTotal, total=${mainTotal + investTotal}")
                },
                onFailure = { t ->
                    _errorMessage.value = t.message ?: "Failed to load pockets"
                    Log.e("HomeViewModel", "Failed to load pockets", t)
                }
            )
        } catch (e: Exception) {
            _errorMessage.value = e.message ?: "Failed to load pockets"
            Log.e("HomeViewModel", "Exception loading pockets", e)
        }
    }

    private suspend fun loadCurrentLevel() {
        try {
            val result = repository.getLevelsByUserDto()
            result.fold(
                onSuccess = { levels ->
                    // Find the current level (first incomplete or last completed)
                    val currentLevelData = levels.firstOrNull { !it.isCompleted }
                        ?: levels.lastOrNull()

                    if (currentLevelData != null) {
                        _currentLevel.value = "Level ${currentLevelData.id}"
                        Log.d("HomeViewModel", "Current level: Level ${currentLevelData.id}")
                    }
                },
                onFailure = { t ->
                    _errorMessage.value = t.message ?: "Failed to load levels"
                    Log.e("HomeViewModel", "Failed to load levels", t)
                }
            )
        } catch (e: Exception) {
            _errorMessage.value = e.message ?: "Failed to load levels"
            Log.e("HomeViewModel", "Exception loading levels", e)
        }
    }
}
