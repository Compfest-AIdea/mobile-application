package com.compfest.aiapplication.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.compfest.aiapplication.data.nestedItem.FaqItem
import com.compfest.aiapplication.databinding.ActivityFaqBinding
import com.compfest.aiapplication.loadFaqDataFromJson
import com.compfest.aiapplication.ui.adapter.nestedItem.ParentRecyclerViewAdapter

class FaqActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFaqBinding
    private lateinit var parentRV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)

        parentRV = binding.parentRecyclerView
        parentRV.setHasFixedSize(true)
        parentRV.layoutManager = LinearLayoutManager(this)

        val origin = intent.getStringExtra(ORIGIN)
        if (origin != null) {
            val adapterData = if (origin == ResultActivity::class.java.simpleName) {
                getAdapterData("faq_part2.json")
            } else {
                getAdapterData("faq_part1.json")
            }
            val adapter = ParentRecyclerViewAdapter(adapterData)
            parentRV.adapter = adapter
        }
    }

    private fun getAdapterData(fileName: String): List<FaqItem> {
        val faqItem = ArrayList<FaqItem>()
        val faqList = loadFaqDataFromJson(fileName, this)
        for (faq in faqList) {
            faqItem.add(
                FaqItem(question = faq.question, sublist = faq.answers)
            )
        }
        return faqItem
    }

    companion object {
        const val ORIGIN = "origin"
    }
}