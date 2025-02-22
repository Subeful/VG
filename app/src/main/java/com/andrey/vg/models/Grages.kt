package com.andrey.vg.models

data class Grages(val student_id: String, val days: Array<DayMark>)
data class DayMark(val date: String, val lessons: Array<Marks>)
data class Marks(val subject: String, val grade: String, val teacher: String)