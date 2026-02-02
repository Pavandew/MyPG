package com.example.searchroom.hostScreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.searchroom.R
import com.example.searchroom.databinding.ActivityHostBinding

class HostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}