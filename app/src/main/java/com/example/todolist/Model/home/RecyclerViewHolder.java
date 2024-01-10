package com.example.todolist.Model.home;

import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.MainActivity;
import com.example.todolist.R;

/**
 * The RecyclerViewHolder class describes a tasks view and metadata about its place within the RecyclerView.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    ImageView taskImage;
    TextView taskName;
    TextView isComplete;
    TextView taskDate;
    TextView taskDesc;
    RelativeLayout relativeLayout;
    SharedPreferences spf;



    public RecyclerViewHolder(@NonNull View taskView) {
        super(taskView);
        spf = MainActivity.spf;
        int font = Integer.parseInt(spf.getString("fontSize", "20"));

        taskImage = taskView.findViewById(R.id.taskImage);

        taskName = taskView.findViewById(R.id.taskName);
        taskName.setTextSize(TypedValue.COMPLEX_UNIT_SP, font + 2);

        taskDate = taskView.findViewById(R.id.taskDate);
        taskDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, font);

        taskDesc = taskView.findViewById(R.id.taskDesc);
        taskDesc.setTextSize(TypedValue.COMPLEX_UNIT_SP, font);

        isComplete = taskView.findViewById(R.id.isComplete);
        isComplete.setTextSize(TypedValue.COMPLEX_UNIT_SP, font);

        relativeLayout = taskView.findViewById(R.id.task_container);
    }
}
