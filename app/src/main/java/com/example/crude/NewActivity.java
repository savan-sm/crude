package com.example.crude;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class NewActivity extends AppCompatActivity {

    private EditText dateEditText, timeEditText, skillEditText;
    private Button addButton, updateButton, deleteButton;
    private DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        // Initialize views and DatabaseHelper
        dateEditText = findViewById(R.id.date);
        timeEditText = findViewById(R.id.time);
        skillEditText = findViewById(R.id.skill);
        addButton = findViewById(R.id.add_button);
        updateButton = findViewById(R.id.update);
        myDb = new DatabaseHelper(this);

        // Get data from Intent (for edit mode)
        String skill1 = getIntent().getStringExtra("SKILL");
        String date1 = getIntent().getStringExtra("DATE");
        String time1 = getIntent().getStringExtra("TIME");

        // Check if this is edit mode
        if (skill1 != null && date1 != null && time1 != null) {
            // Set received data in EditTexts
            skillEditText.setText(skill1);
            dateEditText.setText(date1);
            timeEditText.setText(time1);

            addButton.setVisibility(View.GONE); // Hide Add button
            updateButton.setVisibility(View.VISIBLE); // Show Update button// Show Delete button
        }


        // Handle Update button click (for editing existing data)
        updateButton.setOnClickListener(v -> {
            String newSkill = skillEditText.getText().toString();
            String newDate = dateEditText.getText().toString();
            String newTime = timeEditText.getText().toString();

            if (newSkill.isEmpty() || newDate.isEmpty() || newTime.isEmpty()) {
                Toast.makeText(NewActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                boolean isUpdated = myDb.updateDataBySkill(skill1, newSkill, newDate, newTime);

                if (isUpdated) {
                    Toast.makeText(NewActivity.this, "Data updated successfully", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(NewActivity.this, "Data update failed", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Handle Add button click (for adding new data)
        addButton.setOnClickListener(v -> {
            String skill = skillEditText.getText().toString();
            String date = dateEditText.getText().toString();
            String time = timeEditText.getText().toString();

            if (skill.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(NewActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Insert new data into SQLite
                boolean isInserted = myDb.insertData(skill, date, time);

                if (isInserted) {
                    Toast.makeText(NewActivity.this, "Data added successfully", Toast.LENGTH_LONG).show();
                    clearFields();
                    finish();
                } else {
                    Toast.makeText(NewActivity.this, "Data insertion failed", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Set Date picker on date EditText
        dateEditText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(NewActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        dateEditText.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Set Time picker on time EditText
        timeEditText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(NewActivity.this,
                    (view, selectedHour, selectedMinute) -> {
                        timeEditText.setText(selectedHour + ":" + String.format("%02d", selectedMinute));
                    }, hour, minute, true);
            timePickerDialog.show();
        });
    }

    // Clear the input fields after data is added
    private void clearFields() {
        skillEditText.setText("");
        dateEditText.setText("");
        timeEditText.setText("");
    }
}
