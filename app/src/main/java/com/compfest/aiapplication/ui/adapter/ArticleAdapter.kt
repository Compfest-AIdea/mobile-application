package com.compfest.aiapplication.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.compfest.aiapplication.data.Article
import com.compfest.aiapplication.databinding.ArticleItemBinding

class ArticleAdapter(
    private val articleList: ArrayList<Article>,
    private val onClick: (Article) -> Unit
): RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ArticleItemBinding): RecyclerView.ViewHolder(binding.root) {
        private val tvTitle = binding.itemTvTitle
        private val ivArticle = binding.itemIvArticle
        private val tvDesc = binding.itemTvSortDesc

        fun bind(article: Article) {
            tvTitle.text = article.title
            ivArticle.setImageResource(article.imagePath)
            tvDesc.text = article.story
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ArticleItemBinding = ArticleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = articleList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentArticle = articleList[position]
        holder.bind(currentArticle)

        holder.itemView.setOnClickListener {
            onClick(currentArticle)
        }
    }
}