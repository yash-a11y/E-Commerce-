package com.example.e_commarce;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class catagoryAdp extends RecyclerView.Adapter<Viewholder1> {

    private ArrayList<category_datamodel> catagorylist;
    private Context context;
    private SharedPreferences spf;
    public catagoryAdp(Context homepage, ArrayList<category_datamodel> categorydata) {

        this.catagorylist = categorydata;
        this.context = homepage;
        this.spf = context.getSharedPreferences(constsp.PREF,Context.MODE_PRIVATE);

    }

    @NonNull
    @Override
    public Viewholder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemcat,parent,false);

        return new Viewholder1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder1 holder, int position) {

        category_datamodel catobj = catagorylist.get(position);


        Picasso.get().load(catobj.getImg()).placeholder(R.drawable.loader2).into(holder.getImg());
        holder.getText().setText(catobj.getName());

        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    spf.edit().putString(constsp.catagoryid,catobj.getCatid()).commit();
                    new common_method(context,prod_View_all.class);

                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        return catagorylist.size();
    }


}


class Viewholder1 extends RecyclerView.ViewHolder
{
    private TextView text;
    private ImageView img;
    public Viewholder1(@NonNull View itemView) {
        super(itemView);

                 img = itemView.findViewById(R.id.grocery_img);
                 text = itemView.findViewById(R.id.grocery_name);
    }

    public ImageView getImg() {
        return img;
    }

    public TextView getText() {
        return text;
    }
}