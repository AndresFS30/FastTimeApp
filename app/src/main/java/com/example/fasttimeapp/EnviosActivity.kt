package com.example.fasttimeapp

import Poko.Colaborador
import Poko.Envio
import Utils.Constantes
import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fasttimeapp.databinding.ActivityEnviosBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.koushikdutta.ion.Ion

class EnviosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEnviosBinding
    private lateinit var colaborador : Colaborador
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnviosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        obtenerDatosColaborador()
        descargarEnvios()
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
        Ion.with(this).load("GET","\"${Constantes().URL_WS}/envio/obtenerEnvioNoLicencia/${colaborador.numeroLicencia}")
            //"e" es el Error. "Result" es el resultado
            .asString().setCallback { e, result ->
                if(e == null){
                    Log.e("API",result)
                    mostrarInfoEnvios(result)
                    /*if(result.length > 0){
                        mostrarInfoAlbumes(parseAlbums(result))
                    }*/
                }else{
                    Log.e("API",e.message.toString())
                    Toast.makeText(this,e.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun mostrarInfoEnvios(envios:String){
        val gson = Gson()
        val tipoArreglo = object : TypeToken<List<Envio>>(){}.type
        val listaEnvios = gson.fromJson<List<Envio>>(envios,tipoArreglo)
        val arrayAdapter = ArrayAdapter(this, R.layout.simple_list_item_1,
            listaEnvios)
        binding.listaEnvios.adapter = arrayAdapter
        //Al dar clic al envio, realiza esto
        binding.listaEnvios.setOnItemClickListener {adapterView, view, i , l ->
            Toast.makeText(this, "Envio Seleccionado: ${listaEnvios.get(i).numeroGuia}",
                Toast.LENGTH_LONG).show()

            val gson = Gson()
            val colaboradorJson = gson.toJson(colaborador)

            val envioJson = gson.toJson(listaEnvios.get(i))
            mostrarDetallesEnvio(envioJson, colaboradorJson)
        }
    }

    private fun mostrarDetallesEnvio(envio: String, colaborador: String){
        val intent = Intent(this, DetallesEnvioActivity::class.java)
        intent.putExtra("envio",envio)
        intent.putExtra("colaborador",colaborador)
        startActivity(intent)
    }
}