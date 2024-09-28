package com.example.ejemploactividad5

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ejemploactividad5.ui.theme.Ejemploactividad5Theme
import coil.compose.rememberAsyncImagePainter

class Detalle : AppCompatActivity() {
    val repository = PokeRepository(PokeApiService.create())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val pokemonId = intent.getIntExtra("POKEMON_ID", 0)
            Ejemploactividad5Theme {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column {
                        Row {
                            PokemonEvolution(pokemonId)
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

    @Composable
    fun PokemonEvolution(id: Int) {
        val viewModel: PokeViewModel = viewModel(
            factory = PokeViewModelFactory(repository)
        )

        val evolutionState by viewModel.pokemonEvolutionChain.collectAsState()

        LaunchedEffect(id) {
            viewModel.fetchPokemonEvolutions(id)
        }

        evolutionState?.let { evolutions ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Evoluciones:")
                evolutions.forEach { evolution ->
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(evolution.imageUrl),
                            contentDescription = evolution.name,
                            modifier = Modifier.size(50.dp) // Ajusta el tamaño según tus necesidades
                        )
                        Text(text = evolution.name)
                    }
                }
            } ?: run {
                Text(text = "Cargando evoluciones...")
            }
        }
    }
}