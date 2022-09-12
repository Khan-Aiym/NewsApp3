package com.example.homework41.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {
    private val mText: MutableLiveData<String> = MutableLiveData()
    val text: LiveData<String>
        get() = mText

    init {
        mText.value = "This is notifications fragment"
    }
}