package com.example.taskapp2025;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taskapp2025.dataaccess.CSVTaskDataAccess;
import com.example.taskapp2025.dataaccess.TaskDataAccess;
import com.example.taskapp2025.models.Task;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Task t = new Task(7, "Call mom", new Date(), false);
        Log.d(TAG, t.toString());
        Log.d(TAG, "" + t.isValid());

        TaskDataAccess tda = new TaskDataAccess(this );
        try {
            tda.insertTask(new Task("Do Something", null, true));
        } catch (Exception e) {
//            throw new RuntimeException(e);
            Log.d(TAG, "ERROR: "+ e.getMessage());
        }

        ArrayList<Task> tasks = tda.getAllTasks();
        Log.d(TAG, tasks.toString());

        Task newTask = new Task("Do Homework", new Date(), false);
        try {
            tda.insertTask(newTask);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Log.d(TAG, tasks.toString());

        Task task = tda.getTaskById(1);
        Log.d(TAG, tasks.toString());

        task.setDescription("Cut the lawn");
        Log.d(TAG, tasks.toString());

        Task updateTask = new Task(task.getId(), task.getDescription(), task.getDue(), task.isDone());
        try {
            updateTask = tda.updateTask(updateTask);
            Log.d(TAG, tda.getAllTasks().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        int numDeleted = tda.deleteTask(updateTask);
        Log.d(TAG, tda.getAllTasks().toString());

        CSVTaskDataAccess da = new CSVTaskDataAccess(this);
        Task taa = da.convertCSVToTask("1,mow the lawn,03/05/2025,true");
        Log.d(TAG, taa.toString());
        String csvLine = da.convertTaskToCSV(taa);
        Log.d(TAG, csvLine);




    }
}