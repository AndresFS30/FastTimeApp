package com.example.fasttimeapp

import Poko.Colaborador
import Poko.Envio
import android.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fasttimeapp.databinding.ActivityDetallesEnvioBinding
import com.google.gson.Gson

class DetallesEnvioActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityDetallesEnvioBinding
    private lateinit var colaborador : Colaborador
    private lateinit var envio: Envio
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallesEnvioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        obtenerDatos()
        cargarDatosEnvio()
        configurarSpinnerArreglo()

        binding.btnGuardar.setOnClickListener {
            if(validarMotivo()){
                Toast.makeText(this,"Cargando datos",Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }

        binding.spEstatus.onItemSelectedListener = this
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

    fun configurarSpinnerArreglo(){
        val opcionesIntent = arrayOf("En tránsito", "Detenido","Entregado","Cancelado")
        val adapterArray = ArrayAdapter<String>(this, R.layout.simple_spinner_item, opcionesIntent)
        adapterArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spEstatus.adapter = adapterArray
    }

    fun cargarDatosEnvio(){
        binding.tvDireccionOrigen.setText(envio.origen)
        binding.tvDireccionDestino.setText(envio.destino)
    }

    //Cambiar estatus
    fun validarMotivo():Boolean{
        var esValido = true

        if(envio.status.equals("Detenido") || envio.status.equals("Cancelado")  ){
           if(binding.etMotivo.text.isEmpty()){
               esValido = false
               binding.etMotivo.setError("Campo obligatorio si el status es Detenido o Cancelado")
           }
        }
        return esValido
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        Log.i("Seleccion intent:","Posicion seleccionada ${p2}")
        //tipoIntent = p2
        cargarOpcionesIntent(p2)
    }

    fun cargarOpcionesIntent(opcion:Int){
        when (opcion){
            0 -> {
                envio.status = "En tránsito"
                Log.i("Seleccion intent:","En tránsito")
            }

            1 -> {
                envio.status = "Detenido"
                Log.i("Seleccion intent:","Detenido")
            }

            2 -> {
                envio.status = "Entregado"
                Log.i("Seleccion intent:","Entregado")
            }

            3 -> {
                envio.status = "Cancelado"
                Log.i("Seleccion intent:","Cancelado")
            }
        }

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}