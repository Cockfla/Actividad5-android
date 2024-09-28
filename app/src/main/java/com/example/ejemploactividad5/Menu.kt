package com.example.ejemploactividad5

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import com.example.ejemploactividad5.Detalle


class Menu : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                var contexto = LocalContext.current
                Botonera(contexto)
            }
        }
    }

    companion object {
        @Composable
        fun Botonera(contexto: Context) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Row {
                        Button(onClick = {
                            var buscadorIntent = Intent(contexto, Buscador()::class.java)
                            contexto.startActivity(buscadorIntent)

                        }) {
                            Text(text = "Buscador")
                        }

                    }
                    Row {
                        Button(onClick = {
                            var vistaEvolucion = Intent(contexto, Evoluciones()::class.java)
                            contexto.startActivity(vistaEvolucion)
                        }) {
                            Text(text = "Evoluciones")
                        }

                    }
                    Row {
                        Button(onClick = {
                            var vistaLista = Intent(contexto, Lista()::class.java)
                            contexto.startActivity(vistaLista)
                        }) {
                            Text(text = "Lista")
                        }

                    }
                }

            }

        }
    }
}






