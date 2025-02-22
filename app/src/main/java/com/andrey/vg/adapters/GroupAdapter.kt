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
import com.andrey.vg.screens.MainActivity
import com.andrey.vg.screens.main.group.GroupInfoActivity

class GroupAdapter(val context: Context?, var listGroup: List<String>, var role: String): RecyclerView.Adapter<GroupAdapter.GroupViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.model_group, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.groupName.text = listGroup[position]

        holder.itemView.setOnClickListener {
            when(role){
                "Scheduler" -> {
                    Toast.makeText(context, "add schedular", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    context?.startActivity(Intent(context as MainActivity, GroupInfoActivity::class.java)
                        .putExtra("role", role)
                        .putExtra("group_id", listGroup[position])
                    )
                }
            }
        }
    }

    override fun getItemCount() = listGroup.size

    class GroupViewHolder(view: View): RecyclerView.ViewHolder(view){
        var groupName: TextView
        init {
            groupName = view.findViewById(R.id.model_group_name)
        }
    }
}