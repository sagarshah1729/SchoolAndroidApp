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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.app.school.R;
import com.app.school.adapter.ExamsListAdapter;
import com.app.school.adapter.GalleryCategoryAdapter;
import com.app.school.adapter.SubjectAdapterForHomework;
import com.app.school.bean.Exam;
import com.app.school.bean.GalleryCategoryImages;
import com.app.school.bean.Subject;
import com.app.school.bean.Web_API_Config;
import com.app.school.sqlite_db.DBHelper;

public class ResultModule extends AppCompatActivity {

    private ProgressBar        progressBar;
    private RecyclerView       examsRecycleView;
    private Toolbar            toolbar;
    ArrayList<Exam>            exam_list;
    StaggeredGridLayoutManager mStaggeredLayoutManager;

    String TAG = "ResultModule";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_module);

        progressBar      = (ProgressBar) findViewById(R.id.prgBar);
        examsRecycleView = (RecyclerView) findViewById(R.id.examsRecycleView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("Result(Exams)");

        exam_list = new ArrayList<Exam>();

        getExamsList();
    }



    public void getExamsList()
    {

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String url = Web_API_Config.exam_list_API + dbHelper.getClassId();

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressBar.setVisibility(View.GONE);
                        examsRecycleView.setVisibility(View.VISIBLE);


                        Log.e(TAG, response.toString());
                        try {
                            String success = response.getString("Success");

                            if(success.equals("1"))
                            {

                                JSONArray jsonArray = response.getJSONArray("Exam_List");

                                for(int i=-0; i<jsonArray.length(); i++)
                                {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String examTermId    = jsonObject.getString("Exam_Term_Id");
                                    String examTermName  = jsonObject.getString("Term_Name");

                                    Exam exam = new Exam();
                                    exam.setExamTermId(Integer.parseInt(examTermId));
                                    exam.setExamTermName(examTermName);

                                    exam_list.add(exam);


                                }
                                setItemToRecycleView();
                            }else{
                                new SweetAlertDialog(ResultModule.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("No Result Available")
                                        .setContentText("Please Try After Exam")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);

    }



    public void setItemToRecycleView()
    {
        examsRecycleView.setVisibility(View.VISIBLE);
        examsRecycleView.setHasFixedSize(true);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(1);
        examsRecycleView.setLayoutManager(mStaggeredLayoutManager);
        ExamsListAdapter examsListAdapter = new ExamsListAdapter(exam_list, getApplicationContext(), ResultModule.this);
        examsRecycleView.setAdapter(examsListAdapter);
    }





}
