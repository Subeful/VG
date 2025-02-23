package com.andrey.vg.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andrey.vg.R
import com.andrey.vg.models.Schedule
import java.text.SimpleDateFormat

class SchedulAdapter(val context: Context?, val listData: List<Schedule>)
    : RecyclerView.Adapter<SchedulAdapter.ScheduleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.model_schedule, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val info = listData[position]
        val date = info.date
        holder.date.text = date.substring(4,6) + "." + date.substring(6)
        var i = 0
        info.pars.forEach { t, u ->
            if(i==0){holder.n1.setText(t); holder.name1.setText(u)}
            else if(i==1){holder.n2.setText(t); holder.name2.setText(u)}
            else if(i==2){holder.n3.setText(t); holder.name3.setText(u)}
            else if(i==3){holder.n4.setText(t); holder.name4.setText(u)}
            i++
        }
    }

    override fun getItemCount() = listData.size

    class ScheduleViewHolder(view: View): RecyclerView.ViewHolder(view){
        val date = view.findViewById<TextView>(R.id.model_shc_date)
        val n1 = view.findViewById<TextView>(R.id.model_shc_i_1)
        val n2 = view.findViewById<TextView>(R.id.model_shc_i_2)
        val n3 = view.findViewById<TextView>(R.id.model_shc_i_3)
        val n4 = view.findViewById<TextView>(R.id.model_shc_i_4)
        val name1 = view.findViewById<TextView>(R.id.model_shc_sub_1)
        val name2 = view.findViewById<TextView>(R.id.model_shc_sub_2)
        val name3 = view.findViewById<TextView>(R.id.model_shc_sub_3)
        val name4 = view.findViewById<TextView>(R.id.model_shc_sub_4)
    }
}