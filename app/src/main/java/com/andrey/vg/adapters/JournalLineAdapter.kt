package com.andrey.vg.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.andrey.vg.R
import com.andrey.vg.models.Students
import java.text.SimpleDateFormat

class JournalLineAdapter(val context: Context?, val students: List<Students>, val dates: List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_STUDENT = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.model_jurnal_date, parent, false)
                HeaderViewHolder(view)
            }
            TYPE_STUDENT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.model_jurnal_line, parent, false)
                StudentViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(dates)
            is StudentViewHolder -> {
                val student = students[position - 1] // Позиция 0 — заголовок, поэтому students[position - 1]
                holder.bind(student, dates)
            }
        }
    }

    override fun getItemCount(): Int {
        return students.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_HEADER
        } else {
            TYPE_STUDENT
        }
    }

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val studentName: TextView = itemView.findViewById(R.id.studentName)
        private val gradeViews: List<TextView> = listOf(
            itemView.findViewById(R.id.grade_0),
            itemView.findViewById(R.id.grade_1),
            itemView.findViewById(R.id.grade_2),
            itemView.findViewById(R.id.grade_3),
            itemView.findViewById(R.id.grade_4),
            itemView.findViewById(R.id.grade_5),
            itemView.findViewById(R.id.grade_6),
            itemView.findViewById(R.id.grade_7),
        )

        fun bind(student: Students, dates: List<String>) {
            studentName.text = student.name

            for (i in dates.indices) {
                val date = dates[i]
                val grade = student.grades[date] ?: ""
                gradeViews[i].text = grade
            }
            for(view in gradeViews){
                view.setOnClickListener {

                }
            }
        }
    }
    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerDateViews: List<TextView> = listOf(
            itemView.findViewById(R.id.headerDate_0),
            itemView.findViewById(R.id.headerDate_1),
            itemView.findViewById(R.id.headerDate_2),
            itemView.findViewById(R.id.headerDate_3),
            itemView.findViewById(R.id.headerDate_4),
            itemView.findViewById(R.id.headerDate_5),
            itemView.findViewById(R.id.headerDate_6),
            itemView.findViewById(R.id.headerDate_7)
        )

        fun bind(dates: List<String>) {
            val formater = SimpleDateFormat("yyyy-MM-dd")
            for (i in headerDateViews.indices) {
                if (i < dates.size) {
                    headerDateViews[i].text = dates[i].toString().substring(8) // Заполняем только если дата существует
                } else {
                    headerDateViews[i].text = "" // Очищаем TextView, если даты нет
                }
            }
        }
    }
}