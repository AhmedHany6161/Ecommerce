package com.iti.team.ecommerce.ui.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.iti.team.ecommerce.model.local.room.OfflineDatabase
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingViewModel (application: Application) : AndroidViewModel(application) {
    private val modelRepository: ModelRepository =
        ModelRepository(OfflineDatabase.getInstance(application), application)
    fun logout() {
        modelRepository.setLogin(false)
        modelRepository.setEmail("")
        modelRepository.setAddressID(0)
        modelRepository.setAddress("")
        modelRepository.setDiscount(0)
        FirebaseAuth.getInstance().signOut()
        viewModelScope.launch(Dispatchers.IO) {
            modelRepository.reset()
        }

    }

}