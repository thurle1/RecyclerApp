package com.example.recyclingapp

import com.example.recyclingapp.Interfaces.IRecycleRepository
import com.example.recyclingapp.Model.Recycle
import com.example.recyclingapp.Model.RecycleWays

class RecycleRepository: IRecycleRepository {
    private var recycleList: MutableList<Recycle> = mutableListOf()

    init {
        recycleList.add(Recycle("Food Scraps", RecycleWays.COMPOST))
        recycleList.add(Recycle("Plastic Cup", RecycleWays.RECYCLE))
        recycleList.add(Recycle("Paper Bag", RecycleWays.RECYCLE))
        recycleList.add(Recycle("Paper Cup", RecycleWays.RECYCLE))
        recycleList.add(Recycle("Styrofoam", RecycleWays.LANDFILL))
        recycleList.add(Recycle("Aluminum Can", RecycleWays.RECYCLE))
        recycleList.add(Recycle("Paper", RecycleWays.RECYCLE))
        recycleList.add(Recycle("Plastic Bottle", RecycleWays.RECYCLE))
        recycleList.add(Recycle("Egg Shells", RecycleWays.COMPOST))
        recycleList.add(Recycle("Coffee Grinds", RecycleWays.COMPOST))
        recycleList.add(Recycle("Cardboard Box", RecycleWays.RECYCLE))
        recycleList.add(Recycle("Used Napkins", RecycleWays.COMPOST))
        recycleList.add(Recycle("Bottle", RecycleWays.RECYCLE))
        recycleList.add(Recycle("cardboard", RecycleWays.RECYCLE))
    }

    override fun getAll(): List<Recycle> {
        return recycleList.toMutableList()
    }

    override fun getCount(): Int {
        return recycleList.size
    }

    override fun getRecycle(idx: Int): Recycle {
        return recycleList[idx]
    }

    override fun clear() {
        recycleList.clear()
    }

    override fun addAll(list: List<Recycle>) {
        recycleList.addAll(list)
    }

    override fun contains(name: String): Boolean {
        for(item: Recycle in recycleList){
            if(item.name.contains(name, true)){
                return true
            }
        }
        return false
    }

    override fun getRecycleMethod(name: String): String {
        for(item: Recycle in recycleList){
            if(item.name.contains(name, true)){
                return when(item.howToRecycle){
                    RecycleWays.RECYCLE -> {
                        "Recycle"
                    }
                    RecycleWays.COMPOST -> {
                        "Compost"
                    }
                    RecycleWays.LANDFILL -> {
                        "Landfill"
                    }
                }
            }
        }
        return "Item Not In Database"
    }
}