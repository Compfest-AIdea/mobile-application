package com.compfest.aiapplication.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.compfest.aiapplication.CAMERA_PERMISSION_REQUEST
import com.compfest.aiapplication.R
import com.compfest.aiapplication.databinding.BottomSheetAddImagesBinding
import com.compfest.aiapplication.databinding.FragmentAddThreeBinding
import com.compfest.aiapplication.model.AddViewModel
import com.compfest.aiapplication.saveImage
import com.google.android.material.bottomsheet.BottomSheetDialog

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddFragmentThree.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddFragmentThree : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentAddThreeBinding? = null
    private val viewModel: AddViewModel by viewModels()
    private val binding get() = _binding!!
    private var _bottomSheetBinding: BottomSheetAddImagesBinding? = null
    private val bottomSheetBinding get() = _bottomSheetBinding!!
    private var param1: String? = null
    private var param2: String? = null
    private var thisFragmentName: String? = null
    private var imgBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            thisFragmentName = it.getString("Fragment")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnShowBottomSheet = binding.ifbAddImages
        val bsAddImages = BottomSheetDialog(requireContext())
        btnShowBottomSheet.setOnClickListener {
            val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_add_images, null)
            _bottomSheetBinding = BottomSheetAddImagesBinding.bind(bottomSheetView)
            bsAddImages.setCancelable(false)
            bsAddImages.setContentView(bottomSheetView)
            bsAddImages.show()

            bottomSheetBinding.ivImgPreview.setOnClickListener {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startCamera()
                } else {
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST)
                }
            }

            bottomSheetBinding.btnCancel.setOnClickListener {
                bsAddImages.dismiss()
            }

            bottomSheetBinding.btnSave.setOnClickListener {
                if (imgBitmap != null) {
                    saveImage(imgBitmap, requireContext())
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun startCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imgBitmap = imageBitmap
            bottomSheetBinding.ivImgPreview.setImageBitmap(imageBitmap)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddFragmentThree.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragmentThree().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        const val REQUEST_IMAGE_CAPTURE = 101
    }
}