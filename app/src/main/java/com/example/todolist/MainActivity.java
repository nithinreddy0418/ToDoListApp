package com.example.todolist;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import androidx.room.Room;

import com.example.todolist.Model.AppDatabase;
import com.example.todolist.Model.Task;
import com.example.todolist.Model.ui.settings.SettingsActivity;
import com.example.todolist.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * The Main activity for the application.
 * <p>This is the first Screen the user sees</p>
 */
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    public static AppDatabase db;

    public static SharedPreferences spf;


    /**
     * called on initial creation of the activity. Perform initialization of all fragments.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spf = PreferenceManager.getDefaultSharedPreferences(this);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "tasks-db").allowMainThreadQueries().build();

        com.example.todolist.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.btnAddItem.setOnClickListener(view -> {
            ConstraintLayout popupWindow = findViewById(R.id.popup_window);
            FloatingActionButton btnAddItem = findViewById(R.id.btnAddItem);
            //the btnAddItem floating action button will disappear once pop-up window opens
            btnAddItem.setVisibility(View.GONE);
            popupWindow.setVisibility(View.VISIBLE);
        });


        //Return to Main Screen when cancel button is clicked
        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> {
            // Create an intent to return to the main activity
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        });



        //This will by default display today's date which is editable
        EditText editText = findViewById(R.id.current_date);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
        editText.setText(dateFormat.format(calendar.getTime()));

        // Disable EditText when it gains focus
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                editText.setEnabled(false);
            }
        });

        // Set up the due date format and restriction
        EditText dueDateEditText = findViewById(R.id.due_date);

        dueDateEditText.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // Get current year
                int currentYear = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    currentYear = LocalDate.now().getYear();
                }

                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        String mmddyyyy = "MMDDYYYY";
                        clean = clean + mmddyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int month = Integer.parseInt(clean.substring(0, 2));
                        int day = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        month = month < 1 ? 1 : Math.min(month, 12);
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.MONTH, month - 1);

                        //The line of code below sets minimum year to current year and max year of due date as next year
                        //This auto-corrects when year is for example, 1800 or 2028
                        year = (year < currentYear) ? currentYear : Math.min(year, cal.get(Calendar.YEAR) + 1);

                        cal.set(Calendar.YEAR, year);
                        day = Math.min(day, cal.getActualMaximum(Calendar.DATE));
                        clean = String.format("%02d%02d%02d", month, day, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = Math.max(sel, 0);
                    current = clean;
                    dueDateEditText.setText(current);
                    dueDateEditText.setSelection(Math.min(sel, current.length()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        EditText taskName = findViewById(R.id.task_name);
        EditText description = findViewById(R.id.Prioritylev);

        final Button addTaskBtn = findViewById(R.id.add_task_button);
        addTaskBtn.setOnClickListener(view -> {
            createTask(taskName.getText().toString(),
                    description.getText().toString(),
                    dueDateEditText.getText().toString());
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        });



                DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_complete)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }



    /**
     * called when the activity returns to the foreground
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    private void createTask(String taskName, String description, String dueDate) {
        if (!(taskName.isEmpty()) &&
                !(description.isEmpty()) &&
                !(dueDate.isEmpty())) {

            db.taskDao().insert(new Task(getNextPrimaryKey(),
                    R.drawable.baseline_priority_high_24, false,
                    taskName,
                    description,
                    dueDate));
        }
    }

    /**
     * inflates the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings_navigation) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Retrieves the smallest primary Key not in use by the database
     * @return primaryKey a primary key value not in use by the database
     */
    private int getNextPrimaryKey(){
        int primaryKey = 1;
        for (Task task: db.taskDao().getAll()) {
            if (primaryKey != task.getTaskId()){
                return primaryKey;
            }
            else primaryKey += 1;
        }
        return primaryKey;
    }
}