package com.andrey.vg.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.andrey.vg.R
import com.andrey.vg.models.Users
import com.andrey.vg.screens.MainActivity
import com.andrey.vg.screens.main.group.GroupInfoActivity

class StudentAdapter(val context: Context?, var listGroup: List<String>): RecyclerView.Adapter<StudentAdapter.StudentViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.model_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.stusentName.text = listGroup[position]
    }

    override fun getItemCount() = listGroup.size

    class StudentViewHolder(view: View): RecyclerView.ViewHolder(view){
        var stusentName: TextView
        init {
            stusentName = view.findViewById(R.id.model_student_name)
        }
    }
}