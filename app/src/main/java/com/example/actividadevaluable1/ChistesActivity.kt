package com.example.actividadevaluable1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class ChistesActivity : AppCompatActivity() {

    // Inicializar variables para TextToSpeech y ProgressBar
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var progressBar: ProgressBar

    // Mensaje personalizado para los chistes
    private var mensajePersonalizado: String = ""

    // Estado de los chistes activados o desactivados
    private var chistesActivados: Boolean = true

    // Lista de chistes
    private val chistes = listOf(
        "El secreto para que tu enamorado caiga rendido a tus pies es regalarle flores. Flores que huelan a cloroformo.",
        "Quiero morir mientras duermo, como mi abuelo. No gritando, como los pasajeros de su avión.",
        "Doctor, ¿qué me dijo, capricornio o piscis? Cáncer, señora, cáncer.",
        "¿De qué murió Bob Marley? De un porrazo.",
        "¿Por qué el libro de matemáticas estaba triste? ¡Porque tenía muchos problemas!",
        "¿Cuál es la diferencia entre una pizza y un niño? ¡No se comparte una pizza entera!",
        "¿Qué le dice un jardinero a otro? ¡Disfrutemos mientras podamos!",
        "¿Por qué los esqueletos no pelean? ¡Porque no tienen agallas!",
        "¿Por qué el tomate no tomó el autobús? ¡Porque se quedó hecho puré!",
        "¿Qué le dice un semáforo a otro? ¡No me mires, me estoy cambiando!"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chistes)

        // Vincular vistas con sus IDs
        progressBar = findViewById(R.id.progressBar)
        val buttonChiste = findViewById<Button>(R.id.button_chiste)
        val buttonVolver = findViewById<Button>(R.id.button_volver)

        // Leer configuración de chistes desde SharedPreferences
        val sharedPreferences = getSharedPreferences("AppConfig", MODE_PRIVATE)
        mensajePersonalizado = sharedPreferences.getString("mensajeChistes", "") ?: ""
        chistesActivados = sharedPreferences.getBoolean("chistesActivados", true)

        // Configurar TextToSpeech
        configureTextToSpeech()

        // Evento para el botón de escuchar chiste
        buttonChiste.setOnClickListener {
            if (!chistesActivados) {
                // Mostrar mensaje si los chistes están desactivados
                Toast.makeText(this, "Los chistes están desactivados.", Toast.LENGTH_SHORT).show()
            } else if (progressBar.visibility == View.VISIBLE) {
                // Prevenir múltiples clics mientras se muestra un chiste
                Toast.makeText(this, "Espera a que termine el chiste.", Toast.LENGTH_SHORT).show()
            } else {
                // Mostrar un chiste aleatorio
                mostrarChisteAleatorio()
            }
        }

        // Evento para el botón de volver al menú principal
        buttonVolver.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Finalizar la actividad actual
        }
    }

    // Configurar TextToSpeech para sintetizar voz en español
    private fun configureTextToSpeech() {
        textToSpeech = TextToSpeech(applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale("es", "ES"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TextToSpeech", "Idioma no soportado")
                    Toast.makeText(this, "El idioma no está soportado en este dispositivo.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.e("TextToSpeech", "Error al inicializar TextToSpeech")
            }
        }
    }

    // Mostrar un chiste aleatorio con ProgressBar activo
    private fun mostrarChisteAleatorio() {
        progressBar.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            val chisteAleatorio = chistes.random()
            val chisteCompleto = if (mensajePersonalizado.isNotBlank()) {
                "$mensajePersonalizado $chisteAleatorio"
            } else {
                chisteAleatorio
            }

            if (::textToSpeech.isInitialized) {
                textToSpeech.speak(chisteCompleto, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                Toast.makeText(this, "Error al inicializar el sintetizador de voz.", Toast.LENGTH_SHORT).show()
            }
            progressBar.visibility = View.GONE // Ocultar ProgressBar después del chiste
        }, 2000) // Tiempo para simular carga del chiste
    }

    // Limpiar recursos de TextToSpeech al cerrar la actividad
    override fun onDestroy() {
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}
