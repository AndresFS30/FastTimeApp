package com.example.fasttimeapp

import Poko.Colaborador
import Poko.Conductor
import Poko.Envio
import Poko.Mensaje
import Poko.StatusEnvio
import Utils.Constantes
import Utils.LoginUtils
import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fasttimeapp.databinding.ActivityDetallesEnvioBinding
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

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
        cargarStatus()

        binding.btnGuardar.setOnClickListener {
            if(validarMotivo()){
                //Toast.makeText(this,"Cargando datos",Toast.LENGTH_SHORT).show()
                cambiarStatus()
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
        binding.tvNoGuia.setText(envio.numeroGuia)

        binding.tvDireccionOrigen.setText(envio.origen)
        binding.tvDireccionDestino.setText(envio.destino)
        binding.tvCliente.setText(envio.cliente)
        binding.tvContactoCliente.setText("${envio.correo} \n ${envio.telefono}")
        binding.tvContenido.setText(envio.contenidos)
    }

    fun cargarStatus(){
        val estatus = envio.status

        when (estatus){
            "En tránsito" ->{
                binding.spEstatus.setSelection(0)
            }

            "Detenido" ->{
                binding.spEstatus.setSelection(1)
            }

            "Entregado" ->{
                binding.spEstatus.setSelection(2)
            }

            "Cancelado" ->{
                binding.spEstatus.setSelection(3)
            }
        }
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

        if(binding.etMotivo.text.length > 100){
            binding.etMotivo.setError("El motivo no debe ser mayor a 100 carácteres")
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

    fun cambiarStatus(){
        var statusEnvio = StatusEnvio(
            envio.idEnvio,
            envio.status,
            binding.etMotivo.text.toString(),obtenerFechaHoraActual(),colaborador.idColaborador
        )
        enviarCambioStatus(statusEnvio)
    }

    fun enviarCambioStatus(statusEnvio: StatusEnvio) {
        val gson = Gson()
        val parametros = gson.toJson(statusEnvio)
        //Toast.makeText(this, parametros, Toast.LENGTH_SHORT).show()
        Ion.with(this)
            .load("POST", "${Constantes().URL_WS}envio/cambiarEstatus")
            .setHeader("Content-Type", "application/json")
            .setStringBody(parametros)
            .asString()
            .setCallback { e, result ->
                if (e == null) {
                    respuestaPeticion(result)
                } else {
                    Log.e("API",e.message.toString())
                    Toast.makeText(this, "Error al guardar la información, intente después.", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun respuestaPeticion(resultado : String){
        try {
            val gson = Gson()
            val mensaje = gson.fromJson(resultado, Mensaje::class.java)
            Toast.makeText(this,mensaje.mensaje,Toast.LENGTH_LONG).show()
            Log.e ("API Response",mensaje.mensaje)
            if(!mensaje.error){
                Toast.makeText(this,"Status cambiado con éxito.",Toast.LENGTH_LONG).show()
                finish()
            }
        }catch(e: Exception){
            Toast.makeText(this,"Error al leer la respuesta de los servicios",Toast.LENGTH_LONG).show()
        }
    }

    //Obtener la hora
    fun obtenerFechaHoraActual(): String {
        val formato = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) // Formato deseado
        return formato.format(Calendar.getInstance().time) // Devuelve la fecha y hora actual
    }
}