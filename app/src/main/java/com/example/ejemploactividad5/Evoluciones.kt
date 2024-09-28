package com.example.ejemploactividad5

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.commit
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.ejemploactividad5.ui.theme.Ejemploactividad5Theme

class Evoluciones : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Ejemploactividad5Theme {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column {
                        Row {
                            EvolutionSearchScreen()
                        }
                        Row {
                            AndroidView(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .align(Alignment.CenterVertically),
                                factory = { contextoBotonera ->
                                    FrameLayout(contextoBotonera).apply {
                                        id = android.view.View.generateViewId()
                                    }
                                }) { frameLayout ->
                                supportFragmentManager.commit {
                                    replace(frameLayout.id, OpcionVolver())
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EvolutionSearchScreen() {
    val context = LocalContext.current
    val viewModel: PokeViewModel = viewModel(factory = PokeViewModelFactory(PokeRepository(PokeApiService.create())))
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val pokemonDetails by viewModel.pokemon.collectAsState() // Obtener los detalles del Pokémon
    val evolutionChain by viewModel.pokemonEvolutionChain.collectAsState() // Obtener la cadena evolutiva

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campo de búsqueda
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para iniciar la búsqueda
        Button(onClick = {
            val id = searchText.text.toIntOrNull()
            if (id != null) {
                viewModel.searchPokemonEvolution(id)
                viewModel.fetchPokemon(id) // Llamar a fetchPokemon para obtener los detalles
            }
        }) {
            Text(text = "Buscar Evoluciones")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar detalles del Pokémon (imagen, nombre e ID)
        pokemonDetails?.let { pokemon ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(pokemon.sprites.front_default),
                    contentDescription = pokemon.name,
                    modifier = Modifier.size(150.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Nombre: ${pokemon.name.capitalize()}")
                Text(text = "ID: ${pokemon.id}")
            }
        } ?: run {
            Text(text = "Introduce un ID o nombre para buscar un Pokémon")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar la cadena evolutiva
        evolutionChain?.let { evolutions ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Cadena Evolutiva:")
                evolutions.forEach { evolution ->
                    EvolutionStep(evolution)  // Esto está bien porque está dentro de un Composable
                }
            }
        } ?: run {
            Text(text = "Cargando evoluciones...")
        }
        Button(onClick = {
            context.startActivity(Intent(context, Menu::class.java))
        }) { Text("Volver") }

    }

}

@Composable
fun EvolutionStep(evolution: Evolution) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(evolution.imageUrl),
            contentDescription = evolution.name,
            modifier = Modifier.size(50.dp) // Ajusta el tamaño según tus necesidades
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = evolution.name.capitalize())
    }
}
