package com.alisonnakashima.minhaprimeiraapi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alisonnakashima.minhaprimeiraapi.databinding.ActivityItemDetailBinding
import com.alisonnakashima.minhaprimeiraapi.model.Item
import com.alisonnakashima.minhaprimeiraapi.service.Result
import com.alisonnakashima.minhaprimeiraapi.service.RetrofitClient
import com.alisonnakashima.minhaprimeiraapi.service.safeApiCall
import com.alisonnakashima.minhaprimeiraapi.ui.loadUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemDetailBinding

    private lateinit var item: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        loadItem()
    }

    private fun setupView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.deleteCTA.setOnClickListener {
            deleteItem()
        }
    }

    private fun loadItem() {
        val itemId = intent.getStringExtra(ARG_ID) ?: ""

        CoroutineScope(Dispatchers.IO).launch {
            val result = safeApiCall { RetrofitClient.apiService.getItem(itemId) }

            withContext(Dispatchers.Main) {
                when (result) {
                    is Result.Error -> {}
                    is Result.Success -> {
                        item = result.data
                        handleSuccess()
                    }
                }
            }
        }
    }

    private fun deleteItem() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = safeApiCall { RetrofitClient.apiService.deleteItem(item.id) }

            withContext(Dispatchers.Main) {
                when (result) {
                    is Result.Error -> {
                        Toast.makeText(
                            this@ItemDetailActivity,
                            R.string.error_delete,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Result.Success -> {
                        Toast.makeText(
                            this@ItemDetailActivity,
                            R.string.sucess_delete,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun handleSuccess() {
        binding.name.text = "${item.value.name} ${item.value.surname}"
        binding.address.text = item.value.address
        binding.age.text = getString(R.string.item_age, item.value.age.toString())
        binding.image.loadUrl(item.value.imageUrl)
    }

    companion object {

        private const val ARG_ID = "ARG_ID"

        fun newIntent(
            context: Context,
            itemId: String
        ) =
            Intent(context, ItemDetailActivity::class.java).apply {
                putExtra(ARG_ID, itemId)
            }
    }
}