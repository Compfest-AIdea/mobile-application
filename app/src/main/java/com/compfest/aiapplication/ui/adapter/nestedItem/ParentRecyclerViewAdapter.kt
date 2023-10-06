package com.compfest.aiapplication.ui.adapter.nestedItem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.compfest.aiapplication.R
import com.compfest.aiapplication.data.nestedItem.FaqItem
import com.compfest.aiapplication.databinding.QuestionItemBinding

class ParentRecyclerViewAdapter(private val parentItemList: List<FaqItem>): RecyclerView.Adapter<ParentRecyclerViewAdapter.ParentViewHolder>() {
    inner class ParentViewHolder(itemBinding: QuestionItemBinding): RecyclerView.ViewHolder(itemBinding.root) {
        val parentTvTitle = itemBinding.parentTitle
        val parentIvCollapse = itemBinding.collapseIv
        val parentLayout = itemBinding.titleLayout
        val childView = itemBinding.childRecyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val binding: QuestionItemBinding = QuestionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParentViewHolder(binding)
    }

    override fun getItemCount(): Int = parentItemList.size

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val parentItem = parentItemList[position]

        holder.parentTvTitle.text = parentItem.question
        holder.childView.setHasFixedSize(true)
        holder.childView.layoutManager = LinearLayoutManager(holder.itemView.context)
        val adapter = ChildRecyclerViewAdapter(parentItem.sublist)
        holder.childView.adapter = adapter

        val isExpanded = parentItem.isExpanded
        holder.childView.visibility = if (isExpanded) {
            holder.parentIvCollapse.setImageResource(R.drawable.ic_dropup)
            View.VISIBLE
        } else {
            holder.parentIvCollapse.setImageResource(R.drawable.ic_dropdown)
            View.GONE
        }
        holder.parentLayout.setOnClickListener {
            parentItem.isExpanded = !parentItem.isExpanded
            notifyItemChanged(position)
        }
    }
}