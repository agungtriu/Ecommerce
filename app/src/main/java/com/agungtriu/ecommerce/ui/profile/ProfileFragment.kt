package com.agungtriu.ecommerce.ui.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.remote.model.request.RequestProfile
import com.agungtriu.ecommerce.core.remote.model.response.DataProfile
import com.agungtriu.ecommerce.databinding.FragmentProfileBinding
import com.agungtriu.ecommerce.helper.Extension.makeLinks
import com.agungtriu.ecommerce.helper.PhotoUriManager
import com.agungtriu.ecommerce.helper.Utils.closeSoftKeyboard
import com.agungtriu.ecommerce.helper.ViewState
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var galleryResultLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private var nameState = false
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerActivityResult()
        listener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            val items = arrayOf(
                getString(R.string.dialog_profile_camera),
                getString(R.string.dialog_profile_gallery)
            )
            val dialogProfile = MaterialAlertDialogBuilder(requireContext())
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
            closeSoftKeyboard(binding.tietProfileName, requireContext())
            observeData()

            val userNamePart =
                binding.tietProfileName.text.toString().toRequestBody(MultipartBody.FORM)

            val file = imageUri?.let { uri -> PhotoUriManager.uriToFile(requireContext(), uri) }
            val fileBody = file?.asRequestBody("image/*".toMediaType())
            val imagePart =
                fileBody?.let { requestBody ->
                    MultipartBody.Part.createFormData(
                        "userImage", file.name,
                        requestBody
                    )
                }
            viewModel.registerProfile(
                RequestProfile(userName = userNamePart, userImage = imagePart)
            )

        }
        binding.tvProfileAgreement.makeLinks(
            Pair(getString(R.string.all_terms_conditions), View.OnClickListener {
                Snackbar.make(
                    it.rootView,
                    getString(R.string.all_coming_soon),
                    Snackbar.LENGTH_LONG
                ).show()
            }),
            Pair(getString(R.string.all_privacy_policy), View.OnClickListener {
                Snackbar.make(
                    it.rootView,
                    getString(R.string.all_coming_soon),
                    Snackbar.LENGTH_LONG
                ).show()
            })
        )
        binding.tietProfileName.addTextChangedListener {
            nameState = it?.length!! >= 1
            buttonValidation(nameState = nameState)
        }
    }

    private fun openCamera() {
        imageUri = PhotoUriManager.buildNewUri(requireContext())
        takePictureLauncher.launch(imageUri)
    }

    private fun openGallery() {
        galleryResultLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun buttonValidation(nameState: Boolean) {
        binding.btnProfileFinish.isEnabled = nameState
    }

    private fun observeData() {
        viewModel.resultRegisterProfile.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {
                    binding.pbProfile.visibility = View.VISIBLE
                }

                is ViewState.Success<DataProfile> -> {
                    binding.pbProfile.visibility = View.GONE
                    findNavController().navigate(R.id.action_profileFragment_to_mainFragment)
                }

                is ViewState.Error -> {
                    binding.pbProfile.visibility = View.GONE
                    Snackbar.make(requireView(), it.message, Snackbar.LENGTH_LONG)
                        .show()

                }
            }
        }
    }

}