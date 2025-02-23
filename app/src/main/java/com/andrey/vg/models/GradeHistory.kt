package com.andrey.vg.models

import java.util.SortedMap

data class GradeHistory(val subject: String, val grades: SortedMap<String, String>)