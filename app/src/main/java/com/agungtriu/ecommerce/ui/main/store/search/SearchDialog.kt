package com.agungtriu.ecommerce.ui.main.store.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.databinding.DialogSearchBinding
import com.agungtriu.ecommerce.helper.ViewState
import com.agungtriu.ecommerce.ui.main.store.StoreFragment.Companion.TO_SEARCH_KEY
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchDialog : DialogFragment() {

    private var _binding: DialogSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSearchBinding.inflate(inflater, container, false)
        getArguments(arguments)
        return binding.root
    }

    private fun getArguments(arguments: Bundle?) {
        val query = arguments?.getString(TO_SEARCH_KEY)
        binding.tietBottomsheetsearch.setText(query)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SearchAdapter()
        binding.rvBottomsheetsearch.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBottomsheetsearch.adapter = adapter

        binding.tietBottomsheetsearch.requestFocus()
        binding.tietBottomsheetsearch.addTextChangedListener {
            viewModel.searchDebounced(it.toString())
        }

        binding.tietBottomsheetsearch.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                setFragmentResult(
                    SEARCH_KEY,
                    bundleOf(RESULT_SEARCH_KEY to binding.tietBottomsheetsearch.text.toString())
                )
                dismiss()
                return@setOnKeyListener true
            }
            false
        }
        observeData()
    }

    private fun observeData() {
        viewModel.result.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> binding.pbSearch.visibility = View.VISIBLE
                is ViewState.Error -> {
                    binding.pbSearch.visibility = View.GONE
                    Snackbar.make(requireView(), it.error.message.toString(), Snackbar.LENGTH_LONG)
                        .show()
                }

                is ViewState.Success -> {
                    binding.pbSearch.visibility = View.GONE
                    adapter.submitList(it.data)
                }
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ModalDialogSearch"
        const val SEARCH_KEY = "SearchKey"
        const val RESULT_SEARCH_KEY = "ResultSearchKey"
    }
}
