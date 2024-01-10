package com.example.todolist.Model.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.MainActivity;
import com.example.todolist.Model.AppDatabase;
import com.example.todolist.Model.Notifications;
import com.example.todolist.Model.Task;
import com.example.todolist.Model.TaskDao;
import com.example.todolist.R;
import com.example.todolist.databinding.FragmentHomeBinding;
import com.example.todolist.ui.home.SelectListener;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

/**
 * The default activity Fragment that displays the list of To-Do tasks
 * @author Jay Stewart, Bryce McNary, Marwa Qureshi
 * @version 1.0
 * @see android.app.Fragment
 */
public class HomeFragment extends Fragment implements SelectListener {

    AppDatabase db;
    TaskDao taskDao;

    private FragmentHomeBinding binding;

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    /**
     * called on initial creation of the fragment.
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * called when the fragment returns to the foreground
     */
    @Override
    public void onStart() {
        super.onStart();
        db = MainActivity.db;
        taskDao = db.taskDao();
        recycler();
        setRecyclerVisibility();
    }

    /**
     * Called to add a task to the database and update the RecyclerView
     * @param task a Task
     */
    public void addToRecycler(Task task) {
        taskDao.insert(task);
        recycler();
        setRecyclerVisibility();
    }

    /**
     * Called to remove a task from the database and update the RecyclerView
     * @param task a Task
     */
    public void removeFromRecycler(Task task){
        taskDao.delete(task);
        recycler();
        setRecyclerVisibility();
    }

    /**
     * Called to refresh the RecyclerView
     */
    public void recycler() {
        RecyclerView recyclerView = requireView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Task> taskList = taskDao.getIncomplete();
        Collections.sort(taskList);
        recyclerView.setAdapter(new RecyclerAdapter(getContext(),taskList, this));
    }

    /**
     * This method sets the visibility of the RecyclerView
     * <p>
     * Sets the RecyclerView VISIBLE if the database is storing tasks WHERE isComplete = 0,
     * otherwise sets the RecyclerView INVISIBLE and
     * the TextView is set VISIBLE
     * </p>
     * @see View
     */
    public void setRecyclerVisibility(){
        RecyclerView recyclerView = requireView().findViewById(R.id.recyclerView);
        TextView textView = requireView().findViewById(R.id.noTaskTextView);
        if (!taskDao.getIncomplete().isEmpty()){
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Called when the user selects a task_container from the RecyclerView
     * @param task a Task Selected
     */
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    //onClick listener for recycler view
    public void onItemClicked(Task task) {
        showBottomDialog(task);
    }

    /**
     * Called to display a bottom Dialog on the screen.
     * <p>
     *     Displays the bottom_sheet_layout ContentView on the screen.
     *     The Dialog has three choices: edit, mark as complete, delete, and create notification.
     * </p>
     * @param task the selected Task
     */
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void showBottomDialog(Task task) {
        //declare Dialog and set dialog view to bottom_sheet_layout
        final Dialog bottomDialog = new Dialog(requireContext());
        bottomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomDialog.setContentView(R.layout.bottom_sheet_layout);

        //declare LinearLayouts from bottom_sheet_layout
        LinearLayout markCompleteLayout = bottomDialog.findViewById(R.id.bottom_sheet_markComplete);
        LinearLayout editLayout = bottomDialog.findViewById(R.id.bottom_sheet_edit);
        LinearLayout deleteLayout = bottomDialog.findViewById(R.id.bottom_sheet_delete);
        LinearLayout notifLayout = bottomDialog.findViewById(R.id.bottom_sheet_notification);

        // create onClickListener for each LinearLayout
        markCompleteLayout.setOnClickListener(view -> {
            // Update task to complete
            taskDao.setComplete(task.getTaskId());
            // create Snackbar msg
            Snackbar.make(requireView(), task.getTaskName() + " Marked As Complete", Snackbar.LENGTH_LONG)
                    .setAction("Undo", v -> {
                        taskDao.setIncomplete(task.getTaskId());
                        recycler();
                        setRecyclerVisibility();
                    })
                    .show();

            // dismiss dialog and update recycler
            recycler();
            setRecyclerVisibility();
            bottomDialog.dismiss();
        });

        editLayout.setOnClickListener(view -> {
            showEditDialog(task);
            bottomDialog.dismiss();
        });

        deleteLayout.setOnClickListener(view -> {
            showDeleteDialog(task);
            bottomDialog.dismiss();});

        notifLayout.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // You can use the API that requires the permission.
            }
            else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }

            Notifications notifications = new Notifications();
            notifications.createNotificationChannel(getContext());
            Notification notif = notifications.createNotification(
                    task.getTaskName(),
                    task.getTaskDescription(),
                    R.drawable.baseline_notifications_24,
                    getContext());

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyy", Locale.ENGLISH);
            try {
                calendar.setTime(Objects.requireNonNull(sdf.parse(task.getTaskDate())));
                calendar.set(Calendar.HOUR, Calendar.HOUR);
                calendar.set(Calendar.MINUTE, Calendar.MINUTE);
                calendar.set(Calendar.SECOND, Calendar.SECOND + 5);

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            notifications.scheduleNotification(notif, calendar, getContext());
            bottomDialog.dismiss();
        });

        //start dialog and display on screen with the following settings
        bottomDialog.show();
        Objects.requireNonNull(bottomDialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            });

    /**
     * Called to display a delete Dialog on the screen.
     * <p>
     *     Displays the delete_dialog ContentView on the screen.
     *     The Dialog has two choices: cancel or delete.
     * </p>
     * @param task the selected Task
     */
    private void showDeleteDialog(Task task){
        final Dialog deleteDialog = new Dialog(requireContext());

        deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        deleteDialog.setContentView(R.layout.delete_dialog);

        //declare the buttons from delete_dialog
        Button cancelButton = deleteDialog.findViewById(R.id.cancel_delete_button);
        Button deleteButton = deleteDialog.findViewById(R.id.delete_button);

        //start dialog and display on screen with the following settings
        deleteDialog.show();
        Objects.requireNonNull(deleteDialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deleteDialog.getWindow().setGravity(Gravity.CENTER);
        deleteDialog.setCancelable(false);

        cancelButton.setOnClickListener(view -> {
            //dismiss the dialog and remove it from the screen
            deleteDialog.dismiss();
        });

        deleteButton.setOnClickListener(view -> {
            //remove selected task from recycler and dismiss the dialog
            removeFromRecycler(task);
            deleteDialog.dismiss();
        });

    }

    /**
     * Called to display an edit Dialog on the screen.
     * <p>
     *     Displays the edit_dialog ContentView on the screen.
     *     The Dialog allows user to change task details.
     * </p>
     * @param task the selected Task
     */
     private void showEditDialog(Task task){
         //initialize the dialog
         final Dialog editDialog = new Dialog(requireContext());
         editDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
         editDialog.setContentView(R.layout.edit_dialog);

         //initialize everything
         EditText taskName = editDialog.findViewById(R.id.task_name);
         EditText cDate = editDialog.findViewById(R.id.current_date);
         EditText dueDate = editDialog.findViewById(R.id.due_date);
         editDialog.findViewById(R.id.reminder_time);
         EditText taskDescription = editDialog.findViewById(R.id.Prioritylev);
         Button cancelButton = editDialog.findViewById(R.id.cancelButton);
         Button updateButton = editDialog.findViewById(R.id.update_button);

         cDate.setOnFocusChangeListener((v, hasFocus) -> {
             if (hasFocus) {
                 cDate.setEnabled(false);
             }
         });
         cancelButton.setOnClickListener(view -> editDialog.dismiss());

         dueDate.addTextChangedListener(new TextWatcher() {
             private String current = "";

             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

             @SuppressLint("DefaultLocale")
             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
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
                         year = (year < 1900) ? 1900 : Math.min(year, cal.get(Calendar.YEAR));
                         cal.set(Calendar.YEAR, year);
                         day = Math.min(day, cal.getActualMaximum(Calendar.DATE));
                         clean = String.format("%02d%02d%02d", month, day, year);
                     }

                     clean = String.format("%s/%s/%s", clean.substring(0, 2),
                             clean.substring(2, 4),
                             clean.substring(4, 8));

                     sel = Math.max(sel, 0);
                     current = clean;
                     dueDate.setText(current);
                     dueDate.setSelection(Math.min(sel, current.length()));
                 }
             }

             @Override
             public void afterTextChanged(Editable s) {}
         });

         updateButton.setOnClickListener(view -> {
             addToRecycler(new Task(task.getTaskId(), task.getTaskImage(), task.getIsComplete(), taskName.getText().toString(), taskDescription.getText().toString(), dueDate.getText().toString()));
             editDialog.dismiss();
         });

         //populate page with stored values
         taskName.setText(task.getTaskName());
         @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
         Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
         cDate.setText(dateFormat.format(calendar.getTime()));
         dueDate.setText(task.getTaskDate());
         taskDescription.setText(task.getTaskDescription());

         //start the dialog
         Objects.requireNonNull(editDialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
         editDialog.show();

     }
}