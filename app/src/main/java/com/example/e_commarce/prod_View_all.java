package com.example.e_commarce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.e_commarce.databinding.ActivityProdViewAllBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class prod_View_all extends AppCompatActivity {


    private ActivityProdViewAllBinding binding;
    private prodAdp adapter;
    private ArrayList<Productdata_model> productlist;
    private SharedPreferences spf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        binding = ActivityProdViewAllBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        setSupportActionBar(binding.prodToolBar);

        getSupportActionBar().setTitle("Products");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spf = getSharedPreferences(constsp.PREF,MODE_PRIVATE);
        //recyclerView


        binding.prodRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.prodRecyclerview.setItemAnimator(new DefaultItemAnimator());

        //viewall


        if (new ConnectionDetector(prod_View_all.this).isConnectingToInternet()) {
                new get_all_product().execute();
        } else {
            new ConnectionDetector(prod_View_all.this).connectiondetect();
        }

        //END

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class get_all_product extends AsyncTask<String, String, String> {
        ProgressDialog pd;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(prod_View_all.this);
            pd.setCancelable(false);
            pd.setMessage("please wait ...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            if(spf.getString(constsp.catagoryid,"").equalsIgnoreCase("")) {
                return new MakeServiceCall().makeServiceCall(consturl.getProducturl(), MakeServiceCall.GET, new HashMap<>());
            }
            else
            {
                HashMap map = new HashMap();
                map.put("categoryId",spf.getString(constsp.catagoryid,""));
                return new MakeServiceCall().makeServiceCall(consturl.getCatprod(),MakeServiceCall.POST,map);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();

            try {
                JSONObject object = new JSONObject(s);

                if (object.getBoolean("Status")) {
                    productlist = new ArrayList<Productdata_model>();

                    JSONArray obj = object.getJSONArray("ProductData");
                    for (int i = 0; i < obj.length(); i++) {
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
                    adapter = new prodAdp(productlist, prod_View_all.this, R.layout.itemallprod);
                    binding.prodRecyclerview.setAdapter(adapter);
                } else {
                    new common_method(prod_View_all.this, object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}