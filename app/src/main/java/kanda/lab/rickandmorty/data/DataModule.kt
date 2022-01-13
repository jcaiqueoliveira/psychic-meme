package kanda.lab.rickandmorty.data

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    abstract fun bindRickAndMortyService(infra : RickAndMortyInfraStructure): RickAndMortyService

}

@Module
@InstallIn(SingletonComponent::class)
internal class GatewayModule {

    @Provides
    @Singleton
    fun provideRickAndMortyGateway(): RickAndMortyGateway =
        BuildRetrofit().create(RickAndMortyGateway::class.java)

}