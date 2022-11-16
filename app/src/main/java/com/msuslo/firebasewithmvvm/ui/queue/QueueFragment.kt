package com.msuslo.firebasewithmvvm.ui.queue

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.msuslo.firebasewithmvvm.R
import com.msuslo.firebasewithmvvm.data.model.Queue
import com.msuslo.firebasewithmvvm.databinding.FragmentQueueBinding
import com.msuslo.firebasewithmvvm.ui.auth.AuthViewModel
import com.msuslo.firebasewithmvvm.ui.calendar.CalendarFragment
import com.msuslo.firebasewithmvvm.utils.UiState
import com.msuslo.firebasewithmvvm.utils.hide
import com.msuslo.firebasewithmvvm.utils.show
import com.msuslo.firebasewithmvvm.utils.toast
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class QueueFragment : Fragment() {

    val TAG: String = "QueueFragment"
    private var param1: String? = null
    val viewModel: QueueViewModel by viewModels()
    val authViewModel: AuthViewModel by viewModels()
    lateinit var binding: FragmentQueueBinding
    var deleteItemPos = -1
    val adapter by lazy {
        QueueAdapter(
            onDeleteClicked = { pos, item -> onDeleteClicked(pos, item) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (this::binding.isInitialized) {
            return binding.root
        } else {
            binding = FragmentQueueBinding.inflate(layoutInflater)
            return binding.root
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addRecord.setOnClickListener {
            findNavController().navigate(R.id.action_queueFragment_to_calendarFragment)
//            val createCalendarFragmentSheet = CalendarFragment()
//            createCalendarFragmentSheet {
//                if (it) {
//                    authViewModel.getSession {
//                        viewModel.getRecord(it)
//                    }
//                }
//            }
//            createCalendarFragmentSheet.show(childFragmentManager)
        }

        binding.queueListing.layoutManager = LinearLayoutManager(requireContext())
        binding.queueListing.adapter = adapter

        authViewModel.getSession {
            viewModel.getRecord(it)
        }
        observer()
    }

    private fun observer(){
        viewModel.getRecord.observe(viewLifecycleOwner) {
            when(it){
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(it.error)
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    adapter.updateList(it.data.toMutableList())
                }
            }
        }
        viewModel.deleteRecord.observe(viewLifecycleOwner) {
            when(it){
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(it.error)
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    toast(it.data.second)
                    adapter.removeItem(deleteItemPos)
                }
            }
        }
    }

    private fun onDeleteClicked(pos: Int, item: Queue) {
        deleteItemPos = pos
        viewModel.deleteRecord(item)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            QueueFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}