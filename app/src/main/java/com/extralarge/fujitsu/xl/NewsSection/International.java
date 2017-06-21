package com.extralarge.fujitsu.xl.NewsSection;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.extralarge.fujitsu.xl.R;
import com.extralarge.fujitsu.xl.ReporterSection.AppController;
import com.extralarge.fujitsu.xl.ReporterSection.CustomListAdapter;
import com.extralarge.fujitsu.xl.ReporterSection.Movie;
import com.extralarge.fujitsu.xl.ReporterSection.NewsDetailShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class International extends Fragment implements AdapterView.OnItemClickListener{

    private static final String TAG = International.class.getSimpleName();

    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView listView;
    private CustomListAdapter adapter;
    int strtext;
    Movie movie;

    String  type,headline,content,caption,image;

    public International() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_social, container, false);

        listView = (ListView) view.findViewById(R.id.listvery);
        adapter = new CustomListAdapter(getContext(), movieList);
        listView.setAdapter(adapter);


        pDialog = new ProgressDialog(getContext());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // changing action bar color
//        getActionBar().setBackgroundDrawable(
//                new ColorDrawable(Color.parseColor("#1b1b1b")));

        // Creating volley request obj

        populatedata();
        listView.setOnItemClickListener(this);


        return  view;
    }

    public void populatedata(){

        final String url = "http://api.minews.in/slimapp/public/api/posts/approved/International";


        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);

                                Movie movie = new Movie();

                                String imagestr = obj.getString("image");
                                String imagrurl = "http://minews.in/storage/app/public/uploads/";
                                String imageurlfull = imagrurl+imagestr;

                                movie.setTitle(obj.getString("headline"));
                                movie.setThumbnailUrl(imageurlfull);
                                movie.setRating(obj.getString("content"));

                                movie.setYear(obj.getString("category"));
                                movie.setGenre(obj.getString("created_at"));
                                movie.setId(obj.getString("id"));

                                movieList.add(movie);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

        if(movieList!=null) movieList.clear();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Movie mo123 = (Movie) parent.getItemAtPosition(position);

        Intent newsdetailintnt = new Intent(getContext(),NewsDetailShow.class);
        newsdetailintnt.putExtra("type",mo123.getYear());
        newsdetailintnt.putExtra("headline",mo123.getTitle());
        newsdetailintnt.putExtra("content",mo123.getRating());
        newsdetailintnt.putExtra("image",mo123.getThumbnailUrl());
        newsdetailintnt.putExtra("id",mo123.getId());

        startActivity(newsdetailintnt);

    }

}
