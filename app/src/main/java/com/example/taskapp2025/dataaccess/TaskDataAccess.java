package com.example.taskapp2025.dataaccess;

import android.content.Context;

import com.example.taskapp2025.Taskable;
import com.example.taskapp2025.models.Task;

import java.util.ArrayList;
import java.util.Date;

public class TaskDataAccess implements Taskable {
    public static final String TAG = "TaskDataAccess";

    public static ArrayList<Task> allTasks = new ArrayList<Task>(){{
        add(new Task(1, "Mow The lawn", new Date(), false));
        add(new Task(2, "Take out the trash", new Date(), false));
        add(new Task(3, "Pay rent", new Date(), false));
    }};

    private Context context;

    public TaskDataAccess(Context c){
        this.context = c;
    }

    public ArrayList<Task> getAllTasks(){
        return allTasks;
    }

    public Task getTaskById(long id){
        for(Task t : allTasks){
            if(t.getId() == id){
                return  t;
            }
        }
        return null;
    }

    private long getMaxId(){
        long maxId = 0;
        for(Task t : allTasks){
            maxId = t.getId()>maxId ? t.getId() : maxId;
        }
        return maxId;
    }

    public Task insertTask(Task t) throws Exception{
        if(t.isValid()){
            allTasks.add(t);
            t.setId(getMaxId()+1);
            return  t;
        }
        throw new Exception("Invalid Task on insert");
    }

    public Task updateTask(Task updatedTask) throws Exception{
        if(updatedTask.isValid()){
            Task taskToUpdate = getTaskById(updatedTask.getId());

            taskToUpdate.setDescription(updatedTask.getDescription());
            taskToUpdate.setDue(updatedTask.getDue());
            taskToUpdate.setDone(taskToUpdate.isDone());
            return updatedTask;
        }
        throw new Exception("Invalid Task On Update");
    }

    public int deleteTask(Task taskToDelete){
        for(Task t : allTasks){
            if(t.getId() == taskToDelete.getId()){
                allTasks.remove(taskToDelete);
                return 1;
            }
        }
        return  0;
    }

}