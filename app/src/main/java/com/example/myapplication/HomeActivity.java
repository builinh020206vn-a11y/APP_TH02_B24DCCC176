package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeActivity extends Activity {

    private static final int ADD_TASK_REQUEST_CODE = 100;

    private TextView tvUserName;
    private ListView lvTodayTasks, lvUpcomingTasks;
    private Button btnAddTask;

    private ArrayList<String> todayTasks;
    private ArrayList<String> upcomingTasks;
    private TaskAdapter todayAdapter;
    private TaskAdapter upcomingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvUserName = findViewById(R.id.tvUserName);
        lvTodayTasks = findViewById(R.id.lvTodayTasks);
        lvUpcomingTasks = findViewById(R.id.lvUpcomingTasks);
        btnAddTask = findViewById(R.id.btnAddTask);

        String username = getIntent().getStringExtra("USER_NAME");
        if (username != null && !username.isEmpty()) {
            tvUserName.setText(username);
        }

        todayTasks = new ArrayList<>();
        todayTasks.add("Học lập trình Android Studio");

        upcomingTasks = new ArrayList<>();
        upcomingTasks.add("Ôn thi giữa kỳ");

        todayAdapter = new TaskAdapter(this, todayTasks);
        upcomingAdapter = new TaskAdapter(this, upcomingTasks);

        lvTodayTasks.setAdapter(todayAdapter);
        lvUpcomingTasks.setAdapter(upcomingAdapter);

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, ADD_TASK_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra("TASK_TITLE");
            String dateStr = data.getStringExtra("TASK_DATE");
            boolean isDateSelected = data.getBooleanExtra("IS_DATE_SELECTED", false);

            String fullTaskText = title + (dateStr != null && !dateStr.isEmpty() ? " (" + dateStr + ")" : "");

            if (isDateSelected) {
                int selectedYear = data.getIntExtra("YEAR", 0);
                int selectedMonth = data.getIntExtra("MONTH", 0);
                int selectedDay = data.getIntExtra("DAY", 0);

                Calendar today = Calendar.getInstance();
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0);
                selectedCalendar.set(Calendar.MILLISECOND, 0);

                Calendar todayClean = Calendar.getInstance();
                todayClean.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                todayClean.set(Calendar.MILLISECOND, 0);

                if (selectedCalendar.after(todayClean)) {
                    upcomingTasks.add(fullTaskText);
                    upcomingAdapter.notifyDataSetChanged();
                } else {
                    todayTasks.add(fullTaskText);
                    todayAdapter.notifyDataSetChanged();
                }
            } else {
                todayTasks.add(fullTaskText);
                todayAdapter.notifyDataSetChanged();
            }
        }
    }

    private class TaskAdapter extends ArrayAdapter<String> {
        public TaskAdapter(Activity context, ArrayList<String> tasks) {
            super(context, R.layout.item_task, tasks);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
            }

            CheckBox cbTask = convertView.findViewById(R.id.cbTask);
            TextView tvTaskTitle = convertView.findViewById(R.id.tvTaskTitle);

            tvTaskTitle.setText(getItem(position));


            cbTask.setOnCheckedChangeListener(null);
            cbTask.setChecked(false);
            tvTaskTitle.setPaintFlags(tvTaskTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            tvTaskTitle.setAlpha(1.0f);


            cbTask.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    tvTaskTitle.setPaintFlags(tvTaskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    tvTaskTitle.setAlpha(0.5f);
                } else {
                    tvTaskTitle.setPaintFlags(tvTaskTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    tvTaskTitle.setAlpha(1.0f);
                }
            });

            return convertView;
        }
    }
}