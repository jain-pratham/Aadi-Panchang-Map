package com.aadipanchang.map.util

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExperimentConfigModule {
  @Provides
  @Singleton
  fun provideExperimentConfig(impl: ExperimentConfigImpl): ExperimentConfig = impl
}
