package com.compfest.aiapplication.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.compfest.aiapplication.R
import com.compfest.aiapplication.data.PredictionResult
import com.compfest.aiapplication.databinding.FragmentHomeBinding
import com.compfest.aiapplication.model.HomeViewModel
import com.compfest.aiapplication.model.ViewModelFactory
import com.compfest.aiapplication.ui.activity.ResultActivity
import com.compfest.aiapplication.ui.adapter.PredictionAdapter

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var recycler: RecyclerView
    private val predictionAdapter: PredictionAdapter by lazy {
        PredictionAdapter(::onPredictionClick)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = binding.rvResultPrediction
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]

        recycler.adapter = predictionAdapter
        viewModel.predictionResult.observe(requireActivity()) {
            predictionAdapter.submitList(it)
        }
    }

    private fun onPredictionClick(predictionResult: PredictionResult) {
        val intent = Intent(requireContext(), ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_ID, predictionResult.id)
        intent.putExtra("origin", HomeFragment::class.java.simpleName)
        startActivity(intent)
    }
}