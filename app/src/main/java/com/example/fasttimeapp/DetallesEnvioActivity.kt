package com.example.fasttimeapp

import Poko.Colaborador
import Poko.Envio
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fasttimeapp.databinding.ActivityDetallesEnvioBinding
import com.google.gson.Gson

class DetallesEnvioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetallesEnvioBinding
    private lateinit var colaborador : Colaborador
    private lateinit var envio: Envio
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallesEnvioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        obtenerDatos()
    }

    private fun obtenerDatos(){
        val jsonColaborador = intent.getStringExtra("colaborador")
        if(jsonColaborador != null){
            val gson = Gson()
            colaborador = gson.fromJson(jsonColaborador, Colaborador::class.java)
        }

        val jsonEnvio = intent.getStringExtra("envio")
        if(jsonEnvio != null){
            val gson = Gson()
            envio = gson.fromJson(jsonEnvio, Envio::class.java)
        }
    }
}