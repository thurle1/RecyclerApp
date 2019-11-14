package com.example.recyclingapp.Interfaces

import com.example.recyclingapp.Model.Recycle

interface IRecycleRepository{
    fun getCount(): Int
    fun getRecycle(idx: Int): Recycle
    fun getAll(): List<Recycle>
    fun clear()
    fun addAll(list: List<Recycle>)
    fun contains(name: String): Boolean
    fun getRecycleMethod(name: String): String
}