package com.example.fasttimeapp

import Poko.Colaborador
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fasttimeapp.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var colaborador : Colaborador
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        obtenerDatosColaborador()
    }

    override fun onStart() {
        super.onStart()

        binding.btnEditarPerfil.setOnClickListener {
            val gson = Gson()
            val colaboradorJson = gson.toJson(colaborador)
            irPantallaEditarPefil(colaboradorJson)
        }

        binding.btnEnvios.setOnClickListener {
            irPantallaEnvios()
        }
    }

    fun obtenerDatosColaborador(){
        val jsonColaborador = intent.getStringExtra("colaborador")
        if(jsonColaborador != null){
            val gson = Gson()
            colaborador = gson.fromJson(jsonColaborador,Colaborador::class.java)
        }
    }

    fun irPantallaEnvios(){
        val intent = Intent(this, EnviosActivity::class.java)
        //intent.putExtra("cliente",cliente)
        startActivity(intent)
    }

    fun irPantallaEditarPefil(colaborador: String){
        val intent = Intent(this, EditarPerfilActivity::class.java)
        intent.putExtra("colaborador",colaborador)
        startActivity(intent)
    }
}