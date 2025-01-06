package com.example.fasttimeapp

import Poko.Colaborador
import Poko.ColaboradorJsoneable
import Poko.Conductor
import Poko.Mensaje
import Utils.Constantes
import Utils.LoginUtils
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
        //cargarFotoColaborador(colaborador.fotoBase64.toString())
    }

    override fun onStart() {
        super.onStart()

        binding.btnGuardar.setOnClickListener {

            if(validarCampos()){
                if(fotoPerfilBytes != null){
                    //Toast.makeText(this@EditarPerfilActivity,fotoPerfilBytes.toString(),Toast.LENGTH_SHORT).show()
                    subirFotoPerfil(colaborador.idColaborador)
                }

                cargarDatosColaborador()
                //cargarDatosConductor()
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
        binding.etCurp.setText(colaborador.curp)
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

    fun validarCampos():Boolean{
        var esValidos = true

        if(binding.etApellidoPaterno.text.isEmpty() || (binding.etApellidoPaterno.text.length > 50) ){
            esValidos = false
            binding.etApellidoPaterno.setError("El campo es obligatorio y debe ser menor a 50 carácteres")
        }

        if(binding.etApellidoMaterno.text.isEmpty() || (binding.etApellidoMaterno.text.length > 50)){
            esValidos = false
            binding.etApellidoMaterno.setError("El campo es obligatorio y debe ser menor a 50 carácteres")
        }

        if(binding.etNombre.text.isEmpty() || (binding.etNombre.text.length > 50)){
            esValidos = false
            binding.etNombre.setError("El campo es obligatorio y debe ser menor a 50 carácteres")
        }

        if(binding.etPassword.text.isEmpty() || (binding.etPassword.text.length > 20)){
            esValidos = false
            binding.etPassword.setError("El campo es obligatorio y debe ser menor a 20 carácteres")
        }

        val password = binding.etPassword.text.toString()
        // Verificar que la contraseña sea válida
        if (password.length <= 8 ||
            !password.any { it.isDigit() } ||
            !password.any { "!@#$%^&*()_+-=,.<>?/".contains(it) }) {
            esValidos = false
            binding.etPassword.setError("La contraseña debe ser mayor a 8 carácteres, y debe contener un número y un caracter especial")
        }

        if(binding.etEmail.text.isEmpty() || (binding.etEmail.text.length > 45)){
            esValidos = false
            binding.etEmail.setError("El campo es obligatorio y debe ser menor a 45 carácteres")
        }

        if(!binding.etEmail.text.contains("@")){
            esValidos = false
            binding.etEmail.setError("Correo invalido")
        }

        if(binding.etNoLicencia.text.isEmpty() || (binding.etNoLicencia.text.length > 12)){
            esValidos = false
            binding.etNoLicencia.setError("El campo es obligatorio y debe ser menor a 12 carácteres")
        }

        if(binding.etCurp.text.isEmpty() || (binding.etCurp.text.length > 18)){
            esValidos = false
            binding.etCurp.setError("El campo es obligatorio y debe ser menor a 18 carácteres")
        }

        return esValidos
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
                binding.tvFotoSeleccionada.setText(imgURI.toString())
                fotoPerfilBytes = uriToByteArray(imgURI)
                if(fotoPerfilBytes != null){
                    Log.d("Imagen",fotoPerfilBytes.toString())
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
                        //obtenerFotoColaborador(colaborador.idColaborador)
                    }
                }else{
                    Log.e("API",e.message.toString())
                    Toast.makeText(this, "Error al guardar la información, intente después.", Toast.LENGTH_LONG).show()
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
            val colaboradorFoto = gson.fromJson(json,ColaboradorJsoneable::class.java)
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
    fun cargarDatosColaborador(){
        var colaboradorEnvio = Colaborador(
            colaborador.idColaborador,
            binding.etNombre.text.toString(),
            binding.etApellidoPaterno.text.toString(),
            binding.etApellidoMaterno.text.toString(),
            binding.etCurp.text.toString(),
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString(),
            colaborador.idRol,"",colaborador.fotografia,colaborador.noPersonal,binding.etNoLicencia.text.toString(),colaborador.fotoBase64)
        enviarDatosColaborador(colaboradorEnvio)
    }

    fun cargarDatosConductor() {
        val conductorEnvio = Conductor(
            colaborador.idColaborador,
            binding.etNoLicencia.text.toString()
        )

        enviarDatosConductor(conductorEnvio)
    }

    fun enviarDatosColaborador(parColaborador: Colaborador){
        val gson = Gson()
        val parametros = gson.toJson(parColaborador)
        //Toast.makeText(this, parametros, Toast.LENGTH_SHORT).show()
        Ion.with(this@EditarPerfilActivity)
            .load("PUT","${Constantes().URL_WS}colaborador/editarColaborador")
            .setHeader("Content-Type","application/json")
            .setStringBody(parametros)
            .asString()
            .setCallback { e, result ->
                if(e == null){
                    respuestaPeticion(result)
                }else{
                    Log.e("API",e.message.toString())
                    Toast.makeText(this@EditarPerfilActivity,"Error al guardar la información, intente después.",Toast.LENGTH_LONG).show()
                }
            }

    }

    fun enviarDatosConductor(parConductor: Conductor){
        val gson = Gson()
        val parametros = gson.toJson(parConductor)
        //Toast.makeText(this, parametros, Toast.LENGTH_SHORT).show()
        Ion.with(this@EditarPerfilActivity)
            .load("PUT","${Constantes().URL_WS}colaborador/actualizarConductor")
            .setHeader("Content-Type","application/json")
            .setStringBody(parametros)
            .asString()
            .setCallback { e, result ->
                if(e == null){
                    respuestaPeticion(result)
                }else{
                    Log.e("API",e.message.toString())
                    Toast.makeText(this@EditarPerfilActivity,"Error al guardar la información, intente después.",Toast.LENGTH_LONG).show()
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
                LoginUtils.verificarCredenciales(
                    this,colaborador.noPersonal.toString(),binding.etPassword.text.toString())
                //finish()
            }
        }catch(e: Exception){
            Toast.makeText(this,"Error al leer la respuesta de los servicios",Toast.LENGTH_LONG).show()
        }
    }

}