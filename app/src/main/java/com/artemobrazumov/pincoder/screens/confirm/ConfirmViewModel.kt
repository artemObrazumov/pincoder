package com.artemobrazumov.pincoder.screens.confirm

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artemobrazumov.pincoder.shared_preferences.SharedPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConfirmViewModel(
    private val sharedPreferencesRepository: SharedPreferencesRepository
): ViewModel() {

    private val _state: MutableStateFlow<ConfirmScreenState> = MutableStateFlow(ConfirmScreenState())
    val state = _state.asStateFlow()
    private val _activityFinished: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val activityFinished = _activityFinished.asStateFlow()
    private var packageName: String? = null

    private val correctPinCode = sharedPreferencesRepository.pinCode

    init {
        if (correctPinCode.isNullOrEmpty()) {
            setNewPinMode()
        }
    }

    fun onPinCodeEdit(action: PinCodeEditAction) {
        when(action) {
            PinCodeEditAction.Delete -> {
                val newPinCode = state.value.pinCodeState.pinCode.dropLast(1)
                _state.update {
                    _state.value.copy(
                        pinCodeState = _state.value.pinCodeState.copy(pinCode = newPinCode)
                    )
                }
            }
            is PinCodeEditAction.Number -> {
                val newPinCode = state.value.pinCodeState.pinCode + action.number
                _state.update {
                    _state.value.copy(
                        pinCodeState = _state.value.pinCodeState.copy(pinCode = newPinCode)
                    )
                }

                if (newPinCode.length >= 4) {
                    onFullPinEntered(newPinCode)
                }
            }
        }
    }

    private fun onFullPinEntered(pinCode: String) {
        if (_state.value.isSettingNewPinCode) {
            updatePinCode(pinCode)
        } else {
            verifyPinCode(pinCode)
        }
    }

    private fun verifyPinCode(pinCode: String) {
        viewModelScope.launch {
            if (pinCode == correctPinCode) {
                _activityFinished.update { true }
                if (packageName != null) {
                    sharedPreferencesRepository.securedAppCache =
                        sharedPreferencesRepository.securedAppCache.toMutableList().apply {
                            add(packageName!!)
                        }
                }
            } else {
                _state.update {
                    _state.value.copy(
                        pinCodeVerificationState = PinCodeVerificationState.Incorrect
                    )
                }
            }
        }
    }

    private fun updatePinCode(newPinCode: String) {
        viewModelScope.launch {
            _state.update {
                _state.value.copy(
                    pinCodeUpdateState = PinCodeUpdateState.Updating
                )
            }
            try {
                sharedPreferencesRepository.pinCode = newPinCode
                _state.update {
                    _state.value.copy(
                        pinCodeUpdateState = PinCodeUpdateState.Success
                    )
                }
                _activityFinished.update { true }
            } catch (e: Exception) {
                _state.update {
                    _state.value.copy(
                        pinCodeUpdateState = PinCodeUpdateState.Error("Ошибка")
                    )
                }
            }
        }
    }

    fun setNewPinMode() {
        viewModelScope.launch {
            _state.update {
                _state.value.copy(isSettingNewPinCode = true)
            }
        }
    }

    fun setPackageName(packageName: String) {
        viewModelScope.launch {
            this@ConfirmViewModel.packageName = packageName
        }
    }
}