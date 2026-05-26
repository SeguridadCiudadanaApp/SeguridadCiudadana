package com.catedra.seguridadciudadana

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        findViewById<Button>(R.id.btnRegister).setOnClickListener{
            registerUSer()
        }

        /*
        * findViewById<Button>(R.id.btnLogin).setOnClickListener {
            loginUser()
            }
        * */
    }

    private fun validateFields(): Pair<String, String>? {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.error = "Ingresa un email válido"
            return null
        }

        if (password.length < 6){
            etPassword.error = "La contraseña debe tener al menos 6 caracteres"
            return null
        }

        return Pair(email, password)
    }

    private fun registerUSer(){
        val data = validateFields() ?: return

        auth.createUserWithEmailAndPassword(data.first, data.second)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
                    openHome()
                } else {
                    Toast.makeText(this, task.exception?.message ?: "Error al registrase", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart(){
        super.onStart()

        if(auth.currentUser != null){
            openHome()
        }
    }

    private fun openHome(){
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}