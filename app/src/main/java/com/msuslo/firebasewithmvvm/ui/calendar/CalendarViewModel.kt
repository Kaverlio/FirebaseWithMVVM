package com.msuslo.firebasewithmvvm.ui.calendar

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.msuslo.firebasewithmvvm.data.model.Queue
import com.msuslo.firebasewithmvvm.data.model.User
import com.msuslo.firebasewithmvvm.data.repository.IQueueRepository
import com.msuslo.firebasewithmvvm.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class CalendarViewModel @Inject constructor(val repository: IQueueRepository): ViewModel(){

    private val _queue = MutableLiveData<UiState<List<Queue>>>()
    val queue: LiveData<UiState<List<Queue>>>
        get() = _queue

    private val _addQueue = MutableLiveData<UiState<Pair<Queue, String>>>()
    val addQueue: LiveData<UiState<Pair<Queue, String>>>
        get() = _addQueue

    private val _updateQueue = MutableLiveData<UiState<Pair<Queue, String>>>()
    val updateQueue: LiveData<UiState<Pair<Queue, String>>>
        get() = _updateQueue

    private val _deleteQueue = MutableLiveData<UiState<Pair<Queue, String>>>()
    val deleteQueue: LiveData<UiState<Pair<Queue, String>>>
        get() = _deleteQueue

    var selectDate: LocalDate = LocalDate.now()

    fun addQueue(queue: Queue){
        _addQueue.value = UiState.Loading
        repository.addRecord(queue) { _addQueue.value = it }
    }

    fun updateQueue(queue: Queue){
        _updateQueue.value = UiState.Loading
        repository.updateRecord(queue) { _updateQueue.value = it }
    }

    fun getQueuesForDentist(user: User?){
        _queue.value = UiState.Loading
        repository.getRecordForDentist(user) { _queue.value = it }
    }

    fun getQueues(){
        _queue.value = UiState.Loading
        repository.getRecord(null) { _queue.value = it }
    }

    fun getQueuesByDay(day: String){
        _queue.value = UiState.Loading
        repository.getQueuesByDay(day) { _queue.value = it }
    }

    fun deleteQueue(queue: Queue) {
        _deleteQueue.value = UiState.Loading
        repository.deleteRecord(queue) { _deleteQueue.value = it }
    }

    fun monthYearFromDate(date: LocalDate): String{
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    fun daysInMonthArray(date: LocalDate): ArrayList<String>{
        val daysInMonthArray = ArrayList<String>()
        val yearMonth: YearMonth = YearMonth.from(date)

        val daysInMonth: Int = yearMonth.lengthOfMonth()

        val firsOfMonth: LocalDate = selectDate.withDayOfMonth(1)
        val dayOfWeek = firsOfMonth.dayOfWeek.value

        for(i in 1..42)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                val day = i - dayOfWeek
                if (day < 10)
                    daysInMonthArray.add("0$day")
                else
                    daysInMonthArray.add(day.toString())
            }
        }
        return  daysInMonthArray
    }

    fun previousMonthAction(view: View)
    {
        selectDate = selectDate.minusMonths(1);
    }

    fun nextMonthAction(view: View)
    {
        selectDate = selectDate.plusMonths(1);
    }
}