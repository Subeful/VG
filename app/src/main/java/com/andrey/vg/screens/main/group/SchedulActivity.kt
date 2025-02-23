package com.andrey.vg.screens.main.group

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.andrey.vg.adapters.SchedulAdapter
import com.andrey.vg.databinding.ActivitySchedulBinding
import com.andrey.vg.models.Schedule
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jetbrains.annotations.Async
import java.util.LinkedList

class SchedulActivity : AppCompatActivity() {

    lateinit var binding: ActivitySchedulBinding
    lateinit var adapter: SchedulAdapter
    lateinit var rv: RecyclerView
    var group_id = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchedulBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        init()

        loadSchedul()
    }

    fun init(){
        rv = binding.rvSch
        /*adapter = SchedulAdapter(this, listOf(
            Schedule("22.02", linkedMapOf(
                "1-2" to "matan", "3" to "1C")
            ),
            Schedule("23.02", linkedMapOf(
                "2" to "matan", "3" to "1C")
            ),
            Schedule("24.02", linkedMapOf(
                "1" to "matan", "2" to "history", "3" to "1C", "4" to "az")
            ),
            ))
        rv.adapter = adapter*/

        group_id = intent.getStringExtra("group_id").toString()
        binding.tvSchGroupName.text = group_id
    }

    fun loadSchedul(){
        val ref = FirebaseDatabase.getInstance("https://vgroup-48801-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference().child("Sheduler")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dates = mutableListOf<String>()
                for (dateSnapshot in dataSnapshot.children) {
                    dates.add(dateSnapshot.key ?: "")
                }

                dates.sortDescending()

                val lastFiveDates = dates.take(5)

                val listschedul = LinkedList<Schedule>()

                for (date in lastFiveDates) {
                    val groupSchedule = dataSnapshot.child(date).child(group_id)
                    if (groupSchedule.exists()) {
                        val parsMap = LinkedHashMap<String, String>()

                        // Обрабатываем данные в зависимости от их типа (Map или List)
                        when (val scheduleData = groupSchedule.value) {
                            is Map<*, *> -> {
                                // Если данные в формате Map (например, {"1-4": "Пайганова"})
                                for ((key, value) in scheduleData) {
                                    parsMap[key.toString()] = value.toString()
                                }
                            }
                            is List<*> -> {
                                // Если данные в формате List (например, [null, "aaa", null, "bbb"])
                                for (i in scheduleData.indices) {
                                    val value = scheduleData[i]
                                    if (value != null) {
                                        parsMap["${i + 1}"] = value.toString()
                                    }
                                }
                            }
                        }

                        // Создаем объект Schedule и добавляем его в список
                        listschedul.add(Schedule(date, parsMap))
                        listschedul.forEach{
                            Log.d("Firebase", "${it.date} + ")
                            for (i in it.pars){
                                Log.d("Firebase", "${i.key} - ${i.value}")
                            }
                        }
                        adapter = SchedulAdapter(baseContext, listschedul)
                        rv.adapter = adapter
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("FirebaseData", "Failed to read value.", databaseError.toException())
            }
        })

    }
}