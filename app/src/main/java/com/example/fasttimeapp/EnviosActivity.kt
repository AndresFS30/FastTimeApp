package com.example.fasttimeapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fasttimeapp.databinding.ActivityEnviosBinding

class EnviosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEnviosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnviosBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}