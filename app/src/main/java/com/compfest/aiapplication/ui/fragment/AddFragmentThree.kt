package com.compfest.aiapplication.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.compfest.aiapplication.databinding.BottomSheetAddImagesBinding
import com.compfest.aiapplication.databinding.FragmentAddThreeBinding
import com.compfest.aiapplication.ml.HairlossModel
import com.compfest.aiapplication.ml.ScalpModel
import com.compfest.aiapplication.model.AddViewModel
import com.compfest.aiapplication.ui.activity.ResultActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddFragmentThree : Fragment() {
    private var _binding: FragmentAddThreeBinding? = null
    private val binding get() = _binding!!
    private var _bottomSheetBinding: BottomSheetAddImagesBinding? = null
    private val bottomSheetBinding get() = _bottomSheetBinding!!
    private val viewModel: AddViewModel by activityViewModels()
    private var thisFragmentName: String? = null
    private var currentSelection: Int? = null
    private var imgBitmap: Bitmap? = null

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

        val btnShowBottomSheet1 = binding.ivAddImg1
        val btnShowBottomSheet2 = binding.ivAddImg2
        val btnShowBottomSheet3 = binding.ivAddImg3
        val btnShowBottomSheet4 = binding.ivAddImg4
        val btnShowBottomSheet5 = binding.ivAddImg5
        val bsAddImages = BottomSheetDialog(requireContext())
        btnShowBottomSheet1.setOnClickListener {
            currentSelection = IMG_SELECTOR_1
            setBottomSheet(bsAddImages)
        }
        btnShowBottomSheet2.setOnClickListener {
            currentSelection = IMG_SELECTOR_2
            setBottomSheet(bsAddImages)
        }
        btnShowBottomSheet3.setOnClickListener {
            currentSelection = IMG_SELECTOR_3
            setBottomSheet(bsAddImages)
        }
        btnShowBottomSheet4.setOnClickListener {
            currentSelection = IMG_SELECTOR_4
            setBottomSheet(bsAddImages)
        }
        btnShowBottomSheet5.setOnClickListener {
            currentSelection = IMG_SELECTOR_5
            setBottomSheet(bsAddImages)
        }

        val btnSubmitAll = binding.btnSubmitAll
        btnSubmitAll.setOnClickListener {
            //tryingMLImage(imageBitmap = imgBitmap as Bitmap)
            //tryingML()
            val intent = Intent(requireContext(), ResultActivity::class.java)
            intent.putExtra(ResultActivity.EXTRA_PREDICTION_INPUT, viewModel.getParcelableInputData())
            startActivity(intent)
            requireActivity().finish()
        }

        viewModel.image1.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.ivAddImg1.setImageBitmap(getImageFromExternalStorage(it))
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.image2.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.ivAddImg2.setImageBitmap(getImageFromExternalStorage(it))
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.image3.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.ivAddImg3.setImageBitmap(getImageFromExternalStorage(it))
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.image4.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.ivAddImg4.setImageBitmap(getImageFromExternalStorage(it))
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.image5.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.ivAddImg5.setImageBitmap(getImageFromExternalStorage(it))
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
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
            if (imgBitmap != null) {
                val imgSavedPath = saveImageToExternalStorage(imgBitmap)
                viewModel.saveImage(imgSavedPath, currentSelection)
            }
        }

        dialog.show()
    }

    private fun saveImageToExternalStorage(capturedImage: Bitmap?): String? {
        capturedImage?.let { bitmap ->
            val externalDir = requireContext().getExternalFilesDir(null)

            if (externalDir != null) {
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val imageFileName = "captured_image_$timestamp.jpg"
                val imageFile = File(externalDir, imageFileName)
                try {
                    val fos = FileOutputStream(imageFile)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    fos.close()

                    val imagePath = imageFile.absolutePath

                    Toast.makeText(requireContext(), "Image saved", Toast.LENGTH_SHORT).show()
                    return imagePath
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Failed to save image", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "External storage not available",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } ?: run {
            Toast.makeText(requireContext(), "No image to save", Toast.LENGTH_SHORT).show()
        }

        return null
    }

    private fun getImageFromExternalStorage(imagePath: String): Bitmap? {
        try {
            val imageFile = File(imagePath)
            if (imageFile.exists()) {
                return BitmapFactory.decodeFile(imageFile.absolutePath)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun tryingMLImage(imageBitmap: Bitmap) {
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
        model.close()

        for (value in outputArray) {
            Log.d("TestModel", value.toString())
        }
    }

    private fun tryingML() {
        val model = HairlossModel.newInstance(requireContext())

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 8), DataType.FLOAT32)
        val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * 8)
        byteBuffer.order(ByteOrder.nativeOrder())
        val payload = viewModel.getInputData()
        payload.values.forEach { value ->
            byteBuffer.putFloat(value)
        }
        byteBuffer.rewind()
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        model.close()

        println("Output:")
        for (i in 0 until outputFeature0.typeSize) {
            Log.d("ModelML","Class $i: ${outputFeature0.getFloatValue(i)}")
        }
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