package com.example.taskapp2025;

import com.example.taskapp2025.models.Task;

import java.util.ArrayList;

public interface Taskable {
    public ArrayList<Task> getAllTasks();
    public Task getTaskById(long id);
    public Task insertTask(Task t) throws Exception;
    public Task updateTask(Task t) throws Exception;
    public int deleteTask(Task t);
}