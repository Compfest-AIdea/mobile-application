package com.compfest.aiapplication.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import com.compfest.aiapplication.R
import com.compfest.aiapplication.databinding.FragmentAddTwoBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddFragmentTwo.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddFragmentTwo : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentAddTwoBinding? = null
    private val binding get() = _binding!!
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        val nextButton: Button = view.findViewById(R.id.btn_next_two)
        nextButton.setOnClickListener {
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().replace(R.id.fragment_container, AddFragmentThree()).addToBackStack(AddFragmentTwo::class.java.simpleName).commit()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddFragmentTwo.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragmentTwo().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
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