package com.example.recyclingapp.Model

data class Recycle(val name : String, val howToRecycle : RecycleWays)

enum class RecycleWays {
    LANDFILL,
    RECYCLE,
    COMPOST
}