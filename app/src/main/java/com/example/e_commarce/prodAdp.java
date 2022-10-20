package com.example.e_commarce;

import android.content.Context;
import android.content.Intent;
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

public class prodAdp extends RecyclerView.Adapter<viewholder> {

    private ArrayList<Productdata_model> catagorylist;
    private Context context;
    private int layout;

    public prodAdp(@NonNull ArrayList<Productdata_model> catagory, Context context, int layout)
    {
        this.catagorylist = catagory;
        this.context = context;
        this.layout = layout;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout,parent,false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder,int position) {

        Productdata_model datamodel = catagorylist.get(position);

        Picasso.get().load(datamodel.getImage()).placeholder(R.drawable.loader2).into(holder.getImg());
        holder.getText().setText(datamodel.getName());
        holder.getPqnty().setText(context.getResources().getString(R.string.rupi) + datamodel.getPrice());

        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Bundle bundle = new Bundle();

                        bundle.putString("description",datamodel
                                .getDescription());
                        bundle.putString("name",datamodel
                                .getName());
                        bundle.putString("img",datamodel.getImage());
                        bundle.putDouble("price",datamodel.getPrice());
                        bundle.putString("unit",datamodel.getUnit());

                        Intent intent = new Intent(holder.itemView.getContext(),detail_product.class);


                        intent.putExtras(bundle);
                        holder.itemView.getContext().startActivity(intent);

                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        return catagorylist.size();
    }
}

class viewholder extends RecyclerView.ViewHolder
{
    private TextView text;
    private ImageView img;
    private TextView pqnty;
    public viewholder(@NonNull View itemView) {
        super(itemView);

                 img = itemView.findViewById(R.id.grocery_img);
                 text = itemView.findViewById(R.id.grocery_name);
                 pqnty = itemView.findViewById(R.id.grocery_price_qntity);
    }

    public ImageView getImg() {
        return img;
    }

    public TextView getText() {
        return text;
    }

    public TextView getPqnty() {
        return pqnty;
    }
}