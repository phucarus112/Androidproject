package com.example.project.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.RetrofitClient;
import com.example.project.Data.Province;
import com.example.project.Data.ServiceType;
import com.example.project.R;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Retrofit;

public class EditStopPoint extends AppCompatActivity {

    private long milisecondArr, milisecondLea;
    private ImageView imDateArr,imDateLea;
    private TextView setDateArr,setDateLea,setArrTime,setLeaTime;
    private DatePickerDialog.OnDateSetListener mDateSetListenr, mDateSetListenr2;
    private EditText address,stopPointName,minCost,maxCost;
    private Spinner spinner,spinner2;
    ImageButton close;
    Button addSP;
    String Token;
    private long dateTimeArrival,dateTimeLeave;
    private String dateArrival = "",dateLeave="",timeArrival="",timeLeave="";
    ArrayList<Province> provinceArrayList= new ArrayList<>();
    ArrayList<ServiceType> serviceTypeArrayList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stop_point);
        getCompenent();
        Token = getIntent().getStringExtra("token");


        final ArrayList<String> arrayServiceType = getDataService();
        final ArrayList<String> arrayCity= getDataProvince();

        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arrayServiceType);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        ArrayAdapter<String> arrayAdapter2= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arrayCity);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(arrayAdapter2);

        assignValue();

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

                TimePickerDialog timePickerDialog= new TimePickerDialog(EditStopPoint.this, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        setArrTime.setText(hourOfDay+" : " +minute);
                        timeArrival = String.valueOf(hourOfDay);
                        if(minute<10)timeArrival+="0"+ String.valueOf(minute);
                        else timeArrival+=String.valueOf(minute);
                        timeArrival+="00";
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

                TimePickerDialog timePickerDialog= new TimePickerDialog(EditStopPoint.this, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        setLeaTime.setText(hourOfDay+" : " +minute);
                        timeLeave = String.valueOf(hourOfDay);
                        if(minute<10)timeLeave+="0"+ String.valueOf(minute);
                        else timeLeave+=String.valueOf(minute);
                        timeLeave+="00";
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
                        EditStopPoint.this,
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
                        EditStopPoint.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListenr2, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListenr = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                setDateArr.setText(date);

                dateArrival+=year;
                if(month <10)dateArrival+="0"+ String.valueOf(month);
                else dateArrival+= String.valueOf(month);
                if(dayOfMonth<10) dateArrival += "0"+String.valueOf(dayOfMonth);
                else dateArrival+=String.valueOf(dayOfMonth);
            }
        };
        mDateSetListenr2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                setDateLea.setText(date);

                dateLeave+=year;
                if(month <10)dateLeave+="0"+ String.valueOf(month);
                else dateLeave+= String.valueOf(month);
                if(dayOfMonth<10) dateLeave += "0"+String.valueOf(dayOfMonth);
                else dateLeave+=String.valueOf(dayOfMonth);
            }
        };

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = RetrofitClient.getClient();
                APIService apiService = retrofit.create(APIService.class);
            }
        });
    }

    private void assignValue() {
        //doc tu share pre xuong va gan cho tung field
        stopPointName.setText(getIntent().getStringExtra("nameSP"));
        address.setText(getIntent().getStringExtra("addressSP"));
        int type = Integer.parseInt(getIntent().getStringExtra("serviceSP"));
        spinner.setSelection(type-1);
        int province = Integer.parseInt(getIntent().getStringExtra("provinceSP"));
        spinner2.setSelection(province);
        minCost.setText(getIntent().getStringExtra("minCostSP"));
        maxCost.setText(getIntent().getStringExtra("maxCostSP"));
        String arrival = getIntent().getStringExtra("arrivalSP");
        Log.e("ji",arrival+arrival.charAt(7));
        setDateArr.setText(String.valueOf(arrival.charAt(6))+String.valueOf(arrival.charAt(7))+"/"+
                String.valueOf(arrival.charAt(4))+String.valueOf(arrival.charAt(5))+"/"+
                String.valueOf( arrival.charAt(0))+ String.valueOf( arrival.charAt(1))+
                String.valueOf( arrival.charAt(2))+ String.valueOf( arrival.charAt(3)));

        setArrTime.setText( String.valueOf( arrival.charAt(8))+ String.valueOf( arrival.charAt(9))+":"+
                String.valueOf( arrival.charAt(10))+ String.valueOf( arrival.charAt(11)));
        String leave = getIntent().getStringExtra("leaveSP");
        setDateLea.setText( String.valueOf( leave.charAt(6))+ String.valueOf( leave.charAt(7))+"/"+
                String.valueOf( leave.charAt(4))+ String.valueOf( leave.charAt(5))+"/"+
                String.valueOf(  leave.charAt(0))+ String.valueOf( leave.charAt(1))+
                String.valueOf( leave.charAt(2))+String.valueOf(leave.charAt(3)));

        setLeaTime.setText( String.valueOf( leave.charAt(8))+ String.valueOf( leave.charAt(9))+":"+
                String.valueOf( leave.charAt(10))+ String.valueOf( leave.charAt(11)));
        int pos = Integer.parseInt(getIntent().getStringExtra("index"));
    }

    private ArrayList<String> getDataService() {
        ArrayList<String> temp  =new ArrayList<>();

        serviceTypeArrayList.add(new ServiceType(1,"Restaurant"));
        serviceTypeArrayList.add(new ServiceType(2,"Hotel"));
        serviceTypeArrayList.add(new ServiceType(3,"Rest Station"));
        serviceTypeArrayList.add(new ServiceType(4,"Other"));

        for(int i=0;i<serviceTypeArrayList.size();i++)
        {
            temp.add(serviceTypeArrayList.get(i).getService_name());
        }

        return temp;
    }

    private ArrayList<String> getDataProvince() {
        ArrayList<String> temp  =new ArrayList<>();

        provinceArrayList.add(new Province(1,"Hồ Chí Minh","SG"));
        provinceArrayList.add(new Province(2,"Hà Nội","HN"));
        provinceArrayList.add(new Province(3,"Đà Nẵng","DDN"));
        provinceArrayList.add(new Province(4,"Bình Dương","BD"));
        provinceArrayList.add(new Province(5,"Đồng Nai","DNA"));
        provinceArrayList.add(new Province(6,"Khánh Hòa","KH"));
        provinceArrayList.add(new Province(7,"Hải Phòng","HP"));
        provinceArrayList.add(new Province(8,"Long An","LA"));
        provinceArrayList.add(new Province(9,"Quảng Nam","QNA"));
        provinceArrayList.add(new Province(10,"Bà Rịa Vũng Tàu","VT"));
        provinceArrayList.add(new Province(11,"Đắk Lắk","DDL"));
        provinceArrayList.add(new Province(12,"Cần Thơ","CT"));
        provinceArrayList.add(new Province(13,"Bình Thuận ","BTH"));
        provinceArrayList.add(new Province(14,"Lâm Đồng","LDD"));
        provinceArrayList.add(new Province(15,"Thừa Thiên Huế","TTH"));
        provinceArrayList.add(new Province(16,"Kiên Giang","KG"));
        provinceArrayList.add(new Province(17,"Bắc Ninh","BN"));
        provinceArrayList.add(new Province(18,"Quảng Ninh","QNI"));
        provinceArrayList.add(new Province(19,"Thanh Hóa","TH"));
        provinceArrayList.add(new Province(20,"Nghệ An","NA"));
        provinceArrayList.add(new Province(21,"Hải Dương","HD"));
        provinceArrayList.add(new Province(22,"Gia Lai","GL"));
        provinceArrayList.add(new Province(23,"Bình Phước","BP"));
        provinceArrayList.add(new Province(24,"Hưng Yên","HY"));
        provinceArrayList.add(new Province(25,"Bình Định","BDD"));
        provinceArrayList.add(new Province(26,"Tiền Giang","TG"));
        provinceArrayList.add(new Province(27,"Thái Bình","TB"));
        provinceArrayList.add(new Province(28,"Bắc Giang","BG"));
        provinceArrayList.add(new Province(29,"Hòa Bình","HB"));
        provinceArrayList.add(new Province(30,"An Giang","AG"));
        provinceArrayList.add(new Province(31,"Vĩnh Phúc","VP"));
        provinceArrayList.add(new Province(32,"Tây Ninh","TNI"));
        provinceArrayList.add(new Province(33,"Thái Nguyên","TN"));
        provinceArrayList.add(new Province(34,"Lào Cai","LCA"));
        provinceArrayList.add(new Province(35,"Nam Định","NDD"));
        provinceArrayList.add(new Province(36,"Quảng Ngãi","QNG"));
        provinceArrayList.add(new Province(37,"Bến Tre","BTR"));
        provinceArrayList.add(new Province(38,"Đắk Nông","DNO"));
        provinceArrayList.add(new Province(39,"Cà Mau","CM"));
        provinceArrayList.add(new Province(40,"Vĩnh Long","VL"));
        provinceArrayList.add(new Province(41,"Ninh Bình","NB"));
        provinceArrayList.add(new Province(42,"Phú Thọ","PT"));
        provinceArrayList.add(new Province(43,"Ninh Thuận","NT"));
        provinceArrayList.add(new Province(44,"Phú Yên ","PY"));
        provinceArrayList.add(new Province(45,"Hà Nam","HNA"));
        provinceArrayList.add(new Province(46,"Hà Tĩnh","HT"));
        provinceArrayList.add(new Province(47,"Đồng Tháp","DDT"));
        provinceArrayList.add(new Province(48,"Sóc Trăng","ST"));
        provinceArrayList.add(new Province(49,"Kon Tum","KT"));
        provinceArrayList.add(new Province(50,"Quảng Bình","QB"));
        provinceArrayList.add(new Province(51,"Quảng Trị","QT"));
        provinceArrayList.add(new Province(52,"Trà Vinh","TV"));
        provinceArrayList.add(new Province(53,"Hậu Giang","HGI"));
        provinceArrayList.add(new Province(54,"Sơn La","SL"));
        provinceArrayList.add(new Province(55,"Bạc Liêu","BL"));
        provinceArrayList.add(new Province(56,"Yên Bái","YB"));
        provinceArrayList.add(new Province(57,"Tuyên Quang","TQ"));
        provinceArrayList.add(new Province(58,"Điện Biên","DDB"));
        provinceArrayList.add(new Province(59,"Lai Châu","LCH"));
        provinceArrayList.add(new Province(60,"Lạng Sơn","LS"));
        provinceArrayList.add(new Province(61,"Hà Giang","HG"));
        provinceArrayList.add(new Province(62,"Bắc Kạn","BK"));
        provinceArrayList.add(new Province(63,"Cao Bằng","CB"));

        for(int i=0;i<provinceArrayList.size();i++)
        {
            temp.add(provinceArrayList.get(i).getName());
        }
        return temp;
    }

    private void getCompenent(){
        stopPointName = (EditText) findViewById(R.id.etStopPointEdit);
        address=(EditText)findViewById(R.id.etAddressEdit);
        spinner=(Spinner) findViewById(R.id.spinnerServiceTypeEdit);
        spinner2=(Spinner) findViewById(R.id.spinnerCityEdit);
        imDateArr=(ImageView)findViewById(R.id.IMcalendarArrEdit);
        imDateLea=(ImageView)findViewById(R.id.IMcalendarLeaveEdit);
        setDateArr=(TextView) findViewById(R.id.tvDateArriveEdit);
        setDateLea=(TextView) findViewById(R.id.tvDateLeaveEdit);
        setArrTime=(TextView)findViewById(R.id.tvTimeArriveEdit);
        setLeaTime=(TextView)findViewById(R.id.tvTimeLeaveEdit);
        close = (ImageButton)findViewById(R.id.inforStopPointCloseEdit);
        addSP = (Button) findViewById(R.id.btnAddStopPointEdit);
        minCost = (EditText) findViewById(R.id.etMinCostEdit);
        maxCost = (EditText) findViewById(R.id.etMaxCostEdit);
    }
}
