package com.example.e_commarce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.e_commarce.databinding.ActivityDetailProductBinding;
import com.squareup.picasso.Picasso;

public class detail_product extends AppCompatActivity {

    private Bundle bundel;
    private ActivityDetailProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        bundel = getIntent().getExtras();

        binding = ActivityDetailProductBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        setSupportActionBar(binding.actnProd);

        getSupportActionBar().setTitle("Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Double price = bundel.getDouble("price");
        Picasso.get().load(bundel.getString("img")).placeholder(R.drawable.loader).into(binding.img);
        binding.name.setText(bundel.getString("name"));
        binding.price.setText("₹"+price+" / ");
        binding.qnty.setText(bundel.getString("unit"));
        binding.description.setText(bundel.getString("description"));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);

    }
}