package com.compfest.aiapplication.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.compfest.aiapplication.data.PredictionRepository
import com.compfest.aiapplication.data.PredictionResult

class HomeViewModel(private val repository: PredictionRepository): ViewModel() {
    val predictionResult: LiveData<PagedList<PredictionResult>> = repository.getPredictionResult()

    val recentPredictionResult: LiveData<PredictionResult?> = repository.getRecentPredictionResult()
}