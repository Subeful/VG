package com.andrey.vg.screens.main.group

import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.andrey.vg.R
import com.andrey.vg.adapters.JournalLineAdapter
import com.andrey.vg.databinding.ActivityLessonGroupBinding
import com.andrey.vg.models.Students
import java.text.SimpleDateFormat
import java.util.Date

class LessonGroupActivity : AppCompatActivity() {

    lateinit var binding: ActivityLessonGroupBinding
    lateinit var rv: RecyclerView
    lateinit var adapter: JournalLineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonGroupBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)


        val students = listOf(
            Students("Иван Иванов", mapOf("2023-10-01" to "5", "2023-10-02" to "4", "2023-10-04" to "5", "2023-10-06" to "1")),
            Students("Петр Петров", mapOf("2023-10-01" to "3", "2023-10-02" to "н", "2023-10-03" to "3", "2023-10-08" to "2")),
        )
        val dates = getListDates(students)

        rv = binding.rvJournal
        adapter = JournalLineAdapter(this, students, dates)
        rv.adapter = adapter

    }
    fun getListDates(students: List<Students>): List<String>{
        var date = Calendar.getInstance()
        date.time = Date(0)
        val formater = SimpleDateFormat("yyyy-MM-dd")
        for(student in students){
            for (day in student.grades.keys){
                if(formater.parse(day).after(date.time)) {
                    date.time = formater.parse(day)
                }
            }
        }
        var listDate = ArrayList<String>(9)
        date.add(Calendar.DAY_OF_WEEK, -7)
        for(i in 0..7){
            listDate.add(formater.format(date.time))
            Log.d("MyLog", "date: " + formater.format(date.time))
            date.add(Calendar.DAY_OF_WEEK, 1)
        }
        return listDate

    }
}