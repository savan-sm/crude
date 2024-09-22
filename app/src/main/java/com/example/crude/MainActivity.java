package com.example.crude;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Cursor cursor = (Cursor) listView.getItemAtPosition(position);
            @SuppressLint("Range") String skill = cursor.getString(cursor.getColumnIndex("SKILL"));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("DATE"));
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("TIME"));

            Intent intent = new Intent(MainActivity.this, NewActivity.class);
            intent.putExtra("SKILL", skill);
            intent.putExtra("DATE", date);
            intent.putExtra("TIME", time);
            startActivity(intent);
        });
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            Intent intent = new Intent(this, NewActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
    private void loadData() {
        Cursor cursor = myDb.getAllData();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] fromColumns = new String[] { "SKILL", "DATE", "TIME" };
        int[] toViews = new int[] { R.id.skillTextView, R.id.dateTextView, R.id.timeTextView };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.list_item,
                cursor,
                fromColumns,
                toViews,
                0) {
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                super.bindView(view, context, cursor);
                @SuppressLint("Range") String skill = cursor.getString(cursor.getColumnIndex("SKILL"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("DATE"));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("TIME"));
                ImageButton editButton = view.findViewById(R.id.editButton);
                editButton.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, NewActivity.class);
                    intent.putExtra("SKILL", skill);
                    intent.putExtra("DATE", date);
                    intent.putExtra("TIME", time);
                    startActivity(intent);
                });

                ImageButton deleteButton = view.findViewById(R.id.deleteButton);
                deleteButton.setOnClickListener(v -> {
                    boolean isDeleted = myDb.deleteDataBySkill(skill);
                    if (isDeleted) {
                        Toast.makeText(MainActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(MainActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        listView.setAdapter(adapter);
    }

}
