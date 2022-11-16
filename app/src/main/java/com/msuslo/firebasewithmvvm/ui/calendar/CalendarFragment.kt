package com.msuslo.firebasewithmvvm.ui.calendar

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.msuslo.firebasewithmvvm.databinding.FragmentCalendarBinding
import com.msuslo.firebasewithmvvm.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class CalendarFragment : Fragment()  {

    val TAG: String = "CalendarFragment"
    private var param1: String? = null
    val viewModel: CalendarViewModel by viewModels()
    val authViewModel: AuthViewModel by viewModels()
    lateinit var binding: FragmentCalendarBinding
    val adapter by lazy {
        CalendarAdapter(
            onItemClicked = { pos, item -> onDayClicked(pos, item)}
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
        if (this::binding.isInitialized){
            return binding.root
        }else {
            binding = FragmentCalendarBinding.inflate(layoutInflater)
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.prevMonth.setOnClickListener {
            viewModel.previousMonthAction(it)
            setMonthView()
        }

        binding.nextMonth.setOnClickListener {
            viewModel.nextMonthAction(it)
            setMonthView()
        }
        setMonthView()
    }

    private fun setMonthView(){
        binding.monthYear.setText(viewModel.monthYearFromDate(viewModel.selectDate))
        val daysInMonth: ArrayList<String> = viewModel.daysInMonthArray(viewModel.selectDate)
        adapter.updateList(daysInMonth)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 7)
        binding.calendarR.layoutManager = layoutManager
        binding.calendarR.adapter = adapter
    }

    private fun onDayClicked(pos: Int, item: String) {
        if(!item.equals("")){
            val message: String = item + viewModel.monthYearFromDate(viewModel.selectDate)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

}