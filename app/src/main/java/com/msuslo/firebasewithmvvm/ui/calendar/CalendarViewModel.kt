package com.msuslo.firebasewithmvvm.ui.calendar

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class CalendarViewModel @Inject constructor(): ViewModel(){

    var selectDate: LocalDate = LocalDate.now()

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
                daysInMonthArray.add((i - dayOfWeek).toString());
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