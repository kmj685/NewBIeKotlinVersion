package com.newBie.new_bie.sampleCode.data.repositories

import com.newBie.new_bie.sampleCode.domain.entities.SampleEntity
import com.newBie.new_bie.sampleCode.domain.repositories.SampleRepository

class SampleRepositoryImpl : SampleRepository {
    override suspend fun getSampleList(): List<SampleEntity> {
        return emptyList();
    }
}