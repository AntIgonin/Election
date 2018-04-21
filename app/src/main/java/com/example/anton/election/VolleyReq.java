package com.example.anton.election;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.anton.election.General.boxAdapter;
import static com.example.anton.election.General.candidats;
import static com.example.anton.election.General.textView;


public class VolleyReq {



    Context context;


    VolleyReq(Context context){

        this.context = context;

    }
    String url = "http://adlibtech.ru/elections/api/getcandidates.php";
    public void UpdateVolley(RequestQueue queue, final ListView lvMain) {

        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                JSONParser jsonParser = new JSONParser();

                try {
                    Object object = jsonParser.parse(response);

                    JSONArray jsonArray = (JSONArray) object;

                    for (int i = 0; i < 8; i++) {

                        JSONObject candidat = (JSONObject) jsonArray.get(i);

                        String votes = (String) candidat.get("votes");

                        String id = (String) candidat.get("id");

                        candidats.get(Integer.parseInt(id) - 2).votes = Integer.valueOf(votes);


                    }

                    boxAdapter.notifyDataSetChanged();

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

    }

}
