package com.artemobrazumov.pincoder.screens.main

import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artemobrazumov.pincoder.service.PinAccessibilityService.Companion.APP_CLASS_NAME
import com.artemobrazumov.pincoder.shared_preferences.SharedPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val sharedPreferencesRepository: SharedPreferencesRepository
): ViewModel() {
    private val _state: MutableStateFlow<MainScreenState> = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()

    init {
        if (_state.value.pinCodeState is PinCodeState.Idle) {
            updatePinCodeState()
        }
    }

    private fun updatePinCodeState() {
        viewModelScope.launch {
            _state.update {
                _state.value.copy(pinCodeState = PinCodeState.Loading)
            }
            val newPinCodeState = PinCodeState.Content(
                isPinCodeSet = ( sharedPreferencesRepository.pinCode != null )
            )
            _state.update {
                _state.value.copy(pinCodeState = newPinCodeState)
            }
        }
    }

    fun updateAppsList(
        appsList: List<ResolveInfo>,
        packageManager: PackageManager
    ) {
        viewModelScope.launch {
            _state.update {
                _state.value.copy(appListState = AppListState.Loading)
            }
            val packagesSecured = sharedPreferencesRepository.packagesId
            val appsInfoList = appsList.mapNotNull {
                val activityInfo = it.activityInfo
                if (activityInfo.packageName == APP_CLASS_NAME) {
                    null
                } else {
                    val appInfo = activityInfo.applicationInfo
                    AppInfo(
                        packageId = activityInfo.packageName,
                        icon = appInfo.loadIcon(packageManager).toBitmap(),
                        name = appInfo.loadLabel(packageManager).toString(),
                        isSecured = packagesSecured.contains(activityInfo.packageName)
                    )
                }
            }
            val newAppListState = AppListState.Content(
                apps = appsInfoList
            )
            _state.update {
                _state.value.copy(appListState = newAppListState)
            }
        }
    }

    fun updateAccessibilityPermission(granted: Boolean) {
        _state.update {
            _state.value.copy(
                permissionState = _state.value.permissionState.copy(
                    isAccessibilityPermissionGranted = granted
                )
            )
        }
    }

    fun updateNotificationPermission(granted: Boolean) {
        _state.update {
            _state.value.copy(
                permissionState = _state.value.permissionState.copy(
                    isNotificationPermissionGranted = granted
                )
            )
        }
    }

    fun updateSecureState(packageId: String, secured: Boolean) {
        viewModelScope.launch {
            val packagesList = sharedPreferencesRepository.packagesId.toMutableSet()
            if (secured) {
                packagesList.add(packageId)
            } else {
                packagesList.remove(packageId)
            }
            sharedPreferencesRepository.packagesId = packagesList.toList()

            if (_state.value.appListState is AppListState.Content) {
                val contentState = (_state.value.appListState as AppListState.Content)
                val appsList = contentState.apps.toMutableList()
                val appIndex = appsList.indexOfFirst { it.packageId == packageId }
                appsList[appIndex] = appsList[appIndex].copy(isSecured = secured)
                _state.update {
                    _state.value.copy(
                        appListState = contentState.copy(apps = appsList)
                    )
                }
            }
        }
    }
}