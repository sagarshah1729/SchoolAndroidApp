package com.app.school.activity.modules;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.app.school.R;
import com.app.school.adapter.CircularAdapter;
import com.app.school.adapter.GalleryCategoryAdapter;
import com.app.school.bean.Circular;
import com.app.school.bean.GalleryCategoryDesc;
import com.app.school.bean.GalleryCategoryImages;
import com.app.school.bean.Web_API_Config;

public class GalleryModule extends AppCompatActivity {

    RecyclerView galCatRecyclerView;
    ProgressBar  progressBar;

    ArrayList<GalleryCategoryImages> galleryCategoryImagesArrayList;
    StaggeredGridLayoutManager mStaggeredLayoutManager;
    String TAG = "GalleryModule";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_module);

        galCatRecyclerView = (RecyclerView) findViewById(R.id.galCatRecycleView);
        progressBar        = (ProgressBar) findViewById(R.id.prgBar);
        Toolbar toolbar    = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        galleryCategoryImagesArrayList = new ArrayList<GalleryCategoryImages>();
        getSupportActionBar().setTitle("Gallery");


        getGalleryCategories();
    }


    public void getGalleryCategories()
    {
        String url = Web_API_Config.galleryCategoryAPI;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressBar.setVisibility(View.GONE);
                        galCatRecyclerView.setVisibility(View.VISIBLE);


                        Log.e(TAG, response.toString());
                        try {
                            String success = response.getString("Status");

                            if(success.equals("1"))
                            {

                                JSONArray jsonArray = response.getJSONArray("Gallery_Category_List");

                                for(int i=-0; i<jsonArray.length(); i++)
                                {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    GalleryCategoryImages galleryCategory = new GalleryCategoryImages();
                                    galleryCategory.setId(jsonObject.getString("Gallery_Category_Id"));
                                    galleryCategory.setCat_name(jsonObject.getString("Gallery_Category_Name"));
                                    galleryCategory.setImage_name(jsonObject.getString("Gallery_Category_Image"));
                                    galleryCategory.setCat_desc(jsonObject.getString("Gallery_Category_Description"));

                                    galleryCategoryImagesArrayList.add(galleryCategory);

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        galCatRecyclerView.setHasFixedSize(true);
                        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                        mStaggeredLayoutManager.setSpanCount(2);
                        galCatRecyclerView.setLayoutManager(mStaggeredLayoutManager);

                        GalleryCategoryAdapter galleryCategoryAdapter = new GalleryCategoryAdapter(galleryCategoryImagesArrayList, getApplicationContext());
                        galCatRecyclerView.setAdapter(galleryCategoryAdapter);


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);

    }
}
