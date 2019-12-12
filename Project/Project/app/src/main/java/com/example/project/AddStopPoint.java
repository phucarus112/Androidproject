package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddStopPoint extends AppCompatActivity {
    private ImageView imDateArr,imDateLea;
    private TextView setDateArr,setDateLea,setArrTime,setLeaTime;
    private DatePickerDialog.OnDateSetListener mDateSetListenr, mDateSetListenr2;
    private EditText address;
    private long milisecondArr, milisecondLea;
    private static final String TAG="MainActivity";
    private Spinner spinner,spinner2;

    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater= getActivity().getLayoutInflater();
//        View view= inflater.inflate(R.layout.activity_add_stop_point,null);
//        builder.setView(view).setTitle("StopPoint")
//                .setNegativeButton("x", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//        return builder.create();
//    }
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stop_point);

        getCompenent();
       // spinner=new Spinner(AddStopPoint.this,Spinner.MODE_DIALOG);

        final ArrayList<String> arrayServiceType= new ArrayList<String>();
        arrayServiceType.add("Restaurant");
        arrayServiceType.add("Hotel");
        arrayServiceType.add("Super Market");

        final ArrayList<String> arrayCity= new ArrayList<String>();
        arrayCity.add("TP. Hồ Chí Minh");
        arrayCity.add("Hải Phòng");
        arrayCity.add("Hà Nội");
        arrayCity.add("Đà Nẵng");
        arrayCity.add("Cần Thơ");

        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arrayServiceType);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        ArrayAdapter<String> arrayAdapter2= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arrayCity);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(arrayAdapter2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(AddStopPoint.this,arrayServiceType.get(position),Toast.LENGTH_SHORT).show();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            }
        });
        setArrTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar= Calendar.getInstance();
                int gio= calendar.get(Calendar.HOUR_OF_DAY);
                int phut= calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog= new TimePickerDialog(AddStopPoint.this, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        setArrTime.setText(hourOfDay+" : " +minute);
                    }
                },gio,phut,true);
                timePickerDialog.show();
            }
        });
        setLeaTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar= Calendar.getInstance();
                int gio= calendar.get(Calendar.HOUR_OF_DAY);
                int phut= calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog= new TimePickerDialog(AddStopPoint.this, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        setLeaTime.setText(hourOfDay+" : " +minute);
                    }
                },gio,phut,true);
                timePickerDialog.show();
            }
        });
        imDateArr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                milisecondArr = cal.getTimeInMillis();
                DatePickerDialog dialog = new DatePickerDialog(
                        AddStopPoint.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListenr, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        imDateLea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                milisecondLea = cal.getTimeInMillis();
                DatePickerDialog dialog = new DatePickerDialog(
                        AddStopPoint.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListenr2, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListenr = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);
                String date = dayOfMonth + "/" + month + "/" + year;
                setDateArr.setText(date);

            }
        };
        mDateSetListenr2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);
                String date = dayOfMonth + "/" + month + "/" + year;
                setDateLea.setText(date);

            }
        };
    }

   private void getCompenent(){
       address=(EditText)findViewById(R.id.etAddress);
       address.setText(getIntent().getStringExtra(StopPointActivity.ADDRESS));
       spinner=(Spinner) findViewById(R.id.spinnerServiceType);
       spinner2=(Spinner) findViewById(R.id.spinnerCity);
       imDateArr=(ImageView)findViewById(R.id.IMcalendarArr);
       imDateLea=(ImageView)findViewById(R.id.IMcalendarLeave);
       setDateArr=(TextView) findViewById(R.id.tvDateArrive);
       setDateLea=(TextView) findViewById(R.id.tvDateLeave);
       setArrTime=(TextView)findViewById(R.id.tvTimeArrive);
       setLeaTime=(TextView)findViewById(R.id.tvTimeLeave);
   }
}
