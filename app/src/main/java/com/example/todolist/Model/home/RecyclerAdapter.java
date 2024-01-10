package com.example.todolist.Model.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Model.Task;
import com.example.todolist.R;
import com.example.todolist.ui.home.SelectListener;

import java.util.List;

/**
 * Class is responsible for providing views that represent items in a data set.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    Context context;
    List<Task> taskList;
    SelectListener listener;

    public RecyclerAdapter(Context context, List<Task> taskList, SelectListener listener) {
        this.context = context;
        this.taskList = taskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.task_view,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        holder.taskImage.setImageResource(taskList.get(position).getTaskImage());
        holder.taskName.setText(taskList.get(position).getTaskName());
        holder.taskDesc.setText(taskList.get(position).getTaskDescription());
        holder.taskDate.setText(taskList.get(position).getTaskDate());

        // Completion status for task
        if (taskList.get(position).getIsComplete()) {
            holder.isComplete.setText("Completed");
            holder.taskImage.setImageResource(R.drawable.baseline_check_24);
        }
        else  {
            holder.isComplete.setText("Incomplete");
            holder.taskImage.setImageResource(R.drawable.baseline_priority_high_24);
        }

        //onClickListener for task_container designed in task_view.xml
        holder.relativeLayout.setOnClickListener(view -> listener.onItemClicked(taskList.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
