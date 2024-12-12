package com.example.fasttimeapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fasttimeapp.databinding.ActivityDetallesEnvioBinding

class DetallesEnvioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetallesEnvioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallesEnvioBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}