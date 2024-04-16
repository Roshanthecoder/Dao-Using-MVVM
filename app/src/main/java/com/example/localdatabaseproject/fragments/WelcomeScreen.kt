package com.example.localdatabaseproject.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.localdatabaseproject.R
import com.example.localdatabaseproject.databinding.DialogAddproductBinding
import com.example.localdatabaseproject.databinding.FragmentWelcomeScreenBinding
import com.example.localdatabaseproject.models.ProductList
import com.example.localdatabaseproject.repository.RoomRepo
import com.example.localdatabaseproject.roomdb.AppDatabase
import com.example.localdatabaseproject.viewmodel.RoomViewModel
import com.example.localdatabaseproject.viewmodelfactory.RoomViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class WelcomeScreen : Fragment(R.layout.fragment_welcome_screen) {
    private var _binding: FragmentWelcomeScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var model: RoomViewModel
    private var dialBinding: DialogAddproductBinding? = null
    private var picURI: Uri? = null
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private val roomRepo by lazy { RoomRepo(AppDatabase.getInstance(requireContext()).userDao()) }
    private val viewModelFactory by lazy { RoomViewModelFactory(roomRepo) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWelcomeScreenBinding.bind(view)
        model = ViewModelProvider(this, viewModelFactory)[RoomViewModel::class.java]
        initObserver()
        initListeners()
        registerPermissionLauncher()
        pickImageResult()
    }

    private fun pickImageResult() {
        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let { imageUri ->
                        lifecycleScope.launch {
                            val imageBitmap = withContext(Dispatchers.IO) {
                                context?.contentResolver?.openInputStream(imageUri)
                                    ?.use { inputStream ->
                                        BitmapFactory.decodeStream(inputStream)
                                    }
                            }
                            val resizedBitmap = resizeImage(imageBitmap!!)
                            picURI = saveImageToInternalStorage(resizedBitmap)
                            dialBinding?.productImageView?.setImageBitmap(resizedBitmap)
                        }
                    }
                }
            }
    }

    private fun resizeImage(bitmap: Bitmap): Bitmap {
        // Resize the bitmap to a smaller size
        return Bitmap.createScaledBitmap(bitmap, 200, 200, true)
    }


    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
        val wrapper = ContextWrapper(context)
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "${System.currentTimeMillis()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)

            // Check if the bitmap is recycled before using it
            if (!bitmap.isRecycled) {
                // Resize and compress the bitmap before saving
                val resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

                stream.flush()
                stream.close()

                // Recycle the resized bitmap to release memory
                resizedBitmap.recycle()
            } else {
                // Handle the case where the bitmap is recycled
                // For example, log an error or load a new bitmap
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Uri.fromFile(file)
    }

    private fun dialogShow(binding: DialogAddproductBinding) {
        dialBinding = binding
        val dialog = Dialog(binding.root.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.root)
        val productNameEditText = binding.productNameEditText
        val quantityEditText = binding.quantityEditText
        val rateEditText = binding.rateEditText
        val okButton = binding.dialogOkButton
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        okButton.setOnClickListener {
            val productName = productNameEditText.text.toString()
            val quantity = quantityEditText.text.toString()
            val rate = rateEditText.text.toString()
            if (productName.isEmpty() || quantity.isEmpty() || rate.isEmpty()) {
                // Handle empty fields here, for example show a toast message
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Proceed with your logic if all fields are filled
                // Dismiss the dialog
                model.insertProduct(
                    ProductList(
                        0,
                        productName,
                        quantity.toInt(),
                        rate.toInt(),
                        picURI?.toString()
                    )
                ) {
                    if (it) dialog.dismiss()
                }
            }
        }

        binding.productImageView.setOnClickListener {
            openGallery()
        }

        // Optional: Add TextChangedListener to enable/disable okButton based on editText values
        productNameEditText.addTextChangedListener {
            okButton.isEnabled =
                !it.isNullOrBlank() && !quantityEditText.text.isNullOrBlank() && !rateEditText.text.isNullOrBlank()
        }
        quantityEditText.addTextChangedListener {
            okButton.isEnabled =
                !it.isNullOrBlank() && !productNameEditText.text.isNullOrBlank() && !rateEditText.text.isNullOrBlank()
        }
        rateEditText.addTextChangedListener {
            okButton.isEnabled =
                !it.isNullOrBlank() && !productNameEditText.text.isNullOrBlank() && !quantityEditText.text.isNullOrBlank()
        }
        dialog.show()
    }

    private fun registerPermissionLauncher() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Permission is granted
                openGallery()
            } else {
                val intent = Intent().apply {
                    action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", requireActivity().packageName, null)
                }
                requireActivity().startActivity(intent)
            }
        }
    }

    private fun openGallery() {
        val pickPhotoIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(pickPhotoIntent)
    }

    private fun initListeners() {
        binding.btnShowAllUser.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeScreen_to_userList)
        }
        binding.btnGoToShopping.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeScreen_to_productPage)
        }
        binding.btnAddProduct.setOnClickListener {
            permissionCheck()
        }
    }

    private fun permissionCheck() {
        val permission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) android.Manifest.permission.READ_MEDIA_IMAGES
            else android.Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(
                requireContext(), permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(permission)
        } else {
            dialogShow(DialogAddproductBinding.inflate(layoutInflater))
        }
    }

    private fun initObserver() {
        requireArguments().getString("user")?.let {
            binding.textView.text = "Hii , $it"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
