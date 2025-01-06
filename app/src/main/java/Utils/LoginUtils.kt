package Utils

import Poko.*
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.fasttimeapp.MainActivity
import com.google.gson.Gson
import com.koushikdutta.ion.Ion

object LoginUtils {

     fun verificarCredenciales(context: Context, noPersonal : String, password : String){
        //Sólo se pone la primera vez
        Ion.getDefault(context).conscryptMiddleware.enable(false)
        //Consumo de WS
        Ion.with(context)
            .load("POST","${Constantes().URL_WS}login/colaborador")
            .setHeader("Content-Type","application/x-www-form-urlencoded")
            .setBodyParameter("noPersonal",noPersonal)
            .setBodyParameter("password",password)
            .asString()
            .setCallback { e, result ->
                if(e == null){
                    //Toast.makeText(context,result,Toast.LENGTH_SHORT).show()
                    serializarInformacion(context,result)
                }else{
                    Log.e("API",e.message.toString())
                    Toast.makeText(context, "Error al iniciar sesión, intente después.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun serializarInformacion(context: Context, json:String){
        val gson = Gson()
        //:: sirve para utilizar cosas de java
        val respuestaLoginColaborador = gson.fromJson(json, LoginColaborador::class.java)
        if(!respuestaLoginColaborador.error){
            val colaborador = respuestaLoginColaborador.colaborador
            if(colaborador!!.idRol == 3){
                val colaboradorJson = gson.toJson(respuestaLoginColaborador.colaborador)
                irPantallaPrincipal(context,colaboradorJson)
            }else{
                Toast.makeText(context,"No. personal y/o password incorrectos", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(context,respuestaLoginColaborador.mensaje, Toast.LENGTH_SHORT).show()
        }
    }

    fun irPantallaPrincipal(context: Context,colaborador: String){
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("colaborador",colaborador)
        context.startActivity(intent)
    }
}