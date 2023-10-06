package com.compfest.aiapplication.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.compfest.aiapplication.R
import com.compfest.aiapplication.data.Article
import com.compfest.aiapplication.databinding.FragmentArticlesBinding
import com.compfest.aiapplication.ui.activity.ArticleActivity
import com.compfest.aiapplication.ui.adapter.ArticleAdapter

class ArticlesFragment : Fragment() {
    private var _binding: FragmentArticlesBinding? = null
    private val binding get() = _binding!!
    private lateinit var recycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListOfArticle()
    }

    private fun setListOfArticle() {
        val articleTitle = resources.getStringArray(R.array.disease_name)
        val articleStory = resources.getStringArray(R.array.disease_desc)
        val articleImage = resources.obtainTypedArray(R.array.article_image)
        val arrayOfArticle = ArrayList<Article>()
        for (i in articleTitle.indices) {
            arrayOfArticle.add(
                Article(articleTitle[i], articleStory[i], articleImage.getResourceId(i, -1))
            )
        }
        if (arrayOfArticle.isNotEmpty()) {
            binding.rvArticleList.visibility = View.VISIBLE
            binding.tvEmptyMessage.visibility = View.GONE

            val adapter = ArticleAdapter(arrayOfArticle) {
                Toast.makeText(requireContext(), it.title, Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), ArticleActivity::class.java)
                intent.putExtra(ArticleActivity.EXTRA_ARTICLE, it)
                startActivity(intent)
            }

            recycler = binding.rvArticleList
            recycler.layoutManager = LinearLayoutManager(requireContext())
            recycler.adapter = adapter
        }
    }
}