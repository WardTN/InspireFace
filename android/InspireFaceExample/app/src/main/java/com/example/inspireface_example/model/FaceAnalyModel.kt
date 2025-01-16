package com.example.inspireface_example.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.inspireface_example.bean.FaceAnalyResult

class FaceAnalyModel(application: Application) : AndroidViewModel(application) {


    // face analy result
    var bean = MutableLiveData(FaceAnalyResult())

    // tab index
    var tabIndex = MutableLiveData(1)


    fun changeTab(index: Int) {
        tabIndex.value = index
    }

}