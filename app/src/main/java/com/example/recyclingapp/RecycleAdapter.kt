package com.example.recyclingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclingapp.Interfaces.IRecycleController
import com.example.recyclingapp.Model.Recycle
import com.example.recyclingapp.Model.RecycleWays
import kotlinx.android.synthetic.main.recycle_recycler_view.view.*

class RecycleAdapter(private val controller : IRecycleController, private val fullList: List<Recycle>) : RecyclerView.Adapter<RecyclerViewHolder>(), Filterable{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_recycler_view, parent, false)
        val viewHolder = RecyclerViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return controller.recycles.getCount()
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val recycle = controller.recycles.getRecycle(position)
        holder.bindRecycle(recycle)
    }

    override fun getFilter(): Filter {
        return myFilter()
    }


    inner class myFilter() : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<Recycle>()
            if(constraint == null || constraint.length == 0){
                filteredList.addAll(fullList)
            } else{
                val filterPattern = constraint.toString().toLowerCase().trim()

                for(item in fullList){
                    if(item.name.toLowerCase().contains(filterPattern)){
                        filteredList.add(item)
                    }
                }
            }
            var results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            controller.recycles.clear()

            controller.recycles.addAll(results?.values as List<Recycle>)
            notifyDataSetChanged()
        }
    }
}



class RecyclerViewHolder(view: View): RecyclerView.ViewHolder(view){
    fun bindRecycle(recycle:Recycle){
        itemView.item_name_tv.text = recycle.name
        itemView.how_to_rec_tv.text = when(recycle.howToRecycle){
            RecycleWays.LANDFILL -> "Landfill"
            RecycleWays.RECYCLE -> "Recycle"
            RecycleWays.COMPOST -> "Compost"
        }
    }
}