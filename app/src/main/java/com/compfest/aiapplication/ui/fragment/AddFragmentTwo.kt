package com.compfest.aiapplication.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.compfest.aiapplication.R
import com.compfest.aiapplication.databinding.FragmentAddTwoBinding
import com.compfest.aiapplication.model.AddViewModel

class AddFragmentTwo : Fragment() {
    private var _binding: FragmentAddTwoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddViewModel by activityViewModels()
    private var thisFragmentName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            thisFragmentName = it.getString("Fragment")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        val nextButton: Button = view.findViewById(R.id.btn_next_two)
        nextButton.setOnClickListener {
            val pressureLevel = binding.spinnerLevelOfPressure.selectedItemPosition.toString()
            val stressLevel = binding.spinnerLevelOfStress.selectedItemPosition.toString()
            val swimming = binding.spinnerHaveSwimmedBool.selectedItemPosition.toString()
            val hairWashing = binding.spinnerHairWashedBool.selectedItemPosition.toString()
            val dandruff = binding.spinnerDandruffConditionLvl.selectedItemPosition.toString()
            viewModel.saveDataFragmentTwo(pressureLevel, stressLevel, swimming, hairWashing, dandruff)

            val fragment = AddFragmentThree()
            fragment.arguments = Bundle().apply { putString("Fragment", fragment::class.java.simpleName) }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("$thisFragmentName to ${fragment::class.java.simpleName}")
                .commit()
        }
    }

    private fun setData() {
        val senseLevel = resources.getStringArray(R.array.sense_level)
        val boolVal = resources.getStringArray(R.array.bool_val)
        val conditionLevel = resources.getStringArray(R.array.condition_level)
        binding.spinnerLevelOfPressure.apply {
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, senseLevel)
            setSelection(0)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    //
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    //
                }
            }
        }
        binding.spinnerLevelOfStress.apply {
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, senseLevel)
            setSelection(0)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    //
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    //
                }
            }
        }
        binding.spinnerHaveSwimmedBool.apply {
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, boolVal)
            setSelection(0)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    //
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    //
                }
            }
        }
        binding.spinnerHairWashedBool.apply {
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, boolVal)
            setSelection(0)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    //
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    //
                }
            }
        }
        binding.spinnerDandruffConditionLvl.apply {
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, conditionLevel)
            setSelection(0)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    //
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    //
                }
            }
        }
    }
}