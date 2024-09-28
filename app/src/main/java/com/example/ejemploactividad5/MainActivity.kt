package com.example.ejemploactividad5

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.commit
import com.example.ejemploactividad5.ui.theme.Ejemploactividad5Theme


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejemploactividad5Theme {
                AndroidView(factory = { contextoBotonera ->
                    FrameLayout(contextoBotonera).apply {
                        id = android.view.View.generateViewId()
                    }
                }) { frameLayout ->
                    supportFragmentManager.commit {
                        replace(frameLayout.id, Menu())
                    }

                }
            }
        }
    }
}


