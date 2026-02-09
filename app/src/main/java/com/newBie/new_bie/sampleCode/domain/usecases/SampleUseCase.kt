package com.newBie.new_bie.sampleCode.domain.usecases

import com.newBie.new_bie.sampleCode.data.repositories.SampleRepositoryImpl
import com.newBie.new_bie.sampleCode.domain.entities.SampleEntity
import com.newBie.new_bie.sampleCode.domain.repositories.SampleRepository

class SampleUseCase(
    private val repository: SampleRepository = SampleRepositoryImpl(),
) {
    suspend operator fun invoke(): List<SampleEntity> {
        return repository.getSampleList()
    }
}