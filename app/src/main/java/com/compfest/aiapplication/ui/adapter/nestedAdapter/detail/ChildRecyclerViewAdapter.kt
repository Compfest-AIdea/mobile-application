package com.compfest.aiapplication.ui.adapter.nestedAdapter.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.compfest.aiapplication.databinding.AnswerItemBinding
import com.compfest.aiapplication.databinding.ExpandedDetailItemBinding

class ChildRecyclerViewAdapter(private val childItemList: List<String>): RecyclerView.Adapter<ChildRecyclerViewAdapter.ChildViewHolder>() {
    inner class ChildViewHolder(itemBinding: ExpandedDetailItemBinding): RecyclerView.ViewHolder(itemBinding.root) {
        val childTvDesc = itemBinding.childDescription
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val binding: ExpandedDetailItemBinding = ExpandedDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChildViewHolder(binding)
    }

    override fun getItemCount(): Int = childItemList.size

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val childItem = childItemList[position]
        holder.childTvDesc.text = childItem
    }
}