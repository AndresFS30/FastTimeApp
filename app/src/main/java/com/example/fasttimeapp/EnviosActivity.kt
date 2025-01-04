package com.example.fasttimeapp

import Adaptadores.EnvioAdapter
import Interfaces.ListenerRecyclerEnvios
import Poko.Colaborador
import Poko.Envio
import Utils.Constantes
import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fasttimeapp.databinding.ActivityEnviosBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.koushikdutta.ion.Ion

class EnviosActivity : AppCompatActivity(), ListenerRecyclerEnvios {
    private lateinit var binding: ActivityEnviosBinding
    private lateinit var colaborador : Colaborador
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnviosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        obtenerDatosColaborador()
        descargarEnvios()
        configurarRecyclerEnvios()
    }

    private fun obtenerDatosColaborador(){
        val jsonColaborador = intent.getStringExtra("colaborador")
        if(jsonColaborador != null){
            val gson = Gson()
            colaborador = gson.fromJson(jsonColaborador,Colaborador::class.java)
        }
    }

    private fun descargarEnvios(){
        //Determina si lleva algún cifrado para la conexión. Sólo se utiliza la primera vez
        Ion.getDefault(this).conscryptMiddleware.enable(false)
        //Descargar información
        Ion.with(this).load("GET","${Constantes().URL_WS}/envio/obtenerEnvioNoLicencia/${colaborador.numeroLicencia}")
            //"e" es el Error. "Result" es el resultado
            .asString().setCallback { e, result ->
                if(e == null){
                    Log.e("API",result)
                    mostrarInfoEnvios(result)
                }else{
                    Log.e("API",e.message.toString())
                    Toast.makeText(this,e.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun mostrarInfoEnvios(envios:String){
        val gson = Gson()
        val tipoArreglo = object : TypeToken<List<Envio>>(){}.type
        val listaEnvios : List<Envio> = gson.fromJson(envios,tipoArreglo)
        mostrarInfoEnviosRecycler(listaEnvios)
    }

    private fun mostrarDetallesEnvio(envio: String, colaborador: String){
        val intent = Intent(this, DetallesEnvioActivity::class.java)
        intent.putExtra("envio",envio)
        intent.putExtra("colaborador",colaborador)
        startActivity(intent)
    }

    //Recycler
    private fun mostrarInfoEnviosRecycler(envios : List<Envio>){
        binding.recycleEnvios.visibility = View.VISIBLE
        binding.recycleEnvios.adapter = EnvioAdapter(envios,this)
    }
    private fun configurarRecyclerEnvios(){
        binding.recycleEnvios.layoutManager = LinearLayoutManager(this)
        binding.recycleEnvios.setHasFixedSize(true)
    }

    override fun clicContenido(envio: Envio, posicion: Int) {
        val gson = Gson()
        val envioJson = gson.toJson(envio)
        val colaboradorJson = gson.toJson(colaborador)
        mostrarDetallesEnvio(envioJson, colaboradorJson)
    }
}