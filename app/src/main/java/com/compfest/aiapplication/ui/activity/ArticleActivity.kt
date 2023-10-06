package com.compfest.aiapplication.ui.activity

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.compfest.aiapplication.data.Article
import com.compfest.aiapplication.databinding.ActivityArticleBinding

class ArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val article = getParcelable()
        if (article != null) {
            setArticle(article)
        }

    }

    private fun setArticle(article: Article) {
        binding.tvArticleTitle.text = article.title
        binding.tvArticleStory.text = article.story
        binding.ivArticleImage.setImageResource(article.imagePath)
    }

    private fun getParcelable(): Article? {
        val parcel = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_ARTICLE, Article::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_ARTICLE)
        }
        return parcel
    }

    companion object {
        const val EXTRA_ARTICLE = "extra_article"
    }
}