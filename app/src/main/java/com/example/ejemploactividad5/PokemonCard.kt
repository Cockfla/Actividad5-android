import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.ejemploactividad5.PokemonResponse


@Composable
fun PokemonCard(pokemon: PokemonResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(), // Asegura que ocupe todo el ancho
            horizontalAlignment = Alignment.CenterHorizontally, // Centra el contenido horizontalmente
            verticalArrangement = Arrangement.Center // Opción para alinear elementos verticalmente si hay espacio extra
        ) {
            Image(
                painter = rememberAsyncImagePainter(pokemon.sprites.front_default),
                contentDescription = "Sprite del Pokémon",
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally) // Alinea la imagen al centro horizontalmente
            )
            BasicText(
                text = "Nombre: ${pokemon.name}",
                modifier = Modifier.align(Alignment.CenterHorizontally) // Alinea el texto al centro horizontalmente
            )
            BasicText(
                text = "ID: ${pokemon.id}",
                modifier = Modifier.align(Alignment.CenterHorizontally) // Alinea el texto al centro horizontalmente
            )
            BasicText(text = "Versiones del juego:")
            pokemon.game_indices.forEach { gameIndex ->
                BasicText(text = "- ${gameIndex.version.name}")
            }
        }
    }
}
