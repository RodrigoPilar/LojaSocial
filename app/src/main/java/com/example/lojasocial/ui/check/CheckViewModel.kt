package com.example.lojasocial.ui.check

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.repositories.CheckInOutRepository
import kotlinx.coroutines.launch

class CheckViewModel(private val repository: CheckInOutRepository) : ViewModel() {

    private val _checkInOutState = MutableLiveData<CheckInOutState>()
    val checkInOutState: LiveData<CheckInOutState> = _checkInOutState

    init {
        _checkInOutState.value = CheckInOutState("idle")
    }

    fun checkIn(userId: String) {
        viewModelScope.launch {
            try {
                _checkInOutState.value = CheckInOutState("loading")
                val result = repository.checkIn(userId)
                _checkInOutState.value = result
            } catch (e: Exception) {
                // Lidar com erros
                _checkInOutState.value = CheckInOutState("error")
            }
        }
    }

    fun checkOut(userId: String) {
        viewModelScope.launch {
            try {
                _checkInOutState.value = CheckInOutState("loading")
                val result = repository.checkOut(userId)
                _checkInOutState.value = result
            } catch (e: Exception) {
                // Lidar com erros
                _checkInOutState.value = CheckInOutState("error")
            }
        }
    }
}