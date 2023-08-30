package com.compfest.aiapplication.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
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
import com.compfest.aiapplication.ml.ScalpModel
import com.compfest.aiapplication.model.AddViewModel
import com.compfest.aiapplication.saveImage
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.schema.Model
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Byte
import java.lang.Float
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    private val binding get() = _binding!!
    private var _bottomSheetBinding: BottomSheetAddImagesBinding? = null
    private val bottomSheetBinding get() = _bottomSheetBinding!!
    private val viewModel: AddViewModel by activityViewModels()
    private var param1: String? = null
    private var param2: String? = null
    private var thisFragmentName: String? = null
    private var currentSelection: Int? = null
    private var imgBitmap: Bitmap? = null
    private var interpreter: Interpreter? = null

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
            tryingMLImage(imageBitmap = imgBitmap as Bitmap)
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


        /*
        val conditions = CustomModelDownloadConditions.Builder()
            .requireWifi()  // Also possible: .requireCharging() and .requireDeviceIdle()
            .build()
        FirebaseModelDownloader.getInstance()
            .getModel("scalp_model", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND,
                conditions)
            .addOnSuccessListener { model: CustomModel? ->
                // Download complete. Depending on your app, you could enable the ML
                // feature, or switch from the local model to the remote model, etc.
                Log.d("FragmentThree", "Model has been downloaded")
                // The CustomModel object contains the local path of the model file,
                // which you can use to instantiate a TensorFlow Lite interpreter.
                val modelFile = model?.file
                if (modelFile != null) {
                    interpreter = Interpreter(modelFile)
                }
            } */
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

    /*
    private fun setSelectImageToCapture() {
        bottomSheetBinding.apply {
            ivAddImg1.setOnClickListener {

            }
            ivAddImg2.setOnClickListener {

            }
            ivAddImg3.setOnClickListener {

            }
            ivAddImg4.setOnClickListener {

            }
            ivAddImg5.setOnClickListener {

            }
        }
    }*/

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

                    // Simpan path gambar ke Room database
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
                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                return bitmap
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun tryingMLImage(imageBitmap: Bitmap) {
        val model = ScalpModel.newInstance(requireContext())

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 150, 150, 3), DataType.FLOAT32)
        val bitmap = Bitmap.createScaledBitmap(imageBitmap, 150, 150, true)
        val tensorImage = TensorImage(DataType.FLOAT32)
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
        /*
        val input = ByteBuffer.allocateDirect(150*150*3*4).order(ByteOrder.nativeOrder())
        for (y in 0 until 150) {
            for (x in 0 until 150) {
                val px = bitmap.getPixel(x, y)

                // Get channel values from the pixel value.
                val r = Color.red(px)
                val g = Color.green(px)
                val b = Color.blue(px)

                // Normalize channel values to [-1.0, 1.0]. This requirement depends on the model.
                // For example, some models might require values to be normalized to the range
                // [0.0, 1.0] instead.
                val rf = (r - 127) / 255f
                val gf = (g - 127) / 255f
                val bf = (b - 127) / 255f

                input.putFloat(rf)
                input.putFloat(gf)
                input.putFloat(bf)
            }
        }


        val bufferSize = 5 * Float.SIZE / Byte.SIZE
        val modelOutput = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())

        inputFeature0.loadBuffer(modelOutput)
        val outputs = model.process(inputFeature0)

        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        model.close()

        val result = outputFeature0.intArray
        Log.d("Model Try", result.toString())
        */
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
        const val IMG_SELECTOR_1 = 1
        const val IMG_SELECTOR_2 = 2
        const val IMG_SELECTOR_3 = 3
        const val IMG_SELECTOR_4 = 4
        const val IMG_SELECTOR_5 = 5
    }
}