package com.example.youtuebapijava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<Model> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new Adapter(MainActivity.this,list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        fetchdata();
    }

    private void fetchdata(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UCe-XYtlVB58YcMCNnaq0nvA&maxResults=30&key=YOUR_API_KEY",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("items");
                            Log.i("msg","len "+jsonArray.length());
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                JSONObject jsonvideoid = jsonObject1.getJSONObject("id");
                                Log.i("msg","id "+jsonArray.getJSONObject(i));
                                JSONObject jsonsnippet = jsonObject1.getJSONObject("snippet");
                                JSONObject jsonthumbnail = jsonsnippet.getJSONObject("thumbnails").getJSONObject("medium");


                                Model md = new Model();
                                if( i!= 1 && i!=2 ){
                                    md.setVideoId(jsonvideoid.getString("videoId"));
                                    Log.i("msg","Id "+jsonvideoid.getString("videoId"));
                                    md.setTitle(jsonsnippet.getString("title"));
                                    md.setUrl(jsonthumbnail.getString("url"));
                                    list.add(md);
                                }

                            }
                            if(list.size() > 0){
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Error!!",Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(stringRequest);
    }




}