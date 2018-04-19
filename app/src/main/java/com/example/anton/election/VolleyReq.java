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

import static com.example.anton.election.General.textView;


public class VolleyReq {

    String url = "http://adlibtech.ru/elections/api/getcandidates.php";
    ArrayList<Candidat> candidats = new ArrayList<Candidat>();
    Context context;


    VolleyReq(ArrayList candidats,Context context){

        this.candidats = candidats;

        this.context = context;

    }

    public void ReqVolley(RequestQueue queue, final ListView lvMain) {

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

                        candidats.add(new Candidat(Integer.valueOf(id), firstname, secondname, thirdname, Integer.valueOf(votes), Double.valueOf(totalVote), descr, party, web, image));

                        Log.d("Response", firstname + " " + image);

                    }

                    NewAdapter boxAdapter = new NewAdapter(context, candidats);

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


    }

}
