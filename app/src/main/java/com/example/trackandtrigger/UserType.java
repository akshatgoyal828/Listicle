package com.example.trackandtrigger;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class UserType extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView textView;
    private AlarmManager alarmMgr;
    private Context context;
    private PendingIntent alarmIntent;
    Button notification;
    //EditText time;

    TextView time;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user_type);

            context=getApplicationContext();
            //  initiate the edit text
            time = findViewById(R.id.time);
            // perform click event listener on edit text
            time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(UserType.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            time.setText(selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();

                }
            });

            notification=findViewById(R.id.button2);

            notification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Set the alarm to start at approximately 2:00 p.m.
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    mcurrentTime.setTimeInMillis(System.currentTimeMillis());
                    mcurrentTime.set(Calendar.HOUR_OF_DAY, hour);
                    mcurrentTime.set(Calendar.MINUTE, minute);


// With setInexactRepeating(), you have to use one of the AlarmManager interval
// constants--in this case, AlarmManager.INTERVAL_DAY.

                    alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(context, ReminderBroadcast.class);
                    alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
                    alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, mcurrentTime.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, alarmIntent);

                }
            });

            radioGroup = findViewById(R.id.radioGroup);
            textView = findViewById(R.id.text_view_selected);
            Button buttonApply = findViewById(R.id.button_apply);

            buttonApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int radioId = radioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(radioId);
                    String userTyper = radioButton.getText().toString();
                    textView.setText("Your choice: " + userTyper);

                    Intent userTypeIntent = new Intent(UserType.this, DashBoard.class);
                    userTypeIntent.putExtra("UserType",userTyper);
                    setResult(RESULT_OK, userTypeIntent);
                    startActivity(userTypeIntent);
                    over();
                }
            });
        }

    private void over() {
            finish();
    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Toast.makeText(this, "Selected Radio Button: " + radioButton.getText(),
                Toast.LENGTH_SHORT).show();
    }
}