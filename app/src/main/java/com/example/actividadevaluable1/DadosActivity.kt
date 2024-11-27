package com.example.actividadevaluable1

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class DadosActivity : AppCompatActivity() {
    // Variables para las imágenes de los dados y el texto del resultado
    private lateinit var dado1: ImageView
    private lateinit var dado2: ImageView
    private lateinit var dado3: ImageView
    private lateinit var resultadoTextView: TextView

    // Configuraciones de tiempo y modo
    private var tiempoEntreTiradas: Long = 1000
    private var modoEstatico = true
    private var sonidosActivados = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dados)

        // Inicializar vistas
        dado1 = findViewById(R.id.imageView_dado1)
        dado2 = findViewById(R.id.imageView_dado2)
        dado3 = findViewById(R.id.imageView_dado3)
        resultadoTextView = findViewById(R.id.textView_resultado)
        val buttonLanzar = findViewById<Button>(R.id.button_lanzar)
        val buttonVolver = findViewById<Button>(R.id.button_volver)

        // Leer configuraciones desde SharedPreferences
        val sharedPreferences = getSharedPreferences("AppConfig", MODE_PRIVATE)
        val tiempoSeleccionado = sharedPreferences.getString("tiempo", "1 segundo")
        val modoSeleccionado = sharedPreferences.getString("modo", "Estático")
        sonidosActivados = sharedPreferences.getBoolean("sonidosActivados", false)

        // Convertir tiempo seleccionado a milisegundos
        tiempoEntreTiradas = when (tiempoSeleccionado) {
            "1 segundo" -> 1000
            "2 segundos" -> 2000
            "3 segundos" -> 3000
            else -> 1000
        }

        // Configurar el modo de juego
        modoEstatico = (modoSeleccionado == "Estático")

        // Botón para lanzar los dados
        buttonLanzar.setOnClickListener {
            if (modoEstatico) {
                lanzarDadosEstaticoConTiempo()
            } else {
                lanzarDadosDinamico()
            }
        }

        // Botón para volver al MainActivity
        buttonVolver.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Lógica para lanzar los dados en modo estático
    private fun lanzarDadosEstaticoConTiempo() {
        Handler(Looper.getMainLooper()).postDelayed({
            reproducirSonido() // Reproducir sonido si está activado
            dado1.setImageResource(R.drawable.dado1) // Imagen fija para dado 1
            dado2.setImageResource(R.drawable.dado2) // Imagen fija para dado 2
            dado3.setImageResource(R.drawable.dado3) // Imagen fija para dado 3
            val sum = Random.nextInt(3, 19) // Generar resultado aleatorio
            resultadoTextView.text = "Resultado: $sum" // Mostrar resultado
        }, tiempoEntreTiradas)
    }

    // Lógica para lanzar los dados en modo dinámico
    private fun lanzarDadosDinamico() {
        Handler(Looper.getMainLooper()).postDelayed({
            reproducirSonido() // Reproducir sonido si está activado
            val resultados = Array(3) { Random.nextInt(1, 7) } // Generar valores aleatorios para los dados
            val dados = arrayOf(dado1, dado2, dado3)

            // Actualizar las imágenes de los dados según los valores generados
            resultados.forEachIndexed { index, valor ->
                dados[index].setImageResource(obtenerImagenDado(valor))
            }

            val sum = resultados.sum() // Calcular la suma de los valores
            resultadoTextView.text = "Resultado: $sum" // Mostrar resultado
        }, tiempoEntreTiradas)
    }

    // Reproducir sonido al lanzar los dados
    private fun reproducirSonido() {
        if (sonidosActivados) {
            try {
                val mediaPlayer = MediaPlayer.create(this, R.raw.dice_roll)
                mediaPlayer.setOnCompletionListener { it.release() } // Liberar recurso al terminar
                mediaPlayer.start()
            } catch (e: Exception) {
                e.printStackTrace() // Manejar errores si ocurren
            }
        }
    }

    // Obtener la imagen correspondiente al valor del dado
    private fun obtenerImagenDado(valor: Int): Int {
        return when (valor) {
            1 -> R.drawable.dado1
            2 -> R.drawable.dado2
            3 -> R.drawable.dado3
            4 -> R.drawable.dado4
            5 -> R.drawable.dado5
            6 -> R.drawable.dado6
            else -> R.drawable.dado1
        }
    }
}
