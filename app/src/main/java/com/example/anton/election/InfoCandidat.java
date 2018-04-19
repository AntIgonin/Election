package com.example.anton.election;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class InfoCandidat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_info_candidat);

        final String urlImage = "http://adlibtech.ru/elections/upload_images/";
        RequestQueue queue = Volley.newRequestQueue(InfoCandidat.this);

        Intent intent = getIntent();

        int id = getIntent().getExtras().getInt("id");

        ArrayList<Candidat> filelist =
                (ArrayList<Candidat>)getIntent().getSerializableExtra("candidate");

       final TextView secondText = (TextView) findViewById(R.id.textView7);
       final TextView firstText = (TextView) findViewById(R.id.textView10);
       final TextView thirdText = (TextView) findViewById(R.id.textView11);
       final TextView email = (TextView) findViewById(R.id.textView13);
       final TextView partyText = (TextView) findViewById(R.id.textView12);


       final TextView descrText = (TextView) findViewById(R.id.textView15);

    if(filelist != null) {

        String secondname = (String) filelist.get(id).secondname;

        String descr = (String) filelist.get(id).description;

        String firstname = (String)  filelist.get(id).firstname;

        String thirdname = (String) filelist.get(id).thirdname;

        String web = (String)  filelist.get(id).web;

        String party = (String)  filelist.get(id).party;

        final String image = (String)  filelist.get(id).image;

        ImageRequest imageRequest = new ImageRequest(urlImage + image , new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {

                ImageView imageView = (ImageView) findViewById(R.id.imageView5);
                imageView.setImageBitmap(bitmap);

            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Response", "Не полетели!");
            }
        });
        queue.add(imageRequest);

        secondText.setText(secondname);

        firstText.setText(firstname + " ");

        thirdText.setText("      " + thirdname);

        email.setText(web);

        descrText.setText(descr);

        partyText.setText("Партия : "+party);

    }

    }

}
