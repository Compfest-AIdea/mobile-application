package com.compfest.aiapplication.ui.fragment

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.compfest.aiapplication.R
import com.compfest.aiapplication.databinding.FragmentAddOneBinding
import com.compfest.aiapplication.model.AddViewModel

class AddFragmentOne : Fragment() {
    private var _binding: FragmentAddOneBinding? = null
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
    ): View {
        _binding = FragmentAddOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ThisFragment", thisFragmentName.toString())

        setInputFieldBehavior()
        setNextButtonAction()
    }

    private fun setInputFieldBehavior() {
        binding.stayUpLate.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Tidak perlu melakukan apa-apa sebelum teks berubah
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Tidak perlu melakukan apa-apa saat teks berubah
            }

            override fun afterTextChanged(s: Editable?) {
                // Validasi nilai yang dimasukkan oleh pengguna
                val inputText = s.toString()
                if (inputText.isEmpty()) {
                    // Biarkan kosong atau isi dengan 0 jika Anda ingin mengizinkan nilai 0
                    return
                }

                val value = inputText.toIntOrNull()
                if (value != null && value in 0..8) {
                    // Nilai berada dalam rentang yang valid
                    // Lakukan sesuatu dengan nilai ini jika perlu
                } else {
                    // Nilai tidak valid, atur teks ulang ke nilai yang valid
                    binding.stayUpLate.setText("0")
                    // Tampilkan pesan kesalahan jika perlu
                    Toast.makeText(requireContext(), "Nilai harus antara 0 dan 8", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setNextButtonAction() {
        val nextButton = binding.btnNextOne
        nextButton.setOnClickListener {
            val stayUpLate = binding.stayUpLate.text.toString()
            val coffeeConsumption = binding.coffeeConsumption.text.toString()
            val brainWorkingDuration = binding.brainWorkingDuration.text.toString()

            viewModel.saveDataFragmentOne(stayUpLate, coffeeConsumption, brainWorkingDuration)

            val fragment = AddFragmentTwo()
            fragment.arguments = Bundle().apply { putString("Fragment", fragment::class.java.simpleName) }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("$thisFragmentName to ${fragment::class.java.simpleName}")
                .commit()
        }
    }
}