package com.example.resumebuilderdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.resumebuilderdemo.SessionManager.USERID;

public class ActivityFormEducation extends AppCompatActivity {
    Button EduSubmit,CourseDelete;
    EditText EntCourse, EntNameUni, EntMark, EntPassYear;
    String getCourse;
    SessionManager sessionManager;
    String UserID;
    private String TAG= ActivityFormEducation.class.getSimpleName();
    private static String URL_Read="https://android-resume-builder-app.000webhostapp.com/demo/EucationFormRead.php";
    private static String URL_Write="https://android-resume-builder-app.000webhostapp.com/demo/EucationFromWrite.php";
    private static String URL_Delete="https://android-resume-builder-app.000webhostapp.com/demo/EducationDelete.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_education);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        UserID = user.get(USERID);

        EntCourse = findViewById(R.id.EntCourse);
        EntNameUni = findViewById(R.id.EntNameUni);
        EntMark = findViewById(R.id.EntMark);
        EntPassYear = findViewById(R.id.EntPassYear);
        EduSubmit = findViewById(R.id.EduSubmit);
        CourseDelete = findViewById(R.id.CourseDelete);

        /*-------------------------Read Education if already save in Database--------------*/
        Intent intent = getIntent();
        getCourse = intent.getStringExtra("course");
        ReadEducation();

        if (getCourse.isEmpty()){
            CourseDelete.setVisibility(View.GONE);
        }
        else{

            CourseDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeleteCourse();
                }
            });
        }

        /*-------------------------Submitting Education to Database-----------------------*/
        EduSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Course, University, Mark, Date;
                Course = EntCourse.getText().toString();
                University = EntNameUni.getText().toString();
                Mark = EntMark.getText().toString();
                Date = EntPassYear.getText().toString();

                if (!Course.isEmpty() && !University.isEmpty() && !Mark.isEmpty() && !Date.isEmpty()) {
                    FillEducationDetails(Course,University,Mark,Date);
                } else {
                    if (Course.isEmpty()) {
                        EntCourse.setError("Enter Course Name !!!");
                    }
                    if (University.isEmpty()) {
                        EntNameUni.setError("Enter University Name !!!");
                    }
                    if (Mark.isEmpty()) {
                        EntMark.setError("Enter Marks !!!");
                    }
                    if (Date.isEmpty()) {
                        EntPassYear.setError("Enter Date !!!");
                    }
                }
            }
        });
    }

    private void DeleteCourse() {
        final ProgressDialog progressDialog = new ProgressDialog(this );
        progressDialog.setMessage("loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Delete,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("response");

                            if (success.equals("1")){
                                Toast.makeText(ActivityFormEducation.this, "Deleted SuccessFully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ActivityFormEducation.this, EducationActivity.class);
                                finish();
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(ActivityFormEducation.this, "Error in Delete Work Details"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ActivityFormEducation.this, "Error in Delete Work Details"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("UserID", UserID);
                params.put("getCourse", getCourse);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void ReadEducation() {
        final ProgressDialog progressDialog = new ProgressDialog(this );
        progressDialog.setMessage("loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Read,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("response");

                            if (success.equals("1")){

                                    String Course = jsonObject.getString("Course");
                                    String University = jsonObject.getString("University");
                                    String Mark = jsonObject.getString("Mark");
                                    String Year = jsonObject.getString("Year");

                                    EntCourse.setText(Course);
                                    EntNameUni.setText(University);
                                    EntMark.setText(Mark);
                                    EntPassYear.setText(Year);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(ActivityFormEducation.this, "Error Reading Academic Details"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ActivityFormEducation.this, "Error Reading Academic Details"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("UserID", UserID);
                params.put("getCourse", getCourse);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void FillEducationDetails(final String Course, final String University, final String Mark, final String Date) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Write,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("response");
                            if(success.equals("1")) {
                                Toast.makeText(ActivityFormEducation.this,"Filled SuccessFully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ActivityFormEducation.this, EducationActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(ActivityFormEducation.this,"Try Again",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ActivityFormEducation.this," Error "+ e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityFormEducation.this,"Volley Error "+ error.toString(),Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("Course",Course);
                params.put("University",University);
                params.put("Mark",Mark);
                params.put("Date",Date);
                params.put("UserID", UserID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}