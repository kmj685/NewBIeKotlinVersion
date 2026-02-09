package com.newBie.new_bie.sampleCode.domain.repositories

import com.newBie.new_bie.sampleCode.domain.entities.SampleEntity

interface SampleRepository {
    suspend fun getSampleList() : List<SampleEntity>
}