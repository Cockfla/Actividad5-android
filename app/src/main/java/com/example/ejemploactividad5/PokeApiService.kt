package com.example.ejemploactividad5

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

// Definición de la interfaz del servicio API
interface PokeApiService {

    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") id: Int): PokemonResponse

    @GET("pokemon/{name}") // Método añadido para obtener Pokémon por nombre
    suspend fun getPokemonByName(@Path("name") name: String): PokemonResponse

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(@Path("id") id: Int): PokemonSpecies

    // Obtener la cadena de evolución
    @GET("evolution-chain/{id}")
    suspend fun getEvolutionChain(@Path("id") id: Int): EvolutionChain

    companion object {
        private const val BASE_URL = "https://pokeapi.co/api/v2/"

        fun create(): PokeApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(PokeApiService::class.java)
        }
    }
}

// Clases de datos para la respuesta del API
data class PokemonResponse (
    val baseExperience: Long,
    val height: Long,
    val id: Long,
    val name: String,
    val order: Long,
    val weight: Long,
    val sprites: Sprites,
    val abilities: List<Ability>,
    val types: List<Type>,
    val species: Species,
    val game_indices: List<GameIndex>
)
data class GameIndex(
    val game_index: Int,
    val version: Version
)

data class Version(
    val name: String,
    val url: String
)

data class Sprites(
    val front_default: String,  // La URL del sprite
    val other: OtherSprites
)

data class OtherSprites(
    val official_artwork: OfficialArtwork
)

data class OfficialArtwork(
    val front_default: String // La URL del arte oficial
)

data class PokemonSpecies(
    val evolution_chain: EvolutionChainUrl
)

data class EvolutionChainUrl(
    val url: String
)

data class EvolutionChain(
    val chain: ChainLink
)

data class ChainLink(
    val species: NamedApiResource,
    val evolves_to: List<ChainLink>
)

data class NamedApiResource(
    val name: String
)

data class Evolution(
    val name: String,
    val imageUrl: String
)

data class Ability(val ability: AbilityDetails)
data class AbilityDetails(val name: String)
data class Type(val type: TypeDetails)
data class TypeDetails(val name: String)
data class Species(val url: String)