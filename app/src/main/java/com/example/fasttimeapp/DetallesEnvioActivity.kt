package com.example.fasttimeapp

import Poko.Colaborador
import Poko.Envio
import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
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
        configurarSpinnerArreglo()
    }

    private fun obtenerDatos(){
        val jsonColaborador = intent.getStringExtra("colaborador")
        if(jsonColaborador != null){
            val gson = Gson()
            colaborador = gson.fromJson(jsonColaborador, colaborador::class.java)
        }

        val jsonEnvio = intent.getStringExtra("envio")
        if(jsonEnvio != null){
            val gson = Gson()
            envio = gson.fromJson(jsonEnvio, Envio::class.java)
        }
    }

    fun configurarSpinnerArreglo(){
        val opcionesIntent = arrayOf("En tránsito", "Detenido","Entregado","Cancelado")
        val adapterArray = ArrayAdapter<String>(this, R.layout.simple_spinner_item, opcionesIntent)
        adapterArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spEstatus.adapter = adapterArray
    }
}