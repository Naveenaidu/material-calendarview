package com.prolificinteractive.materialcalendarview.sample;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.sample.decorators.EventDecorator;
import com.prolificinteractive.materialcalendarview.sample.decorators.HighlightWeekendsDecorator;
import com.prolificinteractive.materialcalendarview.sample.decorators.MySelectorDecorator;
import com.prolificinteractive.materialcalendarview.sample.decorators.OneDayDecorator;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

/**
 * Shows off the most basic usage
 */
public class BasicActivityDecorated extends AppCompatActivity implements OnDateSelectedListener {

  private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
  private EventDecorator eventDecoratorRef;

  @BindView(R.id.calendarView)
  MaterialCalendarView widget;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_basic);
    ButterKnife.bind(this);

    widget.setOnDateChangedListener(this);
    widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

    final LocalDate instance = LocalDate.now();
    widget.setSelectedDate(instance);

    final LocalDate min = LocalDate.of(instance.getYear(), Month.JANUARY, 1);
    final LocalDate max = LocalDate.of(instance.getYear(), Month.DECEMBER, 31);

    ArrayList<CalendarDay> initializedDates = new ArrayList<>();
    initializedDates.add(CalendarDay.from(instance));
    eventDecoratorRef = new EventDecorator(Color.RED, initializedDates);

    widget.state().edit().setMinimumDate(min).setMaximumDate(max).commit();

    widget.addDecorators(
            new MySelectorDecorator(this),
            new HighlightWeekendsDecorator(),
            eventDecoratorRef,
            oneDayDecorator
    );

//    new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());
  }

  @Override
  public void onDateSelected(
          @NonNull MaterialCalendarView widget,
          @NonNull CalendarDay date,
          boolean selected) {
    //If you change a decorate, you need to invalidate decorators
    oneDayDecorator.setDate(date.getDate());
    widget.removeDecorator(eventDecoratorRef);
    widget.invalidateDecorators();

    LocalDate temp = date.getDate();
    Log.d("temp", String.valueOf(temp));
    final ArrayList<CalendarDay> dates = new ArrayList<>();
    for (int i = 0; i < 10; i = i + 2) {
      final CalendarDay day = CalendarDay.from(temp);
      dates.add(day);
      temp = temp.plusDays(2);
    }
    Log.d("dates", "dates: " + dates);

    eventDecoratorRef = new EventDecorator(Color.RED, dates);

    widget.addDecorator(eventDecoratorRef);

    // First time
    /*
      1. Get the list of dates from DB
      2. Populate the calender with those dates
     */
    // Second scenario
    /*
      1. User clicks on date and changes the attendance (Present -> Absent and vice versa)
      2. That update is recorded in DB
      3. Invalidate the decorators and use the values again from DB to create the calender
     */

    /*
        Use the EventDecorator from the
     */
  }

}
