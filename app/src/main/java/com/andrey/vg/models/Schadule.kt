package com.andrey.vg.models

data class Schedule(val date: String, val lessons: Array<Lesson>)
data class Lesson(val time: String, val subject: String, val teacher: String)