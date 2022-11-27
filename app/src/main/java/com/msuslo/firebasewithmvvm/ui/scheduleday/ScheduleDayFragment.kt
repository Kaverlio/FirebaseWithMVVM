package com.msuslo.firebasewithmvvm.ui.scheduleday

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.msuslo.firebasewithmvvm.data.model.Queue
import com.msuslo.firebasewithmvvm.data.model.User
import com.msuslo.firebasewithmvvm.databinding.FragmentScheduleDayBinding
import com.msuslo.firebasewithmvvm.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import kotlin.time.Duration.Companion.minutes

private const val ARG_PARAM1 = "param1"

@RequiresApi(Build.VERSION_CODES.M)
@AndroidEntryPoint
class ScheduleDayFragment() : DialogFragment() {

    val TAG: String = "ScheduleDayFragment"
    private var param1: String? = null
    private var objUser: User? = null
    val viewModel: ScheduleDayViewModel by viewModels()
    val authViewModel: AuthViewModel by viewModels()
    var date: String = ""
    lateinit var binding: FragmentScheduleDayBinding


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
            binding = FragmentScheduleDayBinding.inflate(layoutInflater)
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.getSession {
            viewModel.getQueues(it)
            objUser = it
            binding.btnAdd.isVisible = it?.status == "Dentist"
        }
        binding.tvDate.text = date

        observer()

    }

    @SuppressLint("SimpleDateFormat")
    private fun getQueue(usr: User?): Queue{
        return Queue(
            id = "",
            date = date,
            time = "${binding.timePicker.hour}:${binding.timePicker.minute}",
            patient_id = "",
            dentist_id = usr?.id ?: ""
        )
    }

    private fun observer() {
        binding.btnAdd.setOnClickListener {
            viewModel.addQueue(getQueue(objUser))
        }

    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            ScheduleDayFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}