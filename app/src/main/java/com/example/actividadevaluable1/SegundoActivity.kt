package com.example.actividadevaluable1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class SegundoActivity : AppCompatActivity() {
    private val REQUEST_CALL_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.segundo_activity)

        // Recuperamos el número guardado en SharedPreferences
        val sharedPref = getSharedPreferences("MisPreferencias", MODE_PRIVATE)
        val savedPhoneNumber = sharedPref.getString("numeroTelefono", "") // Obtenemos el número guardado

        // Campo de texto para ingresar el número
        val editTextPhone = findViewById<EditText>(R.id.edit_text_phone)
        editTextPhone.setText(savedPhoneNumber) // Ponemos el número recuperado en el campo

        // Botón para realizar la llamada
        val buttonCall = findViewById<ImageButton>(R.id.button_call)
        buttonCall.setOnClickListener {
            val phoneNumber = editTextPhone.text.toString()
            if (phoneNumber.isNotEmpty()) {
                // Verificar si el permiso de llamada ya ha sido concedido
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    makeCall(phoneNumber)
                } else {
                    // Solicitar permiso si no ha sido concedido
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL_PERMISSION)
                }

                // Guardamos el número en SharedPreferences
                val editor = sharedPref.edit()
                editor.putString("numeroTelefono", phoneNumber) // Guardamos el número
                editor.apply()
            } else {
                Toast.makeText(this, "Por favor ingrese un número de teléfono", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para volver a MainActivity
        val buttonBack = findViewById<ImageButton>(R.id.boton_volver)
        buttonBack.setOnClickListener {
            // Volver a MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Finalizar SegundoActivity para evitar que vuelva al presionar atrás
        }
    }

    // Función para realizar la llamada
    private fun makeCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
        startActivity(intent)
    }

    // Resultado de la solicitud de permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL_PERMISSION) {
            val phoneNumber = findViewById<EditText>(R.id.edit_text_phone).text.toString()
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall(phoneNumber)
            } else {
                Toast.makeText(this, "Permiso de llamada denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
