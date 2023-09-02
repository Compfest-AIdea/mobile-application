package com.compfest.aiapplication.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.compfest.aiapplication.R
import com.compfest.aiapplication.data.PredictionResult

class PredictionAdapter(
    private val onClick: (PredictionResult) -> Unit
): PagedListAdapter<PredictionResult, PredictionAdapter.PredictionViewHolder>(DIFF_CALLBACK) {
    inner class PredictionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tvIndex: TextView = itemView.findViewById(R.id.item_tv_index)
        fun bind(predictionResult: PredictionResult) {
            tvIndex.text = "Ke-".plus(predictionResult.id.toString())

            itemView.setOnClickListener {
                onClick(predictionResult)
            }
        }
    }

    override fun onBindViewHolder(holder: PredictionViewHolder, position: Int) {
        val predictionResult = getItem(position) as PredictionResult
        holder.bind(predictionResult)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.result_item, parent, false)
        return PredictionViewHolder(itemView)
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PredictionResult>() {
            override fun areItemsTheSame(oldItem: PredictionResult, newItem: PredictionResult): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PredictionResult, newItem: PredictionResult): Boolean {
                return oldItem == newItem
            }
        }

    }

}