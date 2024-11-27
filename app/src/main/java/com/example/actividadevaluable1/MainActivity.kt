package com.example.actividadevaluable1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageButton
import android.widget.Toast
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private val REQUEST_CALL_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Activa diseño de borde a borde
        setContentView(R.layout.activity_main)

        // Ajusta las barras del sistema para borde a borde
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // DadosActivity
        val buttonDados = findViewById<ImageButton>(R.id.button_dados)
        buttonDados.setOnClickListener {
            val intent = Intent(this, DadosActivity::class.java)
            startActivity(intent)
        }

        // ChistesActivity
        val buttonChistes = findViewById<ImageButton>(R.id.button_chistes)
        buttonChistes.setOnClickListener {
            val intent = Intent(this, ChistesActivity::class.java)
            startActivity(intent)
        }

        val buttonConfig = findViewById<Button>(R.id.button_config)
        buttonConfig.setOnClickListener {
            val intent = Intent(this, ConfigActivity::class.java)
            startActivity(intent)
        }



        // Configuración del botón para realizar una llamada
        val buttonLlamada = findViewById<ImageButton>(R.id.button_llamada)
        buttonLlamada.setOnClickListener {
            checkCallPermissionAndProceed() // Verifica permisos y procede
        }

        // Configuración del botón para abrir YouTube en Chrome
        val buttonUrl = findViewById<ImageButton>(R.id.button_url)
        buttonUrl.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"))
            intent.setPackage("com.android.chrome") // Intenta abrir con Chrome

            // Verifica si Chrome está instalado
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                // Si Chrome no está instalado, abre con el navegador predeterminado
                val defaultIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"))
                startActivity(defaultIntent)
                Toast.makeText(this, "Chrome no está instalado, abriendo con otro navegador", Toast.LENGTH_SHORT).show()
            }
        }

        // Configuración del botón para enviar un correo
        val buttonCorreo = findViewById<ImageButton>(R.id.button_correo)
        buttonCorreo.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:Ediasor737@g.educaand.es") // Establece el email
                putExtra(Intent.EXTRA_SUBJECT, "Examen práctico")
                putExtra(Intent.EXTRA_TEXT, "Hola, es una prueba")
            }

            // Verifica si hay aplicaciones de correo disponibles
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "No hay aplicaciones de correo disponibles", Toast.LENGTH_SHORT).show()
            }
        }

        // Configuración del botón para establecer una alarma
        val buttonAlarma = findViewById<ImageButton>(R.id.button_alarma)
        buttonAlarma.setOnClickListener {
            configurarAlarma(2, "Alarma personalizada") // Establece la alarma para 2 minutos
        }
    }

    // Función para configurar la alarma
    private fun configurarAlarma(minutos: Int, mensaje: String) {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MINUTE, minutos) // Agrega minutos a la hora actual
        }
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_HOUR, hour)
            putExtra(AlarmClock.EXTRA_MINUTES, minute)
            putExtra(AlarmClock.EXTRA_MESSAGE, mensaje)
        }

        // Verifica si se puede configurar la alarma
        if (alarmIntent.resolveActivity(packageManager) != null) {
            startActivity(alarmIntent)
        } else {
            Toast.makeText(this, "No se puede configurar la alarma.", Toast.LENGTH_SHORT).show()
        }
    }

    // Verifica permisos y realiza la llamada si se han concedido permisos
    private fun checkCallPermissionAndProceed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL_PERMISSION)
        } else {
            makeCall()
        }
    }

    // Función para iniciar la actividad de llamada (SegundoActivity)
    private fun makeCall() {
        val intent = Intent(this, SegundoActivity::class.java) // Inicia SegundoActivity para hacer la llamada
        startActivity(intent)
    }

    //La respuesta de permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall() // Realiza la llamada si el permiso fue concedido
            } else {
                Toast.makeText(this, "Permiso de llamada denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }



}
