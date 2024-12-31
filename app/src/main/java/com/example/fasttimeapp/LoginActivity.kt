package com.example.fasttimeapp

import Poko.LoginColaborador
import Utils.Constantes
import Utils.LoginUtils
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fasttimeapp.databinding.ActivityLoginBinding
import com.google.gson.Gson
import com.koushikdutta.ion.Ion

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        binding.btnIngresar.setOnClickListener {
            val noPersonal = binding.etNoPersonal.text.toString()
            val password = binding.etPassword.text.toString()
            if(sonCamposValidos(noPersonal,password)){
                //verificarCredenciales(noPersonal,password)
                LoginUtils.verificarCredenciales(this,noPersonal,password)
            }
        }
    }

    /*fun verificarCredenciales(noPersonal : String, password : String){
        //Sólo se pone la primera vez
        Ion.getDefault(this@LoginActivity).conscryptMiddleware.enable(false)
        //Consumo de WS
        Ion.with(this@LoginActivity)
            .load("POST","${Constantes().URL_WS}login/colaborador")
            .setHeader("Content-Type","application/x-www-form-urlencoded")
            .setBodyParameter("NoPersonal",noPersonal)
            .setBodyParameter("Password",password)
            .asString()
            .setCallback { e, result ->
                if(e == null){
                    //Toast.makeText(this@LoginActivity,result,Toast.LENGTH_SHORT).show()
                    serializarInformacion(result)
                }else{
                    Toast.makeText(this@LoginActivity, "Error:"+e.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun serializarInformacion(json:String){
        val gson = Gson()
        //:: sirve para utilizar cosas de java
        val respuestaLoginColaborador = gson.fromJson(json, LoginColaborador::class.java)
        if(!respuestaLoginColaborador.error){
            val colaborador = respuestaLoginColaborador.colaborador
            if(colaborador!!.idRol == 3){
                val colaboradorJson = gson.toJson(respuestaLoginColaborador.colaborador)
                irPantallaPrincipal(colaboradorJson)
            }else{
                Toast.makeText(this,"No. personal y/o password incorrectos", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,respuestaLoginColaborador.mensaje, Toast.LENGTH_SHORT).show()
        }
    }*/

    fun sonCamposValidos(noPersonal: String, password : String) : Boolean{
        var camposValidos = true
        if(noPersonal.isEmpty()){
            camposValidos=false
            binding.etNoPersonal.setError("Campo obligatorio")
        }

        if(password.isEmpty()){
            camposValidos=false
            binding.etPassword.setError("Campo obligatorio")
        }

        return camposValidos
    }

    fun irPantallaPrincipal(colaborador: String){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("colaborador",colaborador)
        startActivity(intent)
    }
}