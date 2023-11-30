package com.agungtriu.ecommerce.ui.main.store.filter

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.os.bundleOf
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.databinding.BottomSheetFilterBinding
import com.agungtriu.ecommerce.helper.Extension.toRupiah
import com.agungtriu.ecommerce.helper.Sort
import com.agungtriu.ecommerce.ui.main.store.StoreFragment.Companion.TO_FILTER_KEY
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class FilterBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetFilterBinding? = null
    private val binding get() = _binding!!

    private lateinit var analytics: FirebaseAnalytics

    private var filterModel: FilterModel = FilterModel()
    private var sortStatus = false
    private var categoryStatus = false
    private var minPriceStatus = false
    private var maxPriceStatus = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val contextThemeWrapper = ContextThemeWrapper(requireActivity(), R.style.Theme_Ecommerce)
        _binding = BottomSheetFilterBinding.inflate(
            inflater.cloneInContext(contextThemeWrapper),
            container,
            false
        )

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getArguments(arguments)
    }

    private fun getArguments(arguments: Bundle?) {
        filterModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(TO_FILTER_KEY, FilterModel::class.java)
        } else {
            arguments?.getParcelable(TO_FILTER_KEY)
        } ?: FilterModel()

        if (filterModel.sort != null) {
            binding.chipgroupBottomshettfilterSort.forEach { chip ->
                if ((chip as Chip).text == Sort.valueOf(filterModel.sort!!).en || (chip as Chip).text == Sort.valueOf(filterModel.sort!!).id) {
                    sortStatus = true
                    chip.isChecked = true
                }
            }
        }

        if (filterModel.category != null) {
            binding.chipgroupBottomshettfilterCategory.forEach { chip ->
                if ((chip as Chip).text == filterModel.category) {
                    categoryStatus = true
                    chip.isChecked = true
                }
            }
        }

        if (filterModel.min != null) {
            binding.tietBottomsheetfilterMin.setText(filterModel.min.toString())
            minPriceStatus = true
        }

        if (filterModel.max != null) {
            binding.tietBottomsheetfilterMax.setText(filterModel.max.toString())
            maxPriceStatus = true
        }

        statusReset(sortStatus, categoryStatus, minPriceStatus, maxPriceStatus)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val modalBottomSheetBehavior = (dialog as BottomSheetDialog).behavior
        modalBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        clickListener()
    }

    private fun clickListener() {
        binding.chipgroupBottomshettfilterSort.setOnCheckedStateChangeListener { group, _ ->
            if (group.checkedChipId == -1) {
                sortStatus = false
                filterModel.sort = null
            } else {
                sortStatus = true
                val selectedChip = group.findViewById<Chip>(group.checkedChipId)
                Sort.entries.forEach {
                    if (selectedChip.text.toString() == it.en || selectedChip.text.toString() == it.id) {
                        filterModel.sort = it.name
                    }
                }
            }
            statusReset(sortStatus, categoryStatus, minPriceStatus, maxPriceStatus)
        }
        binding.chipgroupBottomshettfilterCategory.setOnCheckedStateChangeListener { group, _ ->
            if (group.checkedChipId == -1) {
                categoryStatus = false
                filterModel.category = null
            } else {
                categoryStatus = true
                val selectedChip = group.findViewById<Chip>(group.checkedChipId)
                filterModel.category = selectedChip.text.toString()
            }
            statusReset(sortStatus, categoryStatus, minPriceStatus, maxPriceStatus)
        }

        binding.tietBottomsheetfilterMin.addTextChangedListener {
            minPriceStatus = it?.isNotEmpty() ?: false
            try {
                if (it?.isNotEmpty() == true) {
                    filterModel.min = it.toString().toInt()
                }
            } catch (e: NumberFormatException) {
                binding.tietBottomsheetfilterMin.setText(filterModel.min.toString())
                binding.tietBottomsheetfilterMin.setSelection(filterModel.min.toString().length)
                binding.tietBottomsheetfilterMin.error =
                    getString(R.string.all_max).plus(Int.MAX_VALUE.toRupiah())
            }
            statusReset(sortStatus, categoryStatus, minPriceStatus, maxPriceStatus)
        }

        binding.tietBottomsheetfilterMax.addTextChangedListener {
            maxPriceStatus = it?.isNotEmpty() ?: false
            try {
                if (it?.isNotEmpty() == true) {
                    filterModel.max = it.toString().toInt()
                }
            } catch (e: NumberFormatException) {
                binding.tietBottomsheetfilterMax.setText(filterModel.max.toString())
                binding.tietBottomsheetfilterMax.setSelection(filterModel.max.toString().length)
                binding.tietBottomsheetfilterMax.error =
                    getString(R.string.all_max).plus(Int.MAX_VALUE.toRupiah())
            }
            statusReset(sortStatus, categoryStatus, minPriceStatus, maxPriceStatus)
        }

        binding.btnBottomsheetfilterReset.setOnClickListener {
            analytics.logEvent("btn_bottom_sheet_filter_reset", null)
            binding.chipgroupBottomshettfilterSort.clearCheck()
            binding.chipgroupBottomshettfilterCategory.clearCheck()
            binding.tietBottomsheetfilterMin.text = null
            binding.tietBottomsheetfilterMax.text = null

            filterModel = FilterModel()

            sortStatus = false
            categoryStatus = false
            minPriceStatus = false
            maxPriceStatus = false
            statusReset(
                sortStatus = false,
                categoryStatus = false,
                minPriceStatus = false,
                maxPriceStatus = false
            )
        }

        binding.btnBottomsheetfilterSubmit.setOnClickListener {
            analytics.logEvent("btn_bottom_sheet_filter_submit", null)
            setFragmentResult(FILTER_KEY, bundleOf(RESULT_FILTER_KEY to filterModel))
            dismiss()
        }

        binding.tietBottomsheetfilterMax.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                setFragmentResult(FILTER_KEY, bundleOf(RESULT_FILTER_KEY to filterModel))
                dismiss()
                return@setOnKeyListener true
            }
            false
        }
    }

    private fun statusReset(
        sortStatus: Boolean,
        categoryStatus: Boolean,
        minPriceStatus: Boolean,
        maxPriceStatus: Boolean
    ) {
        binding.btnBottomsheetfilterReset.isVisible =
            sortStatus || categoryStatus || minPriceStatus || maxPriceStatus
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ModalFilterBottomSheet"
        const val FILTER_KEY = "RequestKey"
        const val RESULT_FILTER_KEY = "BundleKey"
    }
}
