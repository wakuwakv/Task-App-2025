package com.example.taskapp2025;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taskapp2025.dataaccess.CSVTaskDataAccess;
import com.example.taskapp2025.dataaccess.TaskDataAccess;
import com.example.taskapp2025.models.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskDetailsActivity extends AppCompatActivity {

    public static final String TAG = "TaskDetailsActivity";

    public static final String EXTRA_TASK_ID = "taskId";

    Taskable da;
    Task task;
    EditText txtDescription;
    EditText txtDueDate;
    CheckBox chkDone;
    Button btnSave;

    Button btnDelete;
    SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        da = new TaskDataAccess(this);
//        da = new CSVTaskDataAccess(this);

        txtDescription = findViewById(R.id.txtDescription);
        txtDueDate = findViewById(R.id.txtDueDate);
        chkDone = findViewById(R.id.chkDone);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        Intent i = getIntent();
        long id = i.getLongExtra(EXTRA_TASK_ID,0);

        if(id>0){
//            Log.d(TAG,"TODO GET ITEM WIRTH ID: "+id);
            task = da.getTaskById(id);
            Log.d(TAG,task.toString());
            putDataIntoUI();

            btnDelete.setVisibility(View.VISIBLE);

        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        txtDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDiolog();
            }
        });

    }

    private boolean save(){

        try {

            if (validate()) {
                getDataFromUI();

                if (task.getId() > 0) {

                    da.updateTask(task);

                } else {

                    da.insertTask(task);

                }

                Intent i = new Intent(this, TaskListActivity.class);
                startActivity(i);
                return true;
            }
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        return false;
    }

    private void putDataIntoUI(){
        if(task!=null){
            txtDescription.setText(task.getDescription());
            chkDone.setChecked(task.isDone());
            String dateStr = null;
            dateStr = sdf.format(task.getDue());
            txtDueDate.setText(dateStr);
        }
    }

    private boolean validate(){
        boolean isValid = true;
        if(txtDescription.getText().toString().isEmpty()){
            isValid = false;
            txtDescription.setError("You must enter a description");
        }

        Date dueDate = null;
        if(txtDueDate.getText().toString().isEmpty()){
            isValid = false;
            Log.d(TAG,"YOU MUST ENTER A DATE");
            txtDueDate.setError("You must enter a date");
        } else {
            String regex = "^(1[0-2]|0[1-9])/(3[01]"
                    + "|[12][0-9]|0[1-9])/[0-9]{4}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(txtDueDate.getText().toString());
            if(!matcher.matches()){
                isValid = false;
                Log.d(TAG,txtDueDate.getText().toString());
                Log.d(TAG,regex);
                Log.d(TAG,"The date entered is not valid");
                txtDueDate.setError("The date entered is not valid");
            }

        }

        return isValid;
    };

    private void getDataFromUI(){
        String desc = txtDescription.getText().toString();
        String dueDateStr = txtDueDate.getText().toString();
        boolean done = chkDone.isChecked();

        Date date = null;

        try{
            date = sdf.parse(dueDateStr);
        } catch (ParseException e) {
            Log.d(TAG,"UnAble to parse date from string");
        }

        if(task != null){
            task.setDescription(desc);
            task.setDue(date);
            task.setDone(done);
        } else {
            task = new Task(desc,date,done);
        }

    }

    private void showDatePicker (){
        Date today = new Date();
        int year = today.getYear() + 1900;
        int month = today.getMonth();
        int day = today.getDate();

        DatePickerDialog dp = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                String selectedDate = ((m+1)>9 ? "" : "0") + (m + 1) + "/" + (d>9 ? "" : "0") + d + "/" + y;
                txtDueDate.setText(selectedDate);
            }
        }, year, month, day);

        dp.show();

    }

    private void showDeleteDiolog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.delete_task);
        alert.setMessage(R.string.confirm_delete);
        alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                da.deleteTask(task);
                startActivity(new Intent(TaskDetailsActivity.this, TaskListActivity.class));
            }
        });
        alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }

}