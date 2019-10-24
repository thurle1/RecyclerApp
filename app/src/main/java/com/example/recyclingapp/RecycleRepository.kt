package com.example.recyclingapp

import com.example.recyclingapp.Interfaces.IRecycleRepository
import com.example.recyclingapp.Model.Recycle
import com.example.recyclingapp.Model.RecycleWays

class RecycleRepository: IRecycleRepository {
    private var recylcleList: MutableList<Recycle> = mutableListOf()

    init {
        recylcleList.add(Recycle("Food Scraps", RecycleWays.COMPOST))
        recylcleList.add(Recycle("Plastic Cup", RecycleWays.RECYCLE))
        recylcleList.add(Recycle("Paper Bag", RecycleWays.RECYCLE))
        recylcleList.add(Recycle("Paper Cup", RecycleWays.RECYCLE))
        recylcleList.add(Recycle("Styrofoam", RecycleWays.LANDFILL))
        recylcleList.add(Recycle("Aluminum Can", RecycleWays.RECYCLE))
        recylcleList.add(Recycle("Paper", RecycleWays.RECYCLE))
        recylcleList.add(Recycle("Plastic Bottle", RecycleWays.RECYCLE))
        recylcleList.add(Recycle("Egg Shells", RecycleWays.COMPOST))
        recylcleList.add(Recycle("Coffee Grinds", RecycleWays.COMPOST))
        recylcleList.add(Recycle("Cardboard Box", RecycleWays.RECYCLE))
        recylcleList.add(Recycle("Used Napkins", RecycleWays.COMPOST))
    }

    override fun getAll(): List<Recycle> {
        return recylcleList.toMutableList()
    }

    override fun getCount(): Int {
        return recylcleList.size
    }

    override fun getRecycle(idx: Int): Recycle {
        return recylcleList[idx]
    }

    override fun clear() {
        recylcleList.clear()
    }

    override fun addAll(list: List<Recycle>) {
        recylcleList.addAll(list)
    }
}