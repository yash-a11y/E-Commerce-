package com.example.e_commarce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.e_commarce.databinding.ActivitySingupBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.Signature;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class SingupActivity extends AppCompatActivity {

    private ActivitySingupBinding binding;
    private View view;
    private SharedPreferences spf;
    private String[] city = {"AHMEDABAD","NAVSARI","SURAT","RAJKOT","VALSAD"};

    public String gender;
    private String dob;
    private String city_name;

    private String email_val = "^[A-za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        binding = ActivitySingupBinding.inflate(getLayoutInflater());

        if(binding != null)
        {
             view = binding.getRoot();
        }

        //
            spf = getSharedPreferences(constsp.PREF,MODE_PRIVATE);

        //
        super.onCreate(savedInstanceState);
        setContentView(view);

        setSupportActionBar(binding.actnbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("SIGN UP");

        // setup Spinner

        ArrayAdapter<String> adapter = new ArrayAdapter<>(SingupActivity.this
                , android.R.layout.simple_spinner_item,
                city);

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        binding.citylist.setAdapter(adapter);




        //END

        //setup datepicker


        calendar = Calendar.getInstance();

                 // setup dateselectorlistner

        DatePickerDialog.OnDateSetListener dateselector = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                calendar.set(Calendar.YEAR,i);
                calendar.set(Calendar.MONTH,i1);
                calendar.set(Calendar.DAY_OF_MONTH,i2);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                binding.dob.setText(dateFormat.format(calendar.getTime()));

                //

                    dob = dateFormat.format(calendar.getTime()).toString();
                    spf.edit().putString(constsp.dob,dob).commit();

                //
            }
        };

                //select to popup datepicker dialog

        binding.dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(SingupActivity.this,dateselector,calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });



        //END

        //setup radiogroup listner

        binding.signupGender.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        int id = radioGroup.getCheckedRadioButtonId();
                        RadioButton active = findViewById(i);
                        gender = active.getText().toString();
                        spf.edit().putString("gender",gender).commit();

                    }
                }
        );

        //END

        //spinner onclick listner

        binding.citylist.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        city_name = city[i];
                        spf.edit().putString(constsp.city,city[i]);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                        new common_method(binding.getRoot(),"nothing selected");
                    }
                }
        );

        //END


        //validation of form


        binding.signbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if(binding.nameedt.getText().toString().equals(""))
                        {
                            binding.nameedt.setError("Required");
                        }
                        else if(binding.emailedt.getText().toString().trim().equals(""))
                        {
                            binding.emailedt.setError("required");
                        }
                        else if(binding.contactedt.getText().toString().trim().equals("") ||
                                binding.contactedt.getText().toString().trim().length() > 10 )
                        {
                            binding.contactedt.setError("contact Required");
                        }
                        else if(!binding.emailedt.getText().toString().trim()
                                .matches(email_val))
                        {
                            binding.emailedt.setError("correct one require");
                        }
                        else if(binding.passedt.getText().toString().equals(""))
                        {
                            binding.passedt.setError("required");
                        }
                        else if(binding.conpassedt.getText().toString().equalsIgnoreCase(binding.passedt.getText().toString()) == false)
                        {
                            binding.conpassedt.setError("confirm password");
                        }
                        else
                        {
                            new common_method(binding.getRoot(),"SIGNUP SUCCESSFULLY");
                            spf.edit().putString(constsp.email,binding.emailedt.getText().toString()).commit();
                            spf.edit().putString(constsp.password,binding.passedt.getText().toString()).commit();
                            spf.edit().putString(constsp.name,binding.nameedt.getText().toString()).commit();
                            spf.edit().putString(constsp.contact,binding.contactedt.getText().toString());

                            new common_method(SingupActivity.this,homepage.class);
                            if(new ConnectionDetector(SingupActivity.this).isConnectingToInternet())
                            {
                                new putsignupData().execute();
                            }
                            else
                            {
                                new ConnectionDetector(SingupActivity.this).connectiondetect();
                            }

                        }
                    }
                }
        );


        //END

        //login button listner

        binding.loginbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new common_method(SingupActivity.this,MainActivity.class);
                    }
                }
        );

        //END
    }


    // use for actionbar to handle back click event.
    private class putsignupData extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SingupActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("name",binding.nameedt.getText().toString());
            hashMap.put("email", binding.emailedt.getText().toString());
            hashMap.put("contact", binding.contactedt.getText().toString());
            hashMap.put("password", binding.passedt.getText().toString());
            hashMap.put("city", city_name);
            hashMap.put("dob", binding.dob.getText().toString());
            hashMap.put("gender", gender);
            return new MakeServiceCall().makeServiceCall(consturl.getSignupurl(), MakeServiceCall.POST, hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if (object.getBoolean("Status") == true) {
                    new common_method(SingupActivity.this, object.getString("Message"));
                    onBackPressed();
                } else {
                    new common_method(SingupActivity.this, object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                new common_method(SingupActivity.this, e.getMessage());
            }

        }
    }



}