package com.example.taskapp2025.dataaccess;

import android.content.Context;
import android.util.Log;

import com.example.taskapp2025.Taskable;
import com.example.taskapp2025.fileio.FileHelper;
import com.example.taskapp2025.models.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CSVTaskDataAccess implements Taskable {
    public static final String TAG = "CSVTaskDataAccess";
    public static final String DATA_FILE = "tasks.csv";
    ArrayList<Task> allTasks;
    Context context;

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    public CSVTaskDataAccess(Context c){
        this.context = c;
        this.allTasks = new ArrayList<Task>();
        loadTask();

        //For testing
        this.allTasks.add(new Task(1,"Dosometh",new Date(), false));
        this.allTasks.add(new Task(2,"Dosometh 2",new Date(), true));
        saveTasks();
        loadTask();
        Log.d(TAG,this.allTasks.toString());
    }

    public Task convertCSVToTask(String csvLine){
        // "7, mow the lawn or something, 3/5/2025, true"
        String[] csvValues = csvLine.split(",");
        if(csvValues.length == 4){
            long id = Long.parseLong(csvValues[0]);
            String desc = csvValues[1];
            String dateStr = csvValues[2];
            boolean done = csvValues[3].equals("true");

            Date dueDate = null;
            try {
                dueDate = sdf.parse(dateStr);
            } catch (ParseException e) {
                Log.d(TAG, "Unable to parse date string");
                throw new RuntimeException(e);
            }
            Task t = new Task(id, desc, dueDate, done);
            return t;
        }



        return  null;


    }

    public String convertTaskToCSV(Task t){
        String dateStr = null;
        dateStr = sdf.format(t.getDue());
        return String.format("%d,%s,%s,%s", t.getId(), t.getDescription(), dateStr,t.isDone() ? "true" : "false");
    }

    private void loadTask(){
        // TODO: load tasks from the .csv file
        ArrayList<Task> dataList = new ArrayList<Task>();
        String dataString = FileHelper.readFromFile(DATA_FILE, context);
        if(dataString == null){
            Log.d(TAG, "No data file??");
            return;
        }
        String[] lines = dataString.trim().split("\n");
        for(String line : lines){
            Task t = convertCSVToTask(line);
            if(t != null){
                dataList.add(t);
            }
        }
        this.allTasks = dataList;

    }


    private void saveTasks(){
        // TODO: save allTask to the csv file
        String dataString = "";
        StringBuilder sb = new StringBuilder();
        for(Task t : this.allTasks){
            String csv = convertTaskToCSV(t) + "\n";
            sb.append(csv);
        }
        dataString = sb.toString();
        if(FileHelper.writeToFile(DATA_FILE,dataString, context)){
            Log.d(TAG, "Successfully saved data");
        }else{
            Log.d(TAG, "Failed to save data to file");
        }
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return allTasks;
    }

    @Override
    public Task getTaskById(long id) {
        return null;
    }

    @Override
    public Task insertTask(Task t) throws Exception {
        if(t.isValid()){
            allTasks.add(t);
            t.setId(getMaxId()+1);
            saveTasks();
            return  t;
        }
        throw new Exception("Invalid Task on insert");
    }

    private long getMaxId(){
        long maxId = 0;
        for(Task t : allTasks){
            maxId = t.getId()>maxId ? t.getId() : maxId;
        }
        return maxId;
    }

    @Override
    public Task updateTask(Task t) throws Exception {
        if(t.isValid()){
            Task taskToUpdate = getTaskById(t.getId());

            taskToUpdate.setDescription(t.getDescription());
            taskToUpdate.setDue(t.getDue());
            taskToUpdate.setDone(taskToUpdate.isDone());
            saveTasks();
        } else {
            throw new Exception("Invalid Task On Update");
        }
        return t;
    }

    @Override
    public int deleteTask(Task t) {
        Task taskToRemove = getTaskById(t.getId());
        if(taskToRemove != null){
            allTasks.remove(taskToRemove);
            saveTasks();
            return 1;
        }
        return  0;
    }
}