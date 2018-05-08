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
import android.widget.TextView;
import android.provider.Settings.Secure;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.anton.election.Banner.BannerConst;
import com.example.anton.election.Banner.BannerInfo;
import com.example.anton.election.Banner.BannerView;


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
import static com.example.anton.election.NewAdapter.choiseCandidat;
import static java.security.AccessController.getContext;

public class General extends AppCompatActivity {

    private static final String TAG = General.class.getSimpleName();
   static ArrayList<Candidat> candidats = new ArrayList<Candidat>(7);
    public static final String fileName = "candidat.txt";

    static ListView lvMain;

    static TextView textView;

    static NewAdapter boxAdapter;

    int position = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_general);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.Liner);


        BannerView bannerView = new BannerView(General.this,linearLayout);

        bannerView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        linearLayout.addView(bannerView);


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

        for (int i = 0; i < 8; i++){

            candidats.add(new Candidat());

        }


        String url = "http://adlibtech.ru/elections/api/getcandidates.php";
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                JSONParser jsonParser = new JSONParser();

                try {
                    Object object = jsonParser.parse(response);

                    JSONArray jsonArray = (JSONArray) object;

                    JSONObject total = (JSONObject) jsonArray.get(8);

                    String totalVote = (String) total.get("total");

                    textView.setText(totalVote);



                    for (int i = 0; i < 8; i++) {

                        JSONObject candidat = (JSONObject) jsonArray.get(i);

                        String id = (String) candidat.get("id");

                        String firstname = (String) candidat.get("firstname");

                        String secondname = (String) candidat.get("secondname");

                        String thirdname = (String) candidat.get("thirdname");

                        String image = (String) candidat.get("image");

                        String descr = (String) candidat.get("description");

                        String votes = (String) candidat.get("votes");

                        String web = (String) candidat.get("web");

                        String party = (String) candidat.get("party");

                        candidats.set(Integer.parseInt(id) - 2,new Candidat(Integer.valueOf(id), firstname, secondname, thirdname, Integer.valueOf(votes), Double.valueOf(totalVote), descr, party, web, image));

                        Log.d("Response", firstname + " " + image);

                    }
                    boxAdapter = new NewAdapter(General.this, candidats);

                    lvMain.setAdapter(boxAdapter);


                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", "Error");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("device_id", "100");
                params.put("device_name", "Anton");

                return params;
            }
        };

        queue.add(jsObjRequest);



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







