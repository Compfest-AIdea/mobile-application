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
import com.compfest.aiapplication.data.PredictionImageResult
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

        setImageCaptureDialog()
        setButton()
    }

    @Suppress("DEPRECATION")
    private fun startCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Tentukan ukuran gambar yang Anda inginkan (1:1)
        val targetSize = 448

        // Buat objek konfigurasi gambar untuk mengatur proporsi gambar yang diambil
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_background, options)

        // Hitung faktor skala untuk mengatur proporsi 1:1
        val scaleFactor = Math.min(options.outWidth / targetSize, options.outHeight / targetSize)

        // Atur properti tambahan untuk mengubah rasio gambar
        takePictureIntent.putExtra("outputX", targetSize * scaleFactor)
        takePictureIntent.putExtra("outputY", targetSize * scaleFactor)
        takePictureIntent.putExtra("aspectX", 1)
        takePictureIntent.putExtra("aspectY", 1)
        takePictureIntent.putExtra("scale", true)

        // Mulai kamera dengan intent yang telah dikonfigurasi
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

    private fun tryingMLImage(): PredictionImageResult? {

        val payload = viewModel.getImage()
        val imageBitmap = getImageFromExternalStorage(payload as String)
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

            val resultRaw = ArrayList<Float>()
            for (value in outputArray) {
                resultRaw.add(value)
                Log.d("ModelMLScalpCondi", value.toString())
            }
            model.close()

            val result = PredictionImageResult(
                class1 = resultRaw[0],
                class2 = resultRaw[1],
                class3 = resultRaw[2],
                class4 = resultRaw[3],
                class5 = resultRaw[4]
            )
            return result
        } else {
            Toast.makeText(requireContext(), "Sorry your images cannot be found", Toast.LENGTH_SHORT).show()
            return null
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
            Log.d("ModelMLHairLoss","Class $i: ${outputFeature0.getFloatValue(i)}")
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

    private fun setImageCaptureDialog() {
        val bsAddImages = BottomSheetDialog(requireContext())
        binding.ifbAddImages.setOnClickListener {
            setBottomSheet(bsAddImages)
        }

        viewModel.imgPath.observe(viewLifecycleOwner) {
            if (it != null) {
                val imgBitmap = getImageFromExternalStorage(it) as Bitmap
                binding.ifbAddImages.setImageBitmap(imgBitmap)
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
                viewModel.saveImage(imgSavedPath)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun setButton() {
        val btnSubmitAll = binding.btnSubmitAll
        btnSubmitAll.setOnClickListener {
            if (viewModel.getImage() != null) {
                val resultTabular = tryingMLTabular()
                val resultImage = tryingMLImage()
                val intent = Intent(requireContext(), ResultActivity::class.java)
                intent.apply {
                    putExtra("origin", AddFragmentThree::class.java.simpleName)
                    putExtra(ResultActivity.EXTRA_RESULT_TABULAR, resultTabular)
                    putExtra(ResultActivity.EXTRA_RESULT_IMAGE, resultImage)
                    putExtra(ResultActivity.EXTRA_PREDICTION_TABULAR_INPUT, viewModel.getParcelableTabularInputData())
                    putExtra(ResultActivity.EXTRA_PREDICTION_IMAGE_INPUT, viewModel.getParcelableImageInputData())
                }
                startActivity(intent)
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "Tolong, isi data gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 101
    }
}