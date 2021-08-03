package com.fitness.tracker.utilities

import java.time.LocalDate
import java.time.YearMonth

class CalendarLogic {

    def static calendarStartingMonthDays = [sunday: 1, monday: 2, tuesday: 3, wednesday:4, thursday: 5, friday: 6, saturday: 7]

    //days to display of this month
    static ArrayList<Integer> getDaysInMonthList(LocalDate date){
        int daysInMonth = date.lengthOfMonth();

        List<Integer> daysInMonthList = new ArrayList<Integer>()

        for (int i = 1; i <= daysInMonth; i++) {
            daysInMonthList.add(i)
        }

        return daysInMonthList
    }

    //days to display of last month
    static ArrayList<Integer> getDaysOfLastMonthList(LocalDate currentDate){
        Enum currentMonthStartingWeekday = currentDate.withDayOfMonth(1).getDayOfWeek()

        int daysOfLastMonth = currentDate.minusMonths(1).lengthOfMonth()
        int daysOfLastMonthToDisplay = calendarStartingMonthDays[currentMonthStartingWeekday.toString().toLowerCase()]
        List<Integer> daysOfLastMonthList = new ArrayList<Integer>()

        for (int i = daysOfLastMonth - daysOfLastMonthToDisplay; i <= daysOfLastMonth ; i++) {
            daysOfLastMonthList.add(i)
        }

        return daysOfLastMonthList
    }

    //days to display of next month
    static ArrayList<Integer> getDaysOfNextMonthList(LocalDate currentDate){
        int daysOfNextMonthToDisplay = 42 - getDaysOfLastMonthList(currentDate).size() - currentDate.lengthOfMonth() //totalCalendarSlots + daysOfLastMonthsShownOnCalendar + lengthOfCurrentMonth
        List<Integer> daysOfNextMonthList = new ArrayList<Integer>()

        for (int i = 1; i <= daysOfNextMonthToDisplay; i++) {
            daysOfNextMonthList.add(i)
        }

        return daysOfNextMonthList
    }
}
