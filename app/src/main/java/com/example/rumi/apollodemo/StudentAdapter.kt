package com.example.rumi.apollodemo

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class StudentAdapter(private val studentList: List<Any>?): RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, R.layout.stuent_single_row,parent,false)
        return StudentViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return studentList!!.size
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student= studentList!![position]
        if (student is AllStudentsQuery.AllStudent)
            holder.bind(student.name())
        else if (student is CreateStudentQuery.CreateStudent)
            holder.bind(student.name())


    }

    inner class StudentViewHolder(val binding:ViewDataBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item:kotlin.Any){
            binding.setVariable(BR.item, item)
            binding.executePendingBindings()
        }
    }
}