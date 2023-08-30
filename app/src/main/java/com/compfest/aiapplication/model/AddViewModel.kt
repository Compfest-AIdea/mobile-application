package com.compfest.aiapplication.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.compfest.aiapplication.data.Prediction
import com.compfest.aiapplication.ui.fragment.AddFragmentThree

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

    private val _image1 = MutableLiveData<String?>()
    val image1 : LiveData<String?> = _image1

    private val _image2 = MutableLiveData<String?>()
    val image2 : LiveData<String?> = _image2

    private val _image3 = MutableLiveData<String?>()
    val image3 : LiveData<String?> = _image3

    private val _image4 = MutableLiveData<String?>()
    val image4 : LiveData<String?> = _image4

    private val _image5 = MutableLiveData<String?>()
    val image5 : LiveData<String?> = _image5

    fun saveAllImage(img1: String? = null, img2: String? = null, img3: String? = null, img4: String? = null, img5: String? = null) {
        _image1.value = img1
        _image2.value = img2
        _image3.value = img3
        _image4.value = img4
        _image5.value = img5
    }

    fun saveImage(imgPath: String? = null, imgSelector: Int?) {
        when (imgSelector) {
            AddFragmentThree.IMG_SELECTOR_1 -> {
                saveAllImage(img1 = imgPath)
            }
            AddFragmentThree.IMG_SELECTOR_2 -> {
                saveAllImage(img2 = imgPath)
            }
            AddFragmentThree.IMG_SELECTOR_3 -> {
                saveAllImage(img3 = imgPath)
            }
            AddFragmentThree.IMG_SELECTOR_4 -> {
                saveAllImage(img4 = imgPath)
            }
            AddFragmentThree.IMG_SELECTOR_5 -> {
                saveAllImage(img5 = imgPath)
            }
            else -> {}
        }
    }
}