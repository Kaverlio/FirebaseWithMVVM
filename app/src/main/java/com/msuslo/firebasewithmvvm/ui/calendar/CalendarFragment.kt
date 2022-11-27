package com.msuslo.firebasewithmvvm.ui.calendar

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.msuslo.firebasewithmvvm.R
import com.msuslo.firebasewithmvvm.data.model.Queue
import com.msuslo.firebasewithmvvm.databinding.FragmentCalendarBinding
import com.msuslo.firebasewithmvvm.ui.auth.AuthViewModel
import com.msuslo.firebasewithmvvm.ui.profile.ProfileViewModel
import com.msuslo.firebasewithmvvm.ui.scheduleday.ScheduleDayAdapter
import com.msuslo.firebasewithmvvm.ui.scheduleday.ScheduleDayFragment
import com.msuslo.firebasewithmvvm.utils.UiState
import com.msuslo.firebasewithmvvm.utils.hide
import com.msuslo.firebasewithmvvm.utils.show
import com.msuslo.firebasewithmvvm.utils.toast
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class CalendarFragment : Fragment() {

    val TAG: String = "CalendarFragment"
    private var param1: String? = null
    val viewModel: CalendarViewModel by viewModels()
    val viewModelProfile: ProfileViewModel by viewModels()
    val authViewModel: AuthViewModel by viewModels()
    lateinit var binding: FragmentCalendarBinding
    val adapterCalendar by lazy {
        CalendarAdapter(
            onItemClicked = { pos, item -> onDayClicked(pos, item) }
        )
    }
    val adapterRecords by lazy {
        ScheduleDayAdapter(
            onAddClicked = { pos, item -> onAddTimeClick(item) },
            onDeleteClicked = { pos, item -> onDeleteTimeClick(item) }
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (this::binding.isInitialized) {
            return binding.root
        } else {
            binding = FragmentCalendarBinding.inflate(layoutInflater)
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.getSession {
            if (it?.status == "Dentist") {
                viewModel.getQueuesForDentist(it)
                binding.sDoctor.isVisible = false
                adapterRecords.isDentist(true)
            } else{
                adapterRecords.isDentist(false)
                viewModel.getQueues()
            }
        }

        viewModelProfile.getUsersByStatus("Dentist")

        var doctors = mutableListOf<String>()

        viewModelProfile.users.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(it.error)
                }
                is UiState.Success -> {
                    val d = it.data.toMutableList()
                    doctors.clear()
                    d.forEach { user ->
                        doctors.add("${user.first_name} ${user.last_name}")
                    }
                }
            }

        }

        viewModel.queue.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    adapterRecords.updateList(state.data.toMutableList())
                }
            }
        }

        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapterRecords

        val adapterDropDown = ArrayAdapter(requireContext(), R.layout.dropdown_item, doctors)
        binding.sDoctor.adapter = adapterDropDown

        setMonthView()
        observer()
    }

    private fun observer() {
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.prevMonth.setOnClickListener {
            viewModel.previousMonthAction(it)
            setMonthView()
        }

        binding.nextMonth.setOnClickListener {
            viewModel.nextMonthAction(it)
            setMonthView()
        }
    }

    private fun setMonthView() {
        binding.monthYear.setText(viewModel.monthYearFromDate(viewModel.selectDate))
        adapterCalendar.updateList(viewModel.daysInMonthArray(viewModel.selectDate))
        binding.calendarR.layoutManager = GridLayoutManager(context, 7)
        binding.calendarR.adapter = adapterCalendar
    }

    private fun onDeleteTimeClick(item: Queue) {
        authViewModel.getSession {
            if (item.dentist_id == it?.id)
                viewModel.deleteQueue(item)
            if (item.patient_id == it?.id) {
                item.patient_id = ""
                viewModel.updateQueue(item)
            }
            viewModel.getQueuesForDentist(it)
        }

    }

    private fun onAddTimeClick(item: Queue) {
        authViewModel.getSession {
            if (item.patient_id.isEmpty()){
                item.patient_id = it?.id.toString()
                viewModel.updateQueue(item)
            }
        }
    }

    private fun onDayClicked(pos: Int, item: String) {
        val message = "$item ${viewModel.monthYearFromDate(viewModel.selectDate)}"
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        authViewModel.getSession {
            if(it?.status == "Dentist")
            ScheduleDayFragment().also {
                it.date = message
            }.show(childFragmentManager, "create")
        }
        viewModel.getQueuesByDay(message)
    }

}