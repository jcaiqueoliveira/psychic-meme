package kanda.lab.rickandmorty.common

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanda.lab.rickandmorty.common.data.BuildRetrofit
import kanda.lab.rickandmorty.common.data.RickAndMortyGateway
import kanda.lab.rickandmorty.common.data.RickAndMortyInfraStructure
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class InfraModule {

    @Provides
    @Singleton
    fun bindRickAndMortyService(service: RickAndMortyGateway): RickAndMortyInfraStructure =
        RickAndMortyInfraStructure(service)

    @Provides
    @Singleton
    fun provideRickAndMortyGateway(): RickAndMortyGateway =
        BuildRetrofit().create(RickAndMortyGateway::class.java)

}