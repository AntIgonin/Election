package com.example.anton.election;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.provider.Settings.Secure;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.anton.Banner.BannerView;


import org.json.JSONException;
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

import android.view.ViewGroup.LayoutParams;

import static com.android.volley.VolleyLog.wtf;
import static com.example.anton.election.NewAdapter.choiseCandidat;
import static java.security.AccessController.getContext;

public class General extends AppCompatActivity {

    private static final String TAG = General.class.getSimpleName();
   static ArrayList<Candidat> candidats = new ArrayList<Candidat>(7);
    public static final String fileName = "candidat.txt";
    String HOST_NAME = "http://adlibtech.ru";

    static ListView lvMain;

    static TextView textView;

    static NewAdapter boxAdapter;

    public static final String HOST = "http://adlibtech.ru/elections";

    BannerView bannerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_general);

        Log.d("Bundle",getPackageName());


        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.Rel) ;
        bannerView = new BannerView(General.this);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        layoutParams.topMargin = 700;
        bannerView.setLayoutParams(layoutParams);

        relativeLayout.addView(bannerView);

        bannerView.start();

     for(int i = 0; i < 8 ;i++){
            candidats.add(new Candidat());
            wtf("Candidat","Add new");
        }

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

        work();

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
        bannerView.stop();
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

    @Override

    protected void onResume() {

        super.onResume();

        if (bannerView.getIsRunning() != true){bannerView.start();}

        Log.i(TAG, "onResume()");

    }



    private void work() {



        String url = HOST + "/api/getcandidates.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Lenght: ", String.valueOf(response.length()));
                        Log.d("Response", response);

                        jsonDidLoaded(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("device_id", "TEST_ANDROID_ID");
                params.put("device_name", "TEST_ANDROID_NAME");

                return params;
            }
        };
        queue.add(postRequest);
    }

    private void jsonDidLoaded(String response) {
        System.out.println(response);

        if (isJSONValid(response)) {
            try {
                org.json.JSONArray object = new org.json.JSONArray(response);
                org.json.JSONObject totalVote = object.getJSONObject(8);
                double total = Double.parseDouble((String) totalVote.get("total"));
                TextView textView = (TextView) findViewById(R.id.textView2);
                textView.setText((String) totalVote.get("total"));
                for (int i = 0; i < object.length() - 1; i++) {
                    org.json.JSONObject c = object.getJSONObject(i);
                    loadImageWithUrl((String) c.get("image"));
                    Log.d("Candidate: ", c.get("secondname").toString() + " " + c.get("firstname").toString());
                    candidats.set(c.getInt("id") - 2,new Candidat(c,total));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            boxAdapter = new NewAdapter(General.this, candidats);
            lvMain.setAdapter(boxAdapter);
        }


    }

    private void loadImageWithUrl(String imageUrl) {
        ImageLoader imageLoader = VolleySingleton.getInstance(this).getImageLoader();
        imageUrl = HOST + "/upload_images/" + imageUrl;
        //Log.d("MAIN", "imageUrl = " + imageUrl);
        imageLoader.get(imageUrl, new ImageLoader.ImageListener() {

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() !=null) {
                    Log.d("Image loaded size: ", String.valueOf(response.getBitmap().getByteCount()));
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error image loading", error.toString());
            }
        });
    }

    public boolean isJSONValid(String test) {
        try {
            new org.json.JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new org.json.JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }



}







