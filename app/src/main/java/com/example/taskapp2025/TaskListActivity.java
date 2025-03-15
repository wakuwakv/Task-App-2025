package com.example.taskapp2025;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taskapp2025.dataaccess.CSVTaskDataAccess;
import com.example.taskapp2025.dataaccess.TaskDataAccess;
import com.example.taskapp2025.models.Task;

import java.util.ArrayList;

public class TaskListActivity extends AppCompatActivity {

    public static final String TAG = "TaskListActivity";
    private ListView lsTask;
    private Taskable da;
    private ArrayList<Task> allTasks;
    private Button btnAddTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        btnAddTask = findViewById(R.id.btnAddTask);
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TaskListActivity.this, TaskDetailsActivity.class);
                startActivity(i);
            }
        });

        lsTask = findViewById(R.id.lsTask);
        da = new TaskDataAccess(this);
//        da = new CSVTaskDataAccess(this);
        allTasks = da.getAllTasks();

        if(allTasks.isEmpty()){
            startActivity(new Intent(this, TaskListActivity.class));
        }

        ArrayAdapter<Task> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, allTasks);
        lsTask.setAdapter(adapter);

        lsTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
//                Log.d(TAG, "i = "+ position);
                Task selectedTask = allTasks.get(i);
//                Log.d(TAG, selectedTask.toString());
                Intent intent = new Intent(TaskListActivity.this, TaskDetailsActivity.class);
                intent.putExtra(TaskDetailsActivity.EXTRA_TASK_ID, selectedTask.getId());
//                intent.putExtra("SOME KEY", selectedTask.getId());

                startActivity(intent);
            }
        });

    }
}