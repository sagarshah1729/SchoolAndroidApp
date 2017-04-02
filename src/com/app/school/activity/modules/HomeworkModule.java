package com.app.school.activity.modules;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.app.school.R;
import com.app.school.adapter.SubjectAdapter;
import com.app.school.adapter.SubjectAdapterForHomework;
import com.app.school.bean.Subject;
import com.app.school.bean.Web_API_Config;
import com.app.school.sqlite_db.DBHelper;

public class HomeworkModule extends AppCompatActivity {


    private ProgressBar        progressBar;
    private RecyclerView       subjectRecycleView;
    Toolbar                    toolbar;
    ArrayList<Subject>         subject_list;
    StaggeredGridLayoutManager mStaggeredLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_module);

        subjectRecycleView = (RecyclerView) findViewById(R.id.subjectRecycleView);
        progressBar        = (ProgressBar) findViewById(R.id.prgBar);
        toolbar            = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("Subjects");

        subject_list = new ArrayList<Subject>();
        getAllSubjects();


    }

    public void getAllSubjects()
    {

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String url = Web_API_Config.homework_subjects_API + dbHelper.getPersonalUserId();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        progressBar.setVisibility(View.GONE);
                        Log.d("Subjects Response : ", response.toString());

                        for(int i=0; i<response.length(); i++)
                        {
                            try {
                                JSONObject jsonObject = (JSONObject) response.get(i);

                                int sub_id      = Integer.parseInt(jsonObject.getString("Subject_Id"));
                                String sub_name = jsonObject.getString("Subject");
                                String sub_type = "Primary";

                                Subject subject = new Subject();
                                subject.setId(sub_id);
                                subject.setName(sub_name);
                                subject.setType(sub_type);

                                subject_list.add(subject);

                                setItemToRecycleView();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });


        Volley.newRequestQueue(this).add(jsonArrayRequest);

    }



    public void setItemToRecycleView()
    {
        subjectRecycleView.setVisibility(View.VISIBLE);
        subjectRecycleView.setHasFixedSize(true);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(1);
        subjectRecycleView.setLayoutManager(mStaggeredLayoutManager);
        SubjectAdapterForHomework subjectAdapterForHomework = new SubjectAdapterForHomework(subject_list, getApplicationContext());
        subjectRecycleView.setAdapter(subjectAdapterForHomework);
    }


}
