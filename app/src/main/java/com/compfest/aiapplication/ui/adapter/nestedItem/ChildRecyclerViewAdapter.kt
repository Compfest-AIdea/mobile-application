package com.compfest.aiapplication.ui.adapter.nestedItem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.compfest.aiapplication.databinding.AnswerItemBinding

class ChildRecyclerViewAdapter(private val childItemList: List<String>): RecyclerView.Adapter<ChildRecyclerViewAdapter.ChildViewHolder>() {
    inner class ChildViewHolder(itemBinding: AnswerItemBinding): RecyclerView.ViewHolder(itemBinding.root) {
        val childTvDesc = itemBinding.childDescription
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val binding: AnswerItemBinding = AnswerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChildViewHolder(binding)
    }

    override fun getItemCount(): Int = childItemList.size

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val childItem = childItemList[position]
        holder.childTvDesc.text = childItem
    }
}