package com.compfest.aiapplication.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.compfest.aiapplication.data.Prediction

class AddViewModel: ViewModel() {
    private val _stayUpLate = MutableLiveData<Int>()
    val stayUpLate: LiveData<Int> = _stayUpLate

    private val _coffeeConsumption = MutableLiveData<Int>()
    val coffeeConsumption: LiveData<Int> = _coffeeConsumption

    private val _brainWorkingDuration = MutableLiveData<Int>()
    val brainWorkingDuration: LiveData<Int> = _brainWorkingDuration

    private val _pressureLevel = MutableLiveData<Int>()
    val pressureLevel: LiveData<Int> = _pressureLevel

    private val _stressLevel = MutableLiveData<Int>()
    val stressLevel: LiveData<Int> = _stressLevel

    private val _swimming = MutableLiveData<Int>()
    val swimming: LiveData<Int> = _swimming

    private val _hairWashing = MutableLiveData<Int>()
    val hairWashing: LiveData<Int> = _hairWashing

    private val _dandruff = MutableLiveData<Int>()
    val dandruff: LiveData<Int> = _dandruff

    fun saveDataFragmentOne(p0: String, p1: String, p2: String) {
        _stayUpLate.value = p0.toInt()
        _coffeeConsumption.value = p1.toInt()
        _brainWorkingDuration.value = p2.toInt()
    }

    fun saveDataFragmentTwo(p0: String, p1: String, p2: String, p3: String, p4: String) {
        _pressureLevel.value = p0.toInt()
        _stressLevel.value = p1.toInt()
        _swimming.value = p2.toInt()
        _hairWashing.value = p3.toInt()
        _dandruff.value = p4.toInt()
    }

    fun submitAll() {
        val predictionData = Prediction(
            stayUpLate = stayUpLate.value as Int,
            coffeeConsumption = coffeeConsumption.value as Int,
            brainWorkingDuration = brainWorkingDuration.value as Int,
            pressureLevel = pressureLevel.value as Int,
            stressLevel = stressLevel.value as Int,
            swimming = swimming.value as Int,
            hairWashing = hairWashing.value as Int,
            dandruff = dandruff.value as Int
        )
    }

    val viewModelName: String = AddViewModel::class.java.simpleName
}