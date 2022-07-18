package com.promedia.retrofitcorrutinas

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.promedia.retrofitcorrutinas.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(),SearchView.OnQueryTextListener {
    private lateinit var b : ActivityMainBinding
    private lateinit var adapter : DogAdapter
    private val dogImages = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.searchView.setOnQueryTextListener(this)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = DogAdapter(dogImages)
        b.rvDogs.layoutManager = LinearLayoutManager (this)
        b.rvDogs.adapter = adapter
    }

    // Esta función hace una llamda a Inet y puede bloquear el hilo principal
    // https://dog.ceo/api/breed/[raza]/images
    fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            // Llamada a la librería que convierte de JSON a DogsResponse
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Función que crea una corrutina con la llamada a Retrofit
    // Le paso como parámetro la raza de perro que busco
    fun searchByName(query:String){
        // Creo un corrutina para hacer la llamada a la API
        CoroutineScope(Dispatchers.IO).launch {
            // Creo una variable para recoger lo que devuelve la llamada
            val call = getRetrofit().create(APIService::class.java)
                .getDogsByBreeds("$query/images") // Devuelve un objeto Response
            val puppies = call.body() // Del objeto Response cojo sólo el código (JSON)
            // To-do lo que tenga que ver con parte visual debe ejecutarse en el hilo principal
            // Lo que ejecute en esta función se ejecutará en el hilo principal
            runOnUiThread {
                if (call.isSuccessful){
                    // Show RecyclerView
                    val images = puppies?.images ?: emptyList()
                    dogImages.clear()
                    dogImages.addAll(images)
                    // Aviso al adapter que habido cambios
                    adapter.notifyDataSetChanged()
                } else {
                    showError() // Función propia que muestra el error
                }
                hideKeyBoard()
            }
        }
    }
    // Función que oculta el teclado
    private fun hideKeyBoard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(b.constraintLayoutMain.windowToken, 0)
    }

    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            searchByName(query.lowercase())
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}