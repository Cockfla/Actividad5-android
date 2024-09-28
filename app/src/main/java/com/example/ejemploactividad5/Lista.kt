package com.example.ejemploactividad5

import PokemonCard
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ejemploactividad5.Detalle
import com.example.ejemploactividad5.ui.theme.Ejemploactividad5Theme

class Lista : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Ejemploactividad5Theme {
                // Call the composable function here
                PokemonListScreen()
            }
        }
    }
}

@Composable
fun PokemonListScreen() {
    val context = LocalContext.current
    val repository = PokeRepository(PokeApiService.create())
    val viewModel: PokeViewModel = viewModel(factory = PokeViewModelFactory(repository))

    // Llama a fetchPokemonList solo si no se ha llamado antes
    LaunchedEffect(Unit) {
        viewModel.fetchPokemonList()
    }

    val pokemonList by viewModel.pokemonList.collectAsState()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (pokemonList.isEmpty()) {
            // Muestra un texto de "Cargando..." o un indicador de carga
            BasicText(text = "Cargando Pokémon...")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(pokemonList) { pokemon ->
                    PokemonCard(pokemon = pokemon) {
                        val intent = Intent(context, Detalle::class.java)
                        intent.putExtra("POKEMON_ID", pokemon.id)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}

