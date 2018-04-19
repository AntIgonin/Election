package com.example.anton.election;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.provider.Settings.Secure;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.anton.election.NewAdapter.choiseCandidat;
import static java.security.AccessController.getContext;

public class General extends AppCompatActivity {

    private static final String TAG = General.class.getSimpleName();
    ArrayList<Candidat> candidats = new ArrayList<Candidat>();
    public static final String fileName = "candidat.txt";

    static ListView lvMain;

    static TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_general);

        FileInputStream fin = null;
        try {
            fin = openFileInput(fileName);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String(bytes);
            choiseCandidat = text;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        lvMain = (ListView) findViewById(R.id.lvMain);
        final RequestQueue queue = Volley.newRequestQueue(General.this);
        textView = (TextView) findViewById(R.id.textView2);

        VolleyReq volleyReq = new VolleyReq(candidats,General.this);

        volleyReq.ReqVolley(queue,lvMain);


        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {

                Intent intent = new Intent(General.this, InfoCandidat.class);

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("candidate", candidats);
                intent.putExtras(bundle);

                intent.putExtra("id", position );

                startActivity(intent);

            }
        });


    }


    @Override
    protected void onStop() {
        super.onStop();

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = openFileOutput(fileName, MODE_PRIVATE);

            fileOutputStream.write(choiseCandidat.getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}







