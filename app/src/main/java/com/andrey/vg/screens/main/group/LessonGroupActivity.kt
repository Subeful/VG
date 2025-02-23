package com.andrey.vg.screens.main.group

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.andrey.vg.R
import com.andrey.vg.adapters.JournalLineAdapter
import com.andrey.vg.databinding.ActivityLessonGroupBinding
import com.andrey.vg.models.Students
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date

class LessonGroupActivity : AppCompatActivity() {

    lateinit var binding: ActivityLessonGroupBinding
    lateinit var rv: RecyclerView
    var subject = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonGroupBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val groupId = intent.getStringExtra("group_id").toString()
        binding.tvGroupName.text = groupId
        rv = binding.rvJournal

        binding.btUpdateSubject.setOnClickListener {
            subject = binding.tvLessonName.text.toString()

            if(binding.tvLessonName.text.toString().isEmpty()) {
                Toast.makeText(baseContext, "Введите название предмета!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            getStudentsByGroupAndSubject(groupId, subject) { studentsList ->
                // studentsList содержит список объектов Students
                for (student in studentsList) {
                    Log.d("MyLog", "Студент: ${student.name}")
                    Log.d("MyLog", "Оценки: ${student.grades}")
                }

                val dates = getListDates(studentsList)
                rv.adapter = JournalLineAdapter(baseContext, studentsList, dates,
                    object : JournalLineAdapter.OnGradeClickListener {
                        override fun onGradeClick(student: Students, date: String, position: Int) {
                            showGradeDialog(student, date)
                        }
                    })
            }
        }

    }

    fun getStudentsByGroupAndSubject(groupId: String, subject: String, callback: (List<Students>) -> Unit) {
        val database = FirebaseDatabase.getInstance("https://vgroup-48801-default-rtdb.europe-west1.firebasedatabase.app/").reference
        val studentsList = mutableListOf<Students>()
        database.child("Users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userSnapshot in snapshot.children) {
                    val userGroupId = userSnapshot.child("group_id").getValue(String::class.java)
                    if (userGroupId == groupId) { // Фильтруем по group_id
                        val userName = userSnapshot.child("userName").getValue(String::class.java) ?: ""
                        val userId = userSnapshot.key ?: ""
                        // Получаем оценки для этого студента
                        database.child("Grades").child(userId).child(subject)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(gradeSnapshot: DataSnapshot) {
                                    val gradesMap = mutableMapOf<String, String>()
                                    // Заполняем оценки по датам
                                    for (gradeEntry in gradeSnapshot.children) {
                                        val date = gradeEntry.key ?: ""
                                        val grade = gradeEntry.getValue(String::class.java) ?: ""
                                        gradesMap[date] = grade
                                    }
                                    // Создаем объект Students и добавляем в список
                                    studentsList.add(Students(userName, gradesMap))
                                    // Если это последний студент, вызываем callback
                                    if (studentsList.size == snapshot.children.count { it.child("group_id").getValue(String::class.java) == groupId }) {
                                        callback(studentsList)
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    // Обработка ошибок
                                    println("Ошибка при загрузке оценок: ${error.message}")
                                }
                            })
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Обработка ошибок
                println("Ошибка при загрузке пользователей: ${error.message}")
            }
        })
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun showGradeDialog(student: Students, date: String) {
        val dialog = AlertDialog.Builder(this)
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT

        dialog.setView(input)
        dialog.setTitle("Введите оценку ${student.name} за $date")
        dialog.setPositiveButton("OK") { _, _ ->
            val grade = input.text.toString()
            student.grades[date] = grade
            rv.adapter?.notifyDataSetChanged()

            findStudentIdByName(student.name, {
                writeGradeToDatabase(it?:"", subject, date, grade)
            })
        }
        dialog.setNegativeButton("Отмена", null)
        dialog.show()
    }

    fun writeGradeToDatabase(studentId: String, subject: String, date: String, grade: String ) {
        val database = FirebaseDatabase.getInstance("https://vgroup-48801-default-rtdb.europe-west1.firebasedatabase.app/").reference

        val gradePath = "Grades/$studentId/$subject/$date"

        database.child(gradePath).setValue(grade)
            .addOnSuccessListener {
               Log.d("MyLog","$subject, $date")
               Log.d("MyLog","Оценка успешно записана!")
            }
            .addOnFailureListener { error ->
                println("Ошибка при записи оценки: ${error.message}")
            }
    }

    fun findStudentIdByName(studentName: String, callback: (String?) -> Unit ) {
        val database = FirebaseDatabase.getInstance("https://vgroup-48801-default-rtdb.europe-west1.firebasedatabase.app/").reference

        database.child("Users").orderByChild("userName").equalTo(studentName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val userId = userSnapshot.key
                            callback(userId)
                            return
                        }
                    } else {
                        callback(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Обработка ошибок
                    Log.d("MyLog","Ошибка при поиске студента: ${error.message}")
                    callback(null)
                }
            })
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
        if(date.before(Calendar.getInstance()))
            date = Calendar.getInstance()
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