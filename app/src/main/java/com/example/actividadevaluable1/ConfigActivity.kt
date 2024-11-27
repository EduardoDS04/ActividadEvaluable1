package com.example.actividadevaluable1

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ConfigActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        // Referencias a los elementos del layout
        val spinnerTiempo = findViewById<Spinner>(R.id.spinner_tiempo) // Selector de tiempo entre tiradas
        val radioGroupModo = findViewById<RadioGroup>(R.id.radioGroupModo) // Grupo de opciones para modo de juego
        val checkboxSonidos = findViewById<CheckBox>(R.id.checkbox_sonidos) // Checkbox para activar/desactivar sonidos
        val editTextMensaje = findViewById<EditText>(R.id.editText_mensaje_chiste) // Campo para mensaje personalizado
        val toggleButtonChistes = findViewById<ToggleButton>(R.id.toggleButton_chistes) // Botón para activar/desactivar chistes
        val buttonGuardar = findViewById<Button>(R.id.button_guardar_config) // Botón para guardar configuración

        // Leer el estado guardado en SharedPreferences para inicializar los valores
        val sharedPreferences = getSharedPreferences("AppConfig", MODE_PRIVATE)
        val modoGuardado = sharedPreferences.getString("modo", "Estático") // Modo de juego guardado
        val tiempoGuardado = sharedPreferences.getString("tiempo", "1 segundo") // Tiempo entre tiradas guardado
        val sonidosGuardados = sharedPreferences.getBoolean("sonidosActivados", false) // Estado de sonidos guardado
        val mensajeGuardado = sharedPreferences.getString("mensajeChistes", "") // Mensaje personalizado guardado
        val chistesActivados = sharedPreferences.getBoolean("chistesActivados", true) // Estado de chistes guardado

        // Inicializar los controles con los valores guardados
        when (modoGuardado) {
            "Estático" -> radioGroupModo.check(R.id.radio_estatico)
            "Dinámico" -> radioGroupModo.check(R.id.radio_dinamico)
        }
        spinnerTiempo.setSelection(resources.getStringArray(R.array.tiempos_opciones).indexOf(tiempoGuardado))
        checkboxSonidos.isChecked = sonidosGuardados
        editTextMensaje.setText(mensajeGuardado)
        toggleButtonChistes.isChecked = chistesActivados

        // Evento al presionar el botón de guardar configuración
        buttonGuardar.setOnClickListener {
            // Obtener valores seleccionados o introducidos por el usuario
            val tiempoSeleccionado = spinnerTiempo.selectedItem.toString()
            val modoSeleccionado = when (radioGroupModo.checkedRadioButtonId) {
                R.id.radio_estatico -> "Estático"
                R.id.radio_dinamico -> "Dinámico"
                else -> "No seleccionado"
            }
            val sonidosActivados = checkboxSonidos.isChecked
            val mensajeChistes = editTextMensaje.text.toString()
            val toggleChistesEstado = toggleButtonChistes.isChecked

            // Guardar los valores en SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("modo", modoSeleccionado)
            editor.putString("tiempo", tiempoSeleccionado)
            editor.putBoolean("sonidosActivados", sonidosActivados)
            editor.putString("mensajeChistes", mensajeChistes)
            editor.putBoolean("chistesActivados", toggleChistesEstado)
            editor.apply()

            // Mostrar un mensaje de confirmación
            Toast.makeText(
                this,
                "Configuración guardada: Modo $modoSeleccionado, Tiempo $tiempoSeleccionado",
                Toast.LENGTH_LONG
            ).show()

            // Cerrar la actividad
            finish()
        }
    }
}
