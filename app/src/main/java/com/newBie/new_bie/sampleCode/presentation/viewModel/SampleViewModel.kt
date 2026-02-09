package com.newBie.new_bie.sampleCode.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.sampleCode.domain.entities.SampleEntity
import com.newBie.new_bie.sampleCode.domain.usecases.SampleUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class SampleViewModel: ViewModel() {

    var sampleInfo : MutableStateFlow<SampleEntity?> = MutableStateFlow(null)

    val sampleUseCase : SampleUseCase = SampleUseCase()
    init {
        Log.d(Constants.TAG, "SampleViewModel init")
    }

}