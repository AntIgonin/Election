package com.example.anton.election;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;


import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;

import static com.example.anton.election.General.lvMain;


public class NewAdapter extends BaseAdapter implements View.OnClickListener {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Candidat> objects;

    int[] idElections = new int[2];

    public static String choiseCandidat;

    RequestQueue queue;

    final String urlImage = "http://adlibtech.ru/elections/upload_images/";


    NewAdapter(Context context, ArrayList<Candidat> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        queue = Volley.newRequestQueue(ctx);
    }


    @Override
    public int getCount() {
        return objects.size();
    }


    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        final VolleyReq volleyReq = new VolleyReq(ctx);

        final Candidat c = getCandidat(position);

        final View finalView = view;

        ((TextView) view.findViewById(R.id.textView4)).setText(c.firstname);
        ((TextView) view.findViewById(R.id.textView3)).setText(c.secondname);
        ((TextView) view.findViewById(R.id.textView8)).setText(c.thirdname);


        final TextView textView2 = view.findViewById(R.id.textid);
        textView2.setText(c.image);
        textView2.setVisibility(View.GONE);

        ((TextView) view.findViewById(R.id.textView5)).setText("Голосов : "+c.votes);

        ((TextView) view.findViewById(R.id.textView6)).setText(String.valueOf(((c.totalVote)/100)*Double.valueOf(c.votes))+" % ");



            ImageRequest imageRequest = new ImageRequest(urlImage + c.image, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {

                    if (c.image == textView2.getText()) {
                        ((ImageView) finalView.findViewById(R.id.imageView3)).setImageBitmap(bitmap);
                    }

                }
            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("Response", "Не полетели!");
                }
            });

            queue.add(imageRequest);

        final ImageView imageView = (ImageView) view.findViewById(R.id.imageView4);
        final ImageButton imageButton = (ImageButton) view.findViewById(R.id.imageButton);
        final TextView textView = view.findViewById(R.id.textView3);

        imageButton.setFocusable(false);

        if (choiseCandidat != null){
            if (choiseCandidat.equals(textView.getText())){ imageView.setVisibility(View.VISIBLE); idElections[0] = c.id;}
            else {imageView.setVisibility(View.GONE);}
        }

        Log.d("Choise",choiseCandidat);



        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                idElections[1] = idElections[0];

                idElections[0] = c.id;

                imageView.setVisibility(View.VISIBLE);

                notifyDataSetChanged();

                choiseCandidat = (String) textView.getText();

                imageView.setTag(choiseCandidat);

                Log.d("Election", String.valueOf(idElections[0]) + " Текущий выбор " +String.valueOf(idElections[1]) +" Предыдущий выбор ");

                String url = "http://adlibtech.ru/elections/api/addvote.php";

                StringRequest myReq = new StringRequest(Request.Method.POST,url,

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {

                                volleyReq.UpdateVolley(queue,lvMain);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                volleyError.fillInStackTrace();
                            }
                        })
                {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("device_id", "100");
                        params.put("device_name", "Anton");
                        params.put("candidate_id", String.valueOf(idElections[0]));
                        params.put("last_id", String.valueOf(idElections[1]));

                        return params;


                    }
                };

                queue.add(myReq);


            }
        });

        return view;

}


    Candidat getCandidat(int position) {
        return  ((Candidat) getItem(position));
    }

    @Override
    public void onClick(View v) {


    }
}
