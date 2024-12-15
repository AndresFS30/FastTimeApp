package com.example.fasttimeapp

import Poko.Colaborador
import Poko.Mensaje
import Utils.Constantes
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fasttimeapp.databinding.ActivityEditarPerfilBinding
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.lang.Exception

class EditarPerfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarPerfilBinding
    private lateinit var colaborador : Colaborador
    private var fotoPerfilBytes : ByteArray ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        obtenerDatosColaborador()
        llenarCamposColaborador()
    }

    override fun onStart() {
        super.onStart()

        binding.btnGuardar.setOnClickListener {
            if(fotoPerfilBytes != null){
                Toast.makeText(this@EditarPerfilActivity,fotoPerfilBytes.toString(),Toast.LENGTH_SHORT).show()
                subirFotoPerfil(colaborador.idColaborador)
                cargarDatosEnvio()
            }
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }

        binding.btnSubirFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            selectorFotoPerfil.launch(intent)
        }
    }

    fun llenarCamposColaborador(){
        binding.etPassword.setText(colaborador.password)
        binding.etCurp.setText(colaborador.CURP)
        binding.etEmail.setText(colaborador.correo)
        binding.etNombre.setText(colaborador.nombre)
        binding.etApellidoPaterno.setText(colaborador.apellidoPaterno)
        binding.etApellidoMaterno.setText(colaborador.apellidoMaterno)
        binding.etNoLicencia.setText(colaborador.numeroLicencia)
    }

    fun obtenerDatosColaborador(){
        val jsonColaborador = intent.getStringExtra("colaborador")
        if(jsonColaborador != null){
            val gson = Gson()
            colaborador = gson.fromJson(jsonColaborador, Colaborador::class.java)
        }
    }

    //Sección de fotos
    private fun uriToByteArray(uri: Uri): ByteArray? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            byteArrayOutputStream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private val selectorFotoPerfil = this.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result : ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            val data = result.data
            val imgURI = data?.data
            if(imgURI != null){
                fotoPerfilBytes = uriToByteArray(imgURI)
                if(fotoPerfilBytes != null){
                    Toast.makeText(this@EditarPerfilActivity,fotoPerfilBytes.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun subirFotoPerfil(idColaborador: Int){
        Ion.with(this)
            .load("PUT","${Constantes().URL_WS}colaborador/subirFoto/${idColaborador}")
            .setByteArrayBody(fotoPerfilBytes)
            .asString()
            .setCallback{e, result ->
                if(e == null){
                    val gson = Gson()
                    val mensaje = gson.fromJson(result, Mensaje::class.java)
                    Toast.makeText(this, mensaje.mensaje, Toast.LENGTH_LONG).show()
                    if(!mensaje.error){
                        obtenerFotoColaborador(colaborador.idColaborador)
                    }
                }else{
                    Toast.makeText(this, "Error:"+e.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    fun obtenerFotoColaborador(idColaborador:Int){
        Ion.with(this).load("${Constantes().URL_WS}colaborador/obtenerFoto/${idColaborador}").
        asString().setCallback{e, result ->
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
            val colaboradorFoto = gson.fromJson(json,Colaborador::class.java)
            if(colaboradorFoto.fotoBase64 != null){
                try {
                    val imgBytes = Base64.decode(colaboradorFoto.fotoBase64, Base64.DEFAULT)
                    val imgBitmap = BitmapFactory.decodeByteArray(imgBytes,0,imgBytes.size)
                    binding.ivPerfil.setImageBitmap(imgBitmap)
                }catch (e : Exception){
                    Toast.makeText(this, "Error:"+e.message, Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this, "No cuentas con foto de perfil", Toast.LENGTH_LONG).show()
            }
        }
    }

    //Subir información
    fun cargarDatosEnvio(){
        var colaboradorEnvio = Colaborador(
            colaborador.idColaborador,
            binding.etNombre.text.toString(),
            binding.etApellidoPaterno.text.toString(),
            binding.etApellidoMaterno.text.toString(),
            binding.etCurp.text.toString(),
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString(),
            0,"","",colaborador.noPersonal,"","")

        /*
        val idColaborador:Int,
        val nombre:String,
        val apellidoPaterno:String,
        val apellidoMaterno:String,
        val CURP:String,
        val correo:String,
        val password:String,
        val idRol: Int,
        val rol:String,
        val fotografia: String,
        val noPersonal:String,
        val numeroLicencia:String,
        val fotoBase64: String*/

        enviarDatos(colaboradorEnvio)
    }

    fun cargarDatosConductor(){

    }

    fun enviarDatos(parColaborador: Colaborador){
        val gson = Gson()
        val parametros = gson.toJson(parColaborador)
        Toast.makeText(this, parametros, Toast.LENGTH_SHORT).show()
        Ion.with(this@EditarPerfilActivity)
            .load("PUT","${Constantes().URL_WS}colaborador/editarColaborador")
            .setHeader("Content-Type","application/json")
            .setStringBody(parametros)
            .asString()
            .setCallback { e, result ->
                if(e == null){
                    respuestaPeticion(result)
                }else{
                    Toast.makeText(this@EditarPerfilActivity,e.message,Toast.LENGTH_LONG).show()
                }
            }

    }

    fun respuestaPeticion(resultado : String){
        try {
            val gson = Gson()
            val mensaje = gson.fromJson(resultado, Mensaje::class.java)
            Toast.makeText(this,mensaje.mensaje,Toast.LENGTH_LONG).show()
            if(!mensaje.error){
                finish()
            }
        }catch(e: Exception){
            Toast.makeText(this,"Error al leer la respuesta del servicios",Toast.LENGTH_LONG).show()
        }
    }

}