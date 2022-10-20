package com.example.e_commarce;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;

import com.google.android.material.snackbar.Snackbar;

public class common_method {

    common_method(Context context, String msg)
    {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
    common_method(Context context,Class<?> nextclass)
    {
        Intent intent = new Intent(context,nextclass);
        context.startActivity(intent);
    }


    common_method(View view, String msg)
    {
        Snackbar.make(view,msg,Snackbar.LENGTH_SHORT).show();
    }
}
