package Adaptadores

import Interfaces.ListenerRecyclerEnvios
import Poko.Envio
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fasttimeapp.R

class EnvioAdapter (val envios:List<Envio>, val listenerRecyclerEnvios: ListenerRecyclerEnvios) : RecyclerView.Adapter<EnvioAdapter.ViewHolderEnvio>(){
    class ViewHolderEnvio(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvNumeroGuia : TextView = itemView.findViewById(R.id.tv_numero_guia)
        val tvDestino : TextView = itemView.findViewById(R.id.tv_destino_envio)
        val tvEstatus : TextView = itemView.findViewById(R.id.tv_estatus_envio)

        val llEnvio : LinearLayout = itemView.findViewById(R.id.ll_envio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderEnvio {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_envios, parent, false)
        return EnvioAdapter.ViewHolderEnvio(itemView)
    }

    override fun getItemCount(): Int {
        return envios.size
    }

    override fun onBindViewHolder(holder: ViewHolderEnvio, position: Int) {
        val envio = envios.get(position)
        holder.tvNumeroGuia.text = envio.numeroGuia
        holder.tvDestino.text = envio.destino
        holder.tvEstatus.text = envio.status

        holder.llEnvio.setOnClickListener{
            listenerRecyclerEnvios.clicContenido(envio,position)
        }
    }
}