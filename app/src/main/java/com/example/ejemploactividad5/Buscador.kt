package com.example.ejemploactividad5
import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup.Input
import android.widget.FrameLayout
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.commit
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ejemploactividad5.ui.theme.Ejemploactividad5Theme
import coil.compose.rememberAsyncImagePainter
import com.example.ejemploactividad5.Detalle


class   Buscador : AppCompatActivity() {

    val repository = PokeRepository(PokeApiService.create())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejemploactividad5Theme {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        var searchQuery by remember { mutableStateOf("0") } // Estado para el valor ingresado
                        var pokemonId by remember { mutableStateOf(0) } // Estado del ID para mostrar

                        // Input para buscar el Pokémon
                        Row(modifier = Modifier.background(Color.LightGray)) {
                            CardBuscador(
                                searchQuery = searchQuery,
                                onSearchQueryChange = { searchQuery = it }, // Actualiza el valor ingresado
                                onSearch = { pokemonId = searchQuery.toIntOrNull() ?: 0 } // Al presionar buscar, actualiza el ID de Pokémon
                            )
                        }

                        // Mostrar los detalles del Pokémon basado en el ID
                        Row {
                            CardDetallePokemon(pokemonId)
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
     fun CardBuscador(
         searchQuery: String, // Parámetro para el valor ingresado
         onSearchQueryChange: (String) -> Unit, // Función para manejar cambios en el campo de texto
         onSearch: () -> Unit // Función para manejar el clic en el botón buscar
     ){
         Row{
             Card(
                 modifier = Modifier.padding(10.dp)
             ) {
                 Text("Ingrese Id o Nombre de Pokemon")
                 Spacer(modifier = Modifier.height(18.dp))
                 TextField(
                     value = searchQuery,
                     onValueChange = { onSearchQueryChange(it) },
                     modifier = Modifier.background(color = Color.Blue)
                 )
                 Spacer(modifier = Modifier.height(18.dp))
                 Button(
                     onClick = { onSearch() }
                 ){
                     Text("Buscar ")
                     Image(
                         painter = painterResource( id = R.drawable.pokebola),
                         contentDescription = "pokebola",
                         modifier = Modifier.size(30.dp)
                     )
                 }
             }
         }
     }

    @Composable
    fun CardDetallePokemon(id: Int) {
        val context = LocalContext.current
        // Obtenemos el ViewModel con la inyección de dependencia del repositorio
        val viewModel: PokeViewModel = viewModel(
            factory = PokeViewModelFactory(repository)
        )

        // Llamamos a la función para buscar los detalles del Pokémon con el ID proporcionado
        LaunchedEffect(id) {
            viewModel.fetchPokemon(id)
        }

        // Obtenemos el estado del Pokémon desde el ViewModel como un Flow
        val pokemonState by viewModel.pokemon.collectAsState()

        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            Card {
                // Comprobamos si el estado del Pokémon ya está cargado
                pokemonState?.let { pokemon ->
                    // Mostramos la imagen del sprite del Pokémon usando rememberAsyncImagePainter
                    Image(
                        painter = rememberAsyncImagePainter(pokemon.sprites.front_default),
                        contentDescription = "Sprite del Pokémon",
                        modifier = Modifier.size(230.dp)
                    )
                    // Mostramos el nombre del Pokémon
                    Text(text = "Nombre: ${pokemon.name}")
                    Text(text = "Order: ${pokemon.order}")
                    Text(text = "ID: ${pokemon.id}")
                    Text(text = "Altura: ${pokemon.height}")
                    Text(text = "Peso: ${pokemon.weight}")
                    Button(onClick = {
                        val intent = Intent(context, Detalle::class.java)
                        intent.putExtra("POKEMON_ID", id) // Enviar el ID del Pokémon
                        context.startActivity(intent) // Cambiar a la nueva actividad

                    }) {
                        Text("Ver Cadena Evolutiva")
                    }
                }
            }
        }
    }

}