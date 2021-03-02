package com.github.rtyvZ.kitties.di

import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.repositories.RandomCatsRepository.KittiesPagingRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RandomKittiesModule {
    @Provides
    @ViewModelScoped
    fun providePagingRepo(api: Api): KittiesPagingRepo {
        return KittiesPagingRepo(api)
    }
}