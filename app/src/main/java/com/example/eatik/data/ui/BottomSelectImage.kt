package com.example.eatik.data.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.eatik.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import java.io.FileOutputStream

class BottomSelectImage(private val onImageSelected: (Uri) -> Unit)
    : BottomSheetDialogFragment() {

    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Void?>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottom_select_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardCamera = view.findViewById<CardView>(R.id.cardCamera)
        val cardGallery = view.findViewById<CardView>(R.id.cardGallery)

        // animasi tekan
        setPressAnimation(cardCamera)
        setPressAnimation(cardGallery)

        // launcher gallery
        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if (uri != null) {
                    onImageSelected(uri)
                    dismiss()
                } else {
                    Toast.makeText(requireContext(), "Tidak ada gambar dipilih", Toast.LENGTH_SHORT).show()
                }
            }

        // launcher kamera
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
                if (bitmap != null) {
                    val uri = bitmapToUri(bitmap)
                    if (uri != null) {
                        onImageSelected(uri)
                        dismiss()
                    } else {
                        Toast.makeText(requireContext(), "Gagal konversi foto", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal ambil foto", Toast.LENGTH_SHORT).show()
                }
            }

        // klik gallery
        cardGallery.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // klik kamera
        cardCamera.setOnClickListener {
            checkCameraPermissionAndLaunch()
        }
    }

    private fun setPressAnimation(view: View) {
        view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.startAnimation(
                        AnimationUtils.loadAnimation(v.context, R.anim.scale_in)
                    )
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    v.startAnimation(
                        AnimationUtils.loadAnimation(v.context, R.anim.scale_out)
                    )
                }
            }
            false
        }
    }

    private fun bitmapToUri(bitmap: Bitmap): Uri? {
        return try {
            val file = File(requireContext().cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }
            FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun checkCameraPermissionAndLaunch() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            cameraLauncher.launch(null)
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 1001)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1001 &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(requireContext(), "Camera permission diperlukan", Toast.LENGTH_SHORT).show()
        }
    }
}