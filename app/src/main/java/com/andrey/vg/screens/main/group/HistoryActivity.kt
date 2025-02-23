package com.andrey.vg.screens.main.group

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.andrey.vg.R
import com.andrey.vg.adapters.GradeAdapter
import com.andrey.vg.databinding.ActivityHistoryBinding
import com.andrey.vg.models.GradeHistory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityHistoryBinding
    lateinit var rv: RecyclerView
    lateinit var adaapter: GradeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        rv = binding.rvHistory
        val uid = FirebaseAuth.getInstance().uid.toString()
        getGradeHistory(uid, {
            for(i in it){
                Log.d("History", i.subject)
                i.grades.forEach { t, u -> Log.d("History", "$t: $u") }
            }
            adaapter = GradeAdapter(it)
            rv.adapter = adaapter
        })

    }
    fun getGradeHistory(userId: String, callback: (List<GradeHistory>) -> Unit ) {
        val database = FirebaseDatabase.getInstance("https://vgroup-48801-default-rtdb.europe-west1.firebasedatabase.app/").reference

        database.child("Grades").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val gradeHistoryList = mutableListOf<GradeHistory>()

                    for (subjectSnapshot in snapshot.children) {
                        val subjectName = subjectSnapshot.key ?: ""
                        val gradesMap = sortedMapOf<String, String>()

                        for (gradeEntry in subjectSnapshot.children) {
                            val date = gradeEntry.key ?: ""
                            val grade = gradeEntry.getValue(String::class.java) ?: ""
                            gradesMap[date] = grade
                        }

                        // Добавляем предмет и его оценки в список
                        gradeHistoryList.add(GradeHistory(subjectName, gradesMap))
                    }

                    // Возвращаем результат через callback
                    callback(gradeHistoryList)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Обработка ошибок
                    println("Ошибка при загрузке истории оценок: ${error.message}")
                    callback(emptyList())
                }
            })
    }
}