package com.agungtriu.ecommerce.ui.profile

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.remote.model.request.RequestProfile
import com.agungtriu.ecommerce.core.remote.model.response.DataProfile
import com.agungtriu.ecommerce.databinding.FragmentProfileBinding
import com.agungtriu.ecommerce.helper.Extension.setColor
import com.agungtriu.ecommerce.helper.PhotoUriManager
import com.agungtriu.ecommerce.helper.Utils.closeSoftKeyboard
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.base.BaseFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private lateinit var galleryResultLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerActivityResult()
        observeData()
        listener()

        binding.tvProfileAgreement.setColor(
            getString(R.string.all_terms_conditions),
            MaterialColors.getColor(requireView(), com.google.android.material.R.attr.colorPrimary)
        )

        binding.tvProfileAgreement.setColor(
            getString(R.string.all_privacy_policy),
            MaterialColors.getColor(requireView(), com.google.android.material.R.attr.colorPrimary)
        )
    }

    private var imageUri: Uri? = null

    private fun registerActivityResult() {
        takePictureLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { isSuccess ->
            if (isSuccess) {
                imageUri?.let {
                    Glide.with(requireContext()).load(it)
                        .apply(RequestOptions.circleCropTransform()).into(binding.ivProfile)
                }
            }
        }

        galleryResultLauncher = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            if (uri != null) {
                imageUri = uri
                imageUri?.let {
                    Glide.with(requireContext()).load(it)
                        .apply(RequestOptions.circleCropTransform()).into(binding.ivProfile)
                }
            }
        }
    }

    private fun listener() {
        binding.ivProfile.setOnClickListener {
            analytics.logEvent("btn_profile_image", null)
            val items = arrayOf(
                getString(R.string.dialog_profile_camera),
                getString(R.string.dialog_profile_gallery)
            )
            val dialogProfile =
                MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                    .setTitle(R.string.dialog_title)
                    .setBackground(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.all_rectangle
                        )
                    )
                    .setItems(items) { dialog, which ->
                        when (items[which]) {
                            getString(R.string.dialog_profile_gallery) -> {
                                openGallery()
                                dialog.dismiss()
                            }

                            getString(R.string.dialog_profile_camera) -> {
                                openCamera()
                                dialog.dismiss()
                            }
                        }
                    }
            dialogProfile.show()
        }

        binding.btnProfileFinish.setOnClickListener {
            analytics.logEvent("btn_profile_finish", null)
            closeSoftKeyboard(binding.tietProfileName, requireContext())

            val textBody =
                binding.tietProfileName.text.toString().toRequestBody("text/plain".toMediaType())
            val userNamePart = MultipartBody.Part.createFormData("userName", null, textBody)

            val file = imageUri?.let { uri -> PhotoUriManager.uriToFile(requireContext(), uri) }
            val fileBody = file?.asRequestBody("image/*".toMediaType())
            val imagePart =
                fileBody?.let { requestBody ->
                    MultipartBody.Part.createFormData(
                        "userImage",
                        file.name,
                        requestBody
                    )
                }
            viewModel.postProfile(
                RequestProfile(userName = userNamePart, userImage = imagePart)
            )
        }
        binding.tietProfileName.addTextChangedListener {
            binding.btnProfileFinish.isEnabled = it?.length!! > 0
        }
    }

    private fun openCamera() {
        imageUri = PhotoUriManager.buildNewUri(requireContext())
        takePictureLauncher.launch(imageUri)
    }

    private fun openGallery() {
        galleryResultLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun observeData() {
        viewModel.resultRegisterProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {
                    binding.pbProfile.visibility = View.VISIBLE
                    binding.btnProfileFinish.visibility = View.INVISIBLE
                }

                is ViewState.Success<DataProfile> -> {
                    binding.pbProfile.visibility = View.GONE
                    binding.btnProfileFinish.visibility = View.VISIBLE
                    findNavController().navigate(R.id.action_profileFragment_to_mainFragment)
                }

                is ViewState.Error -> {
                    binding.pbProfile.visibility = View.GONE
                    binding.btnProfileFinish.visibility = View.VISIBLE
                    Snackbar.make(requireView(), it.error.message ?: "", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}
