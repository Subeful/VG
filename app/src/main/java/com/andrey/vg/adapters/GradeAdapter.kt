package com.andrey.vg.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andrey.vg.R
import com.andrey.vg.models.GradeHistory

class GradeAdapter(val gradeHistoryList: List<GradeHistory>) : RecyclerView.Adapter<GradeAdapter.GradeViewHolder>() {

    class GradeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectName: TextView = itemView.findViewById(R.id.model_his_subjectName)
        val gradeDetails: TextView = itemView.findViewById(R.id.model_his_gradeDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.model_history, parent, false)
        return GradeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GradeViewHolder, position: Int) {
        val currentItem = gradeHistoryList[position]

        holder.subjectName.text = currentItem.subject

        val gradesText = currentItem.grades.entries.joinToString("\n") { (date, grade) ->
            "$date: $grade"
        }
        holder.gradeDetails.text = gradesText
    }

    override fun getItemCount() = gradeHistoryList.size
}