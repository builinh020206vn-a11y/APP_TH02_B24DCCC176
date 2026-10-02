package com.example.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddTaskActivity extends Activity {

    private EditText etTaskTitle, etTaskTime, etTaskNote;
    private Button btnSave;
    private int selectedYear, selectedMonth, selectedDay;
    private boolean isDateSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        etTaskTitle = findViewById(R.id.etTaskTitle);
        etTaskTime = findViewById(R.id.etTaskTime);
        etTaskNote = findViewById(R.id.etTaskNote);
        btnSave = findViewById(R.id.btnSave);
        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);


        etTaskTime.setFocusable(false);
        etTaskTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddTaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                selectedYear = year;
                                selectedMonth = monthOfYear;
                                selectedDay = dayOfMonth;
                                isDateSelected = true;

                                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                etTaskTime.setText(selectedDate);
                            }
                        }, selectedYear, selectedMonth, selectedDay);
                datePickerDialog.show();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTaskTitle.getText().toString().trim();

                if (title.isEmpty()) {
                    Toast.makeText(AddTaskActivity.this, "Vui lòng nhập tên công việc!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("TASK_TITLE", title);
                resultIntent.putExtra("TASK_DATE", etTaskTime.getText().toString().trim());
                resultIntent.putExtra("YEAR", selectedYear);
                resultIntent.putExtra("MONTH", selectedMonth);
                resultIntent.putExtra("DAY", selectedDay);
                resultIntent.putExtra("IS_DATE_SELECTED", isDateSelected);

                setResult(RESULT_OK, resultIntent);
                Toast.makeText(AddTaskActivity.this, "Đã lưu công việc!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}