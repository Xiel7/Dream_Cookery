package com.example.dream_cookery;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class editProfileView extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] gender = {"Male", "Female"};
    FirebaseAuth fBaseAuth;
    String currentID;
    private static final String TAG = editProfileView.class.getSimpleName();

    Button Save;
    EditText editName, editPhone;
    EditText editGender;
    TextView resultBirthday;
    String eName;
    String eBirthday;
    String eGender;
    int ePhone;


    private EditText mDisplayDate;
    private Button mChooseDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

        @Override
        protected void onCreate (Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.edit_profile);

            ImageButton backPress = findViewById(R.id.backEditProfile);
            backPress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(editProfileView.this, profilePageView.class);
                    startActivity(intent);
                }
            });

            //firebase
            fBaseAuth = FirebaseAuth.getInstance();
            currentID = fBaseAuth.getCurrentUser().getUid();

            //spinner for gender
            final Spinner spin = (Spinner) findViewById(R.id.gender);
            spin.setOnItemSelectedListener(this);

            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1, gender);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spin.setAdapter(aa);


            //display date to let user choose
            mDisplayDate = (EditText)findViewById(R.id.profile_birth);
            mChooseDate = (Button)findViewById(R.id.profile_birth_button);
            mChooseDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            editProfileView.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            mDateSetListener,
                            year,month,day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                }
            });

            mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;
                    Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                    String date = month + "/" + day + "/" + year;
                    mDisplayDate.setText(date);
                }
            };

            //shared preferences
            Save = (Button)findViewById(R.id.save_Button);
            editName = (EditText)findViewById(R.id.profile_name);
            editPhone = (EditText)findViewById(R.id.profile_phone);
            resultBirthday = (TextView) findViewById(R.id.profile_birth);


            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

            final String e_Name = prefs.getString("eName","");
            editName.setText(e_Name);
            int e_Phone = prefs.getInt("ePhone", 0);
            editPhone.setText(""+e_Phone);
            final String e_Birthday = prefs.getString("eBirthday","");
            resultBirthday.setText(""+e_Birthday);
            if(prefs.getString("eGender","")=="Female")
            {
                String temp = gender[0];
                gender[0] = gender[1];
                gender[1] = temp;
                aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1, gender);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(aa);

            }


            Save.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    eName = editName.getText().toString();
                    ePhone = Integer.parseInt(editPhone.getText().toString());
                    eBirthday = mDisplayDate.getText().toString();
                    eGender = spin.getSelectedItem().toString();
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(editProfileView.this);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("eName",eName);
                    editor.putInt("ePhone",ePhone);
                    editor.putString("eBirthday",eBirthday);
                    editor.putString("eGender",eGender);
                    editor.apply();


                    //firebase functions
                    FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference fReference;
                    fReference = fDatabase.getReference("Users").child(currentID);
                    fReference.child("username").setValue(eName);
                    fReference.child("phoneNumber").setValue(ePhone);
                    fReference.child("gender").setValue(eGender);
                    fReference.child("birthday").setValue(eBirthday);

                }
            });
        }


    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id)
    {
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0)
    {
    }
    
}
