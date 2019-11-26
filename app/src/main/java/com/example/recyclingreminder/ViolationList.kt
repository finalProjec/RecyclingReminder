package com.example.recyclingreminder

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class ViolationList(private val context: Activity, internal var violationList: List<Violation>) : ArrayAdapter<Violation>(context, R.layout.violation_list_layout, violationList) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.violation_list_layout, null, true)

        val textViewNumber = listViewItem.findViewById<View>(R.id.textViewNumber) as TextView
        val textViewDate = listViewItem.findViewById<View>(R.id.textViewDate) as TextView

        val violation = violationList[position]
        textViewNumber.text = violation.OffenseDate
        textViewDate.text = violation.OffenseNumber

        return listViewItem
    }
}