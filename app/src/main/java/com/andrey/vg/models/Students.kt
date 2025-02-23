package com.andrey.vg.models

data class Students(
    val name: String,
    val grades: MutableMap<String, String>
)