package com.example.todolist.Model.ui.completed;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.MainActivity;
import com.example.todolist.Model.AppDatabase;
import com.example.todolist.Model.Task;
import com.example.todolist.Model.TaskDao;
import com.example.todolist.Model.home.RecyclerAdapter;
import com.example.todolist.R;
import com.example.todolist.databinding.FragmentCompletedBinding;
import com.example.todolist.ui.home.SelectListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;


/**
 * The activity Fragment that displays the list of completed To-Do tasks.
 * @author Jay Stewart, Bryce McNary, Marwa Qureshi
 * @version 1.0
 * @see android.app.Fragment
 */
public class CompletedFragment extends Fragment implements SelectListener {

    AppDatabase db;
    TaskDao taskDao;

    private FragmentCompletedBinding binding;

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

        binding = FragmentCompletedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
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
        refreshRecycler();
        setRecyclerVisibility();
    }

    /**
     * Called to refresh the RecyclerView
     * @return updated RecyclerView
     */
    public void refreshRecycler(){
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Task> taskList = taskDao.getComplete();
        Collections.sort(taskList);
        recyclerView.setAdapter(new RecyclerAdapter(getContext(),taskList, this));
    }

    /**
     * This method sets the visibility of the RecyclerView
     * <p>
     * Sets the RecyclerView VISIBLE if the database is storing tasks WHERE isComplete = 1,
     * otherwise sets the RecyclerView INVISIBLE and
     * the TextView is set VISIBLE
     * </p>
     * @see View
     */
    public void setRecyclerVisibility(){
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        TextView textView = getView().findViewById(R.id.noTaskTextView);
        if (!taskDao.getComplete().isEmpty()){
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Called when the user selects a task_container from the RecyclerView
     * @param task a Task Selected
     * @return a bottom_sheet_dialog for the selected Task
     */
    @Override
    //onClick listener for recycler view
    public void onItemClicked(Task task) {
        showBottomDialog(task);
    }

    /**
     * Called to display a bottom Dialog on the screen.
     * <p>
     *     Displays the completed_bottom_sheet_layout ContentView on the screen.
     *     The Dialog has two choices: Mark As Incomplete, and delete.
     * </p>
     * @param task the selected Task
     */
    public void showBottomDialog(Task task) {

        final Dialog bottomDialog = new Dialog(getContext());
        bottomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomDialog.setContentView(R.layout.completed_bottom_sheet_layout);

        //declare LinearLayouts from bottom_sheet_layout
        LinearLayout markIncompleteLayout = bottomDialog.findViewById(R.id.completed_bottom_sheet_markIncomplete);
        LinearLayout deleteLayout = bottomDialog.findViewById(R.id.completed_bottom_sheet_delete);

        // create onClickListener for each LinearLayout
        markIncompleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update task to complete
                taskDao.setIncomplete(task.getTaskId());
                // create Snack bar msg
                Snackbar.make(getView(), task.getTaskName() + " Marked As Incomplete", Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> {
                            taskDao.setComplete(task.getTaskId());
                            refreshRecycler();
                            setRecyclerVisibility();
                        })
                        .show();

                // dismiss dialog and update recycler
                refreshRecycler();
                setRecyclerVisibility();
                bottomDialog.dismiss();
            }
        });

        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelete(task);
                bottomDialog.dismiss();}
        });

        //start dialog and display on screen with the following settings
        bottomDialog.show();
        bottomDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    /**
     * Called to display a delete Dialog on the screen.
     * <p>
     *     Displays the delete_dialog ContentView on the screen.
     *     The Dialog has two choices: cancel or delete.
     * </p>
     * @param task the selected Task
     */
    public void showDelete(Task task){
        final Dialog deleteDialog = new Dialog(getContext());

        deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        deleteDialog.setContentView(R.layout.delete_dialog);

        //declare the buttons from delete_dialog
        Button cancelButton = deleteDialog.findViewById(R.id.cancel_delete_button);
        Button deleteButton = deleteDialog.findViewById(R.id.delete_button);

        //start dialog and display on screen with the following settings
        deleteDialog.show();
        deleteDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deleteDialog.getWindow().setGravity(Gravity.CENTER);
        deleteDialog.setCancelable(false);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dismiss the dialog and remove it from the screen
                deleteDialog.dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove selected task from recycler and dismiss the dialog
                taskDao.delete(task);
                refreshRecycler();
                setRecyclerVisibility();
                deleteDialog.dismiss();
            }
        });
    }

    /**
     * Called when the view previously created by onCreateView has been detached from the fragment.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}