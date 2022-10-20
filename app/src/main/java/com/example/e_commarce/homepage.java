package com.example.e_commarce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.e_commarce.databinding.ActivityHomepageBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class homepage extends AppCompatActivity {

    private SharedPreferences spf;
    private ActivityHomepageBinding binding;
//    private ArrayList<consat_catagory_data.datamodel> constdata;

    private ArrayList<category_datamodel> categorydata;
    private catagoryAdp adaptercat;
    private prodAdp adapterprod;
    private ArrayList<Productdata_model> productlist;
    //
    private RecyclerView catrecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        binding = ActivityHomepageBinding.inflate(getLayoutInflater());

        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        spf = getSharedPreferences(constsp.PREF,MODE_PRIVATE);



        setSupportActionBar(binding.actnbar);
        getSupportActionBar().setTitle("DASHBOARD");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        constdata = new consat_catagory_data().datalist();


        // recyclerView set for product
        catrecycler = findViewById(R.id.home_recyclerview);

        binding.homeProductRecyclerview.setLayoutManager(new LinearLayoutManager(homepage.this,LinearLayoutManager.HORIZONTAL,false));
        binding.homeProductRecyclerview.setItemAnimator(new DefaultItemAnimator());
        binding.homeProductRecyclerview.setNestedScrollingEnabled(false);

        // recyclerView set for catgory
        catrecycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        catrecycler.setItemAnimator(new DefaultItemAnimator());




        catrecycler.setNestedScrollingEnabled(false);
        //END

        //view all click listener


        binding.homeViewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            spf.edit().putString(constsp.catagoryid,"").commit();
                            new common_method(homepage.this,prod_View_all.class);

                    }
                }
        );


        //END

        //start using API

        if(new ConnectionDetector(homepage.this).isConnectingToInternet())
        {
                new getCategoryData().execute();
                new getprod().execute();

        }
        else
        {
            new common_method(binding.getRoot(),"connected with network");
            new ConnectionDetector(homepage.this).connectiondetect();
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.actionbar_button,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int Id = item.getItemId();

        if(Id == android.R.id.home)
        {
            onBackPressed();
        }
        if(Id == R.id.logout)
        {
            spf.edit().clear().commit();
            new common_method(homepage.this,splashActivity.class);
            finish();
        }
        if(Id == R.id.chat)
        {
          new common_method(homepage.this,ChatActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        alertmethod();
    }

    //create alert dialog
    private void alertmethod() {

        AlertDialog.Builder builder = new AlertDialog.Builder(homepage.this);

        builder.setCancelable(false);
        builder.setTitle("EXIT !");
        builder.setIcon(R.drawable.warning);
        builder.setMessage("ARE TO SURE TO EXIT ?");
        builder.setNeutralButton("Rate US", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new common_method(homepage.this,"rating of app");
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                finishAffinity();
                dialogInterface.dismiss();
            }
        });


        builder.show();
    }

    //data fetching from REST API

    private class getCategoryData extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(homepage.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return new MakeServiceCall().makeServiceCall(consturl.getCategoryurl(), MakeServiceCall.GET, new HashMap<>());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {

                Log.i("fetchdata",s);
                JSONObject object = new JSONObject(s);

                if (object.getBoolean("Status")) {
                    JSONArray jsonArray = object.getJSONArray("CategoryData");
                    categorydata = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        category_datamodel list = new category_datamodel();
                        list.setName(jsonObject.getString("name"));
                        list.setImg(jsonObject.getString("image"));
                        list.setCatid(jsonObject.getString("id"));

                        categorydata.add(list);
                    }

                    adaptercat = new catagoryAdp(homepage.this,categorydata);
                    binding.homeRecyclerview.setAdapter(adaptercat);


                } else {
                    new common_method(homepage.this, object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                new common_method(homepage.this, e.getMessage());
            }
        }
    }


    private class getprod extends AsyncTask<String,String,String>
    {

        ProgressDialog pd;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(homepage.this);
            pd.setCancelable(false);
            pd.setMessage("please wait ...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            return new MakeServiceCall().makeServiceCall(consturl.getProducturl(),MakeServiceCall.GET,new HashMap<>());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();

            try {
                JSONObject object = new JSONObject(s);

                if(object.getBoolean("Status"))
                {
                    productlist = new ArrayList<Productdata_model>();

                    JSONArray obj = object.getJSONArray("ProductData");
                    for(int i=0;i<obj.length();i++)
                    {
                        Productdata_model item = new Productdata_model();
                        JSONObject objitem = obj.getJSONObject(i);

                        item.setCategory_id(objitem.getInt("categoryId"));
                        item.setDescription(objitem.getString("description"));
                        item.setName(objitem.getString("name"));
                        item.setUnit(objitem.getString("unit"));
                        item.setPrice(objitem.getDouble("price"));
                        item.setImage(objitem.getString("image"));
                        productlist.add(item);
                    }
                    adapterprod = new prodAdp(productlist,homepage.this, R.layout.itemprod);
                    binding.homeProductRecyclerview.setAdapter(adapterprod);

                }
                else
                {
                    new common_method(homepage.this,object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}