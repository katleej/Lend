package com.example.lend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CalendarView;

public class BorrowItemActivity extends AppCompatActivity {

    CalendarView mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_item);

        mCalendar = (CalendarView) findViewById(R.id.availability_calendar);
        mCalendar.setDate(System.currentTimeMillis(), false, true);
    }
}
