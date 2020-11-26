package com.example.trackandtrigger;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewNoteActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;

    TextView date;
    DatePickerDialog datePickerDialog;
    TextView time;
    private int mYear, mMonth, mDay,hour,minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        createNotificationChannel();
        // initiate the date picker and a button
        date = (TextView) findViewById(R.id.date);
        // perform click event on edit text
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR); // current year
                mMonth = c.get(Calendar.MONTH); // current month
                mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(NewNoteActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                date.setText(dayOfMonth + "/"
                                        + (monthOfYear +1 ) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        //  initiate the edit text
        time = (TextView) findViewById(R.id.time);
        // perform click event listener on edit text
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(NewNoteActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        //Toast.makeText(this, "NewActivityLaunch",Toast.LENGTH_SHORT).show();
        if(getActionBar()!=null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            getActionBar().show();
        }
        else Toast.makeText(this,"Empty Action Bar",Toast.LENGTH_SHORT).show();

        setTitle("Add Note");

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);

        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(5);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                try {
                    saveNote();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() throws ParseException {

        //Schedule Notification
        Intent intent = new Intent(NewNoteActivity.this,ReminderBroadcast.class);
        intent.putExtra("TITLE",editTextTitle.getText().toString());
        intent.putExtra("DESCRIPTION",editTextDescription.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(NewNoteActivity.this,
                0,intent,0);


        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // TODO: 26-11-2020 Time Logic goes here!
        //long preOneHourTime =

        //String myDate = "2014/10/29 18:10:45";
        /*String myDate = String.valueOf(mYear)+"/"+
                       String.valueOf(mMonth+1)+"/"+
                       String.valueOf(mDay) + " " + String.valueOf(hour) +":"+String.valueOf(minute)+":00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = sdf.parse(myDate);
        long millis = date.getTime();
        long oneHourInMillis = 1000*60*60;*/
        long timeAtButtonClick = System.currentTimeMillis();
        long tenSecondsInMillis = 1000*5;

        //long timeForReminder = millis-oneHourInMillis;
        //if(timeForReminder>0) {
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    timeAtButtonClick - tenSecondsInMillis,
                    pendingIntent);

            String title = editTextTitle.getText().toString();
            String description = editTextDescription.getText().toString();
            int priority = numberPickerPriority.getValue();

            if (title.trim().isEmpty() || description.trim().isEmpty()) {
                Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
                return;
            }
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            CollectionReference notebookRef = FirebaseFirestore.getInstance()
                    .collection("Notebook_"+user.getUid().toString());
            notebookRef.add(new Note(title, description, priority));
            //notebookRef.add(new Note(title,description,priority));
            Toast.makeText(this, "Reminder added!", Toast.LENGTH_SHORT).show();
       // }else{
        //    Toast.makeText(this, "Add time in the future!", Toast.LENGTH_SHORT).show();
       // }
        finish();
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "ToDoReminderChannel";
            String description = "Channel for Todo Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyTodo",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}