package com.andrey.vg.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andrey.vg.R
import com.andrey.vg.screens.MainActivity
import com.andrey.vg.screens.main.group.GroupInfoActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat


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
                    showNewScheduleDialog(position)
                }
                else -> {
                    context?.startActivity(Intent(context as MainActivity, GroupInfoActivity::class.java)
                        .putExtra("role", role)
                        .putExtra("group_id", listGroup[position])
                    )
                }
            }
        }
        holder.itemView.setOnLongClickListener {
            deleteScheduler(position)
            true
        }
    }
    private fun deleteScheduler(position: Int){

    }
    private fun showNewScheduleDialog(position: Int) {
        val view = LayoutInflater.from(context).inflate(R.layout.alert_new_shedule, null)
        val date = view.findViewById<EditText>(R.id.alert_sch_date)
        val n_1 = view.findViewById<EditText>(R.id.alert_sch_num_1)
        val n_2 = view.findViewById<EditText>(R.id.alert_sch_num_2)
        val n_3 = view.findViewById<EditText>(R.id.alert_sch_num_3)
        val n_4 = view.findViewById<EditText>(R.id.alert_sch_num_4)
        val name_1 = view.findViewById<EditText>(R.id.alert_sch_name_1)
        val name_2 = view.findViewById<EditText>(R.id.alert_sch_name_2)
        val name_3 = view.findViewById<EditText>(R.id.alert_sch_name_3)
        val name_4 = view.findViewById<EditText>(R.id.alert_sch_name_4)

        val dialog = AlertDialog.Builder(context)
            .setTitle("Введите расписание для ${listGroup[position]}")
            .setView(view)
            .setPositiveButton("ОК") { _, _ ->
                var date = date.text.toString()
                val n1 = n_1.text.toString()
                val n2 = n_2.text.toString()
                val n3 = n_3.text.toString()
                val n4 = n_4.text.toString()
                val name1 = name_1.text.toString()
                val name2 = name_2.text.toString()
                val name3 = name_3.text.toString()
                val name4 = name_4.text.toString()

                val info = HashMap<String, String>()
                if (n1.isNotEmpty()) info[n1] = name1
                if (n2.isNotEmpty()) info[n2] = name2
                if (n3.isNotEmpty()) info[n3] = name3
                if (n4.isNotEmpty()) info[n4] = name4

                if(date.isEmpty())
                    loadToDb(position, info)
                else if (date.length == 8 && (date.toIntOrNull()) != null)
                    loadToDb(position, info, date)
            }
            .setNegativeButton("Отмена", null)
            .create()
        dialog.show()
    }
    fun loadToDb(position: Int, info: Map<String, String>, date: String = getDate()){

        val ref = FirebaseDatabase.getInstance("https://vgroup-48801-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference().child("Sheduler").child(date).child(listGroup[position])

        ref.setValue(info)
    }

    fun getDate(): String{
        val date = Calendar.getInstance()
        return SimpleDateFormat("yyyyMMdd").format(date.time)
    }

    override fun getItemCount() = listGroup.size

    class GroupViewHolder(view: View): RecyclerView.ViewHolder(view){
        var groupName: TextView
        init {
            groupName = view.findViewById(R.id.model_group_name)
        }
    }
}