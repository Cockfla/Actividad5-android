package com.example.ejemploactividad5

class PokeRepository(private val apiService: PokeApiService) {

    // Obtener los detalles de un Pokémon
    suspend fun getPokemon(id: Int): PokemonResponse {
        return apiService.getPokemon(id)
    }

    // Obtener la especie del Pokémon
    suspend fun getPokemonSpecies(id: Int): PokemonSpecies {
        return apiService.getPokemonSpecies(id)
    }

    // Obtener la cadena de evolución
    suspend fun getEvolutionChain(id: Int): EvolutionChain {
        return apiService.getEvolutionChain(id)
    }

    suspend fun getPokemonByName(name: String): PokemonResponse {
        return apiService.getPokemonByName(name) // Asegúrate de tener un endpoint que permita esto en tu servicio
    }

}