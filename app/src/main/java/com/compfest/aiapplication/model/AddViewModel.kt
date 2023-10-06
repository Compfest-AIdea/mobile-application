package com.compfest.aiapplication.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.compfest.aiapplication.data.PredictionImageInput
import com.compfest.aiapplication.data.PredictionTabularInput

class AddViewModel: ViewModel() {
    val viewModelName: String = AddViewModel::class.java.simpleName

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
        _stayUpLate.value = if (p0.isNotBlank()) p0.toInt() else 0
        _coffeeConsumption.value = if (p1.isNotBlank()) p1.toInt() else 0
        _brainWorkingDuration.value = if (p2.isNotBlank()) p2.toInt() else 0
    }

    fun saveDataFragmentTwo(p0: String, p1: String, p2: String, p3: String, p4: String) {
        _pressureLevel.value = p0.toInt()
        _stressLevel.value = p1.toInt()
        _swimming.value = p2.toInt()
        _hairWashing.value = p3.toInt()
        _dandruff.value = p4.toInt()
    }

    fun getTabularInputData(): Map<String, Float> {
        val predictionData = getParcelableTabularInputData()
        return mapOf(
            "stay_up_late" to predictionData.stayUpLate.toFloat(),
            "coffee_consumed" to predictionData.coffeeConsumption.toFloat(),
            "brain_working_duration" to predictionData.brainWorkingDuration.toFloat(),
            "pressure_level" to predictionData.pressureLevel.toFloat(),
            "stress_level" to predictionData.stressLevel.toFloat(),
            "swimming" to predictionData.swimming.toFloat(),
            "hair_washing" to predictionData.hairWashing.toFloat(),
            "dandruff" to predictionData.dandruff.toFloat()
        )
    }

    fun getParcelableTabularInputData(): PredictionTabularInput {
        return PredictionTabularInput(
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

    private val _imgPath = MutableLiveData<String?>()
    val imgPath : LiveData<String?> = _imgPath

    fun saveImage(imgPath: String?) {
        _imgPath.value = imgPath
    }

    fun getImage(): String? {
        return imgPath.value
    }

    fun getParcelableImageInputData(): PredictionImageInput {
        return PredictionImageInput(
            imagePath = imgPath.value as String
        )
    }
}