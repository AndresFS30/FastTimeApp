package com.example.fasttimeapp

import Poko.Colaborador
import Utils.Constantes
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fasttimeapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var colaborador : Colaborador
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        obtenerDatosColaborador()
        obtenerFotoColaborador(colaborador.idColaborador)
    }

    override fun onStart() {
        super.onStart()

        binding.btnEditarPerfil.setOnClickListener {
            val gson = Gson()
            val colaboradorJson = gson.toJson(colaborador)
            irPantallaEditarPefil(colaboradorJson)
        }

        binding.btnEnvios.setOnClickListener {
            val gson = Gson()
            val colaboradorJson = gson.toJson(colaborador)
            irPantallaEnvios(colaboradorJson)
        }
    }

    fun obtenerDatosColaborador(){
        val jsonColaborador = intent.getStringExtra("colaborador")
        if(jsonColaborador != null){
            val gson = Gson()
            colaborador = gson.fromJson(jsonColaborador,Colaborador::class.java)
            cargarDatosColaborador()
        }
    }

    fun cargarDatosColaborador(){
        binding.tvNombreCompleto.setText("${colaborador.nombre} ${colaborador.apellidoPaterno} ${colaborador.apellidoMaterno}")
        binding.tvCorreo.setText(colaborador.correo)
        binding.tvNoPersonal.setText(colaborador.noPersonal)
        binding.tvCurp.setText(colaborador.curp)
        binding.tvNoLicencia.setText(colaborador.numeroLicencia)
    }

    fun irPantallaEnvios(colaborador: String){
        val intent = Intent(this, EnviosActivity::class.java)
        intent.putExtra("colaborador",colaborador)
        startActivity(intent)
    }

    fun irPantallaEditarPefil(colaborador: String){
        val intent = Intent(this, EditarPerfilActivity::class.java)
        intent.putExtra("colaborador",colaborador)
        startActivity(intent)
    }

    //cargarFoto
    fun obtenerFotoColaborador(idColaborador:Int){
        Ion.with(this)
            .load("GET","${Constantes().URL_WS}colaborador/obtenerFoto/${idColaborador}")
            .asString().setCallback{e, result ->
            if(e == null){
                cargarFotoColaborador(result)
            }else{
                Toast.makeText(this, "Error:"+e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun cargarFotoColaborador(json:String){
        if(json.isNotEmpty()){
            val gson = Gson()
            val colaboradorFoto = gson.fromJson(json, colaborador::class.java)
            if(colaboradorFoto.fotoBase64 != null){
                try {
                    val imgBytes = Base64.decode(colaboradorFoto.fotoBase64, Base64.DEFAULT)
                    val imgBitmap = BitmapFactory.decodeByteArray(imgBytes,0,imgBytes.size)
                    //binding.ivPerfil.setImageBitmap(imgBitmap)
                    binding.ivPerfilGrande.setImageBitmap(imgBitmap)
                }catch (e : Exception){
                    Toast.makeText(this, "Error:"+e.message, Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this, "No cuentas con foto de perfil", Toast.LENGTH_LONG).show()
            }
        }
    }
}