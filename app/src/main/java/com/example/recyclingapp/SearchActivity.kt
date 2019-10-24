package com.example.recyclingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclingapp.Interfaces.IRecycleController
import com.example.recyclingapp.Interfaces.IRecycleRepository
import com.example.recyclingapp.Model.Recycle
import com.example.recyclingapp.Model.RecycleWays
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), IRecycleController {

    override val recycles = RecycleRepository()
    val adapter = RecycleAdapter(this, recycles.getAll())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //Bind the recycler


        recycle_recycle.adapter = adapter
        recycle_recycle.layoutManager = LinearLayoutManager(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu?.findItem(R.id.search_menu)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(QueryListener())
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        return true
    }
    inner class QueryListener: SearchView.OnQueryTextListener{
        override fun onQueryTextChange(newText: String?): Boolean {
            adapter.filter.filter(newText)
            return false
        }

        override fun onQueryTextSubmit(p0: String?): Boolean {
            return false
        }
    }
}
