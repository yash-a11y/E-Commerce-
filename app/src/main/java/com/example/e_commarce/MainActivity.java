package com.example.e_commarce;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.e_commarce.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPreferences spf;
    private String emailcheck;
    private String passwordchek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


       binding = ActivityMainBinding.inflate(getLayoutInflater());


        super.onCreate(savedInstanceState);

        setContentView(binding.getRoot());

        //
        spf = getSharedPreferences(constsp.PREF,MODE_PRIVATE);

        emailcheck = spf.getString(constsp.email,"");
        passwordchek = spf.getString(constsp.password,"");


        //End


        binding.loginBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(binding.emailEdt.getText().toString().trim().equals(""))
                        {
                            binding.emailEdt.setError("Required");

                        }
                        else if(binding.passEdt.getText().toString().equalsIgnoreCase(""))
                        {
                            binding.passEdt.setError("Required");
                        }
                        else
                        {

                            if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                                new doLogin().execute();
                            } else {
                                new ConnectionDetector(MainActivity.this).connectiondetect();
                            }
                        }
                    }
                }
        );


        binding.singupbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new common_method(MainActivity.this,SingupActivity.class);
                    }
                }
        );



    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }


    private class doLogin extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("email", binding.emailEdt.getText().toString());
            hashMap.put("password", binding.passEdt.getText().toString());
            return new MakeServiceCall().makeServiceCall(consturl.getLoginurl(), MakeServiceCall.POST, hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if (object.getBoolean("Status") == true) {
                    new common_method(MainActivity.this, object.getString("Message"));
                    JSONArray array = object.getJSONArray("UserData");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        spf.edit().putString(constsp.id, jsonObject.getString("id")).commit();
                        spf.edit().putString(constsp.name, jsonObject.getString("name")).commit();
                        spf.edit().putString(constsp.email, jsonObject.getString("email")).commit();
                        spf.edit().putString(constsp.contact, jsonObject.getString("contact")).commit();
                        spf.edit().putString(constsp.password, jsonObject.getString("password")).commit();
                        spf.edit().putString(constsp.gender, jsonObject.getString("gender")).commit();
                        spf.edit().putString(constsp.city, jsonObject.getString("city")).commit();
                    }
                    new common_method(MainActivity.this,homepage.class);
                } else {
                    new common_method(MainActivity.this, object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                new common_method(MainActivity.this, e.getMessage());
            }

        }
    }
}