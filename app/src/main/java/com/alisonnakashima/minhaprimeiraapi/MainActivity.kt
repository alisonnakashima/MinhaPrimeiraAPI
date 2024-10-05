package com.alisonnakashima.minhaprimeiraapi

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alisonnakashima.minhaprimeiraapi.adapter.ItemAdapter
import com.alisonnakashima.minhaprimeiraapi.databinding.ActivityMainBinding
import com.alisonnakashima.minhaprimeiraapi.model.Item
import com.alisonnakashima.minhaprimeiraapi.service.Result
import com.alisonnakashima.minhaprimeiraapi.service.RetrofitClient
import com.alisonnakashima.minhaprimeiraapi.service.safeApiCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
    }

    override fun onResume() {
        super.onResume()
        fetchItems()
    }

    private fun fetchItems() {
        // Alterando execução para IO thread
        CoroutineScope(Dispatchers.IO).launch {
            val result = safeApiCall { RetrofitClient.apiService.getItems() }

            // Alterando execução para Main thread
            withContext(Dispatchers.Main) {
                binding.swipeRefreshLayout.isRefreshing = false
                when (result) {
                    is Result.Error -> {
                        Toast.makeText(
                            this@MainActivity,
                            R.string.error_loading,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Result.Success -> handleOnSuccess(result.data)
                }
            }
        }
    }

    private fun handleOnSuccess(data: List<Item>) {
        val adapter = ItemAdapter(data) {
            // listener do item clicado
            startActivity(ItemDetailActivity.newIntent(
                this,
                it.id
            ))
        }
        binding.recyclerView.adapter = adapter
    }

    private fun setupView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = true
            fetchItems()
        }
        binding.addCta.setOnClickListener {
            startActivity(NewItemActivity.newIntent(this))
        }
    }
}