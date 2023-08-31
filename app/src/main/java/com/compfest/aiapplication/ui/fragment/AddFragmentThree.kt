package com.compfest.aiapplication.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.compfest.aiapplication.CAMERA_PERMISSION_REQUEST
import com.compfest.aiapplication.R
import com.compfest.aiapplication.data.PredictionTabularResult
import com.compfest.aiapplication.databinding.BottomSheetAddImagesBinding
import com.compfest.aiapplication.databinding.FragmentAddThreeBinding
import com.compfest.aiapplication.getImageFromExternalStorage
import com.compfest.aiapplication.ml.HairlossModel
import com.compfest.aiapplication.ml.ScalpModel
import com.compfest.aiapplication.model.AddViewModel
import com.compfest.aiapplication.saveImageToExternalStorage
import com.compfest.aiapplication.ui.activity.ResultActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class AddFragmentThree : Fragment() {
    private var _binding: FragmentAddThreeBinding? = null
    private val binding get() = _binding!!
    private var _bottomSheetBinding: BottomSheetAddImagesBinding? = null
    private val bottomSheetBinding get() = _bottomSheetBinding!!
    private val viewModel: AddViewModel by activityViewModels()
    private var thisFragmentName: String? = null
    private var currentSelection: Int? = null
    private var imageCaptured: Bitmap? = null

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
        _binding = FragmentAddThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setImageCaptureButton()

        val btnSubmitAll = binding.btnSubmitAll
        btnSubmitAll.setOnClickListener {
            val resultTabular = tryingMLTabular()
            val intent = Intent(requireContext(), ResultActivity::class.java)
            intent.apply {
                putExtra(ResultActivity.EXTRA_RESULT_TABULAR, resultTabular)
                putExtra(ResultActivity.EXTRA_PREDICTION_TABULAR_INPUT, viewModel.getParcelableTabularInputData())
            }
            startActivity(intent)
            requireActivity().finish()
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
            imageCaptured = imageBitmap
            bottomSheetBinding.ivImgPreview.setImageBitmap(imageBitmap)
        }
    }

    private fun tryingMLImage() {
        val payload = viewModel.getImageInputData()
        payload.values.forEach {  imagePath ->
            if (!imagePath.isNullOrBlank()) {
                Log.d("TestModelPath", imagePath)
                /*
                val imageBitmap = getImageFromExternalStorage(imagePath)
                if (imageBitmap != null) {
                    val model = ScalpModel.newInstance(requireContext())

                    val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 150, 150, 3), DataType.FLOAT32)
                    val tensorImage = TensorImage(DataType.FLOAT32)
                    val bitmap = Bitmap.createScaledBitmap(imageBitmap, 150, 150, true)
                    tensorImage.load(bitmap)
                    val byteBuffer: ByteBuffer = tensorImage.buffer
                    inputFeature0.loadBuffer(byteBuffer)

                    val output = model.process(inputFeature0)
                    val outputFeature0 = output.outputFeature0AsTensorBuffer
                    val outputArray = outputFeature0.floatArray

                    val result = ArrayList<Float>()
                    for (value in outputArray) {
                        result.add(value)
                        Log.d("TestModel", value.toString())
                    }
                    model.close()
                } else {
                    Toast.makeText(requireContext(), "Sorry your images cannot be found", Toast.LENGTH_SHORT).show()
                }*/
            }
        }
    }

    private fun tryingMLTabular(): PredictionTabularResult {
        val model = HairlossModel.newInstance(requireContext())

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 8), DataType.FLOAT32)
        val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * 8)
        byteBuffer.order(ByteOrder.nativeOrder())
        val payload = viewModel.getTabularInputData()
        payload.values.forEach { value ->
            byteBuffer.putFloat(value)
        }
        byteBuffer.rewind()
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        model.close()

        val resultRaw = ArrayList<Float>()
        println("Output:")
        for (i in 0 until outputFeature0.typeSize) {
            resultRaw.add(outputFeature0.getFloatValue(i))
            Log.d("ModelML","Class $i: ${outputFeature0.getFloatValue(i)}")
        }

        var maxVal = resultRaw[0]
        for (i in 1 until resultRaw.size) {
            val currentVal = resultRaw[i]

            if (currentVal > maxVal) {
                maxVal = currentVal
            }
        }
        val result = PredictionTabularResult(
            class1 = resultRaw[0],
            class2 = resultRaw[1],
            class3 = resultRaw[2],
            class4 = resultRaw[3]
        )
        return result
    }

    private fun setImageCaptureButton() {
        val bsAddImages = BottomSheetDialog(requireContext())
        binding.ivAddImg1.setOnClickListener {
            currentSelection = IMG_SELECTOR_1
            setBottomSheet(bsAddImages)
        }
        binding.ivAddImg2.setOnClickListener {
            currentSelection = IMG_SELECTOR_2
            setBottomSheet(bsAddImages)
        }
        binding.ivAddImg3.setOnClickListener {
            currentSelection = IMG_SELECTOR_3
            setBottomSheet(bsAddImages)
        }
        binding.ivAddImg4.setOnClickListener {
            currentSelection = IMG_SELECTOR_4
            setBottomSheet(bsAddImages)
        }
        binding.ivAddImg5.setOnClickListener {
            currentSelection = IMG_SELECTOR_5
            setBottomSheet(bsAddImages)
        }

        viewModel.image1.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.ivAddImg1.setImageBitmap(getImageFromExternalStorage(it))
            }
        }
        viewModel.image2.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.ivAddImg2.setImageBitmap(getImageFromExternalStorage(it))
            }
        }
        viewModel.image3.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.ivAddImg3.setImageBitmap(getImageFromExternalStorage(it))
            }
        }
        viewModel.image4.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.ivAddImg4.setImageBitmap(getImageFromExternalStorage(it))
            }
        }
        viewModel.image5.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.ivAddImg5.setImageBitmap(getImageFromExternalStorage(it))
            }
        }
    }

    private fun setBottomSheet(dialog: BottomSheetDialog) {
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_add_images, null)
        _bottomSheetBinding = BottomSheetAddImagesBinding.bind(bottomSheetView)
        dialog.setCancelable(false)
        dialog.setContentView(bottomSheetView)

        bottomSheetBinding.ivImgPreview.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST)
            }
        }

        bottomSheetBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        bottomSheetBinding.btnSave.setOnClickListener {
            if (imageCaptured != null) {
                val imgSavedPath = saveImageToExternalStorage(requireContext(), imageCaptured)
                viewModel.saveImage(imgSavedPath, currentSelection)
            }
        }

        dialog.show()
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 101
        const val IMG_SELECTOR_1 = 1
        const val IMG_SELECTOR_2 = 2
        const val IMG_SELECTOR_3 = 3
        const val IMG_SELECTOR_4 = 4
        const val IMG_SELECTOR_5 = 5
    }
}