package com.example.ejemploactividad5

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokeViewModel(private val repository: PokeRepository): ViewModel() {

    private val _pokemonList = MutableStateFlow<List<PokemonResponse>>(emptyList())
    val pokemonList: StateFlow<List<PokemonResponse>> = _pokemonList

    private val _pokemon = MutableStateFlow<PokemonResponse?>(null)
    val pokemon: StateFlow<PokemonResponse?> = _pokemon



    fun fetchPokemon(id: Int){
        viewModelScope.launch {
            try{
                val result = repository.getPokemon(id)
                _pokemon.value = result
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    // Cambia el tipo de la lista a List<Evolution>
    private val _pokemonEvolutionChain = MutableStateFlow<List<Evolution>?>(null)
    val pokemonEvolutionChain: StateFlow<List<Evolution>?> = _pokemonEvolutionChain

    suspend fun fetchPokemonEvolutions(id: Int) {
        try {
            // Obtener la especie del Pokémon
            val species = repository.getPokemonSpecies(id)

            // Obtener la ID de la cadena de evolución desde la URL
            val evolutionChainId = species.evolution_chain.url
                .split("/").filter { it.isNotEmpty() }.last().toInt()

            // Obtener la cadena de evolución
            val evolutionChain = repository.getEvolutionChain(evolutionChainId)

            // Recoger las evoluciones en una lista
            val evolutions = mutableListOf<Evolution>()
            var currentLink: ChainLink? = evolutionChain.chain

            while (currentLink != null) {
                // Aquí suponemos que puedes obtener el URL de la imagen
                // Necesitarás implementar un método en tu repositorio que obtenga el Pokémon por su nombre o ID
                val pokemonDetails = repository.getPokemonByName(currentLink.species.name) // Añadir este método en tu repositorio

                evolutions.add(Evolution(currentLink.species.name, pokemonDetails.sprites.front_default))
                currentLink = currentLink.evolves_to.firstOrNull()
            }

            _pokemonEvolutionChain.value = evolutions
        } catch (e: Exception) {
            e.printStackTrace() // Imprimir el error en el log
            _pokemonEvolutionChain.value = emptyList() // Devolver una lista vacía en caso de error
        }
    }
    fun fetchPokemonList() {
        viewModelScope.launch {
            try {
                val pokemonList = mutableListOf<PokemonResponse>()
                // Obtenemos los primeros 20 Pokémon (ajusta este número según tus necesidades)
                for (id in 1..50) {
                    val pokemon = repository.getPokemon(id)
                    pokemonList.add(pokemon)
                }
                _pokemonList.value = pokemonList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun fetchPokemonByName(name: String) {
        viewModelScope.launch {
            try {
                val result = repository.getPokemonByName(name)
                _pokemon.value = result
                fetchPokemonEvolutions(result.id.toInt())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun searchPokemonEvolution(id: Int) {
        viewModelScope.launch {
            fetchPokemonEvolutions(id)
        }
    }

}