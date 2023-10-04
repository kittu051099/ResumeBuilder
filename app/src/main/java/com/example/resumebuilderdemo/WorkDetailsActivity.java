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

public class WorkDetailsActivity extends AppCompatActivity {
    Button WorkSubmit,WorkDelete;
    EditText EntOrg, EntJob, EntExp, EntRole;
    String getJob;
    SessionManager sessionManager;
    String UserID;
    private String TAG= WorkDetailsActivity.class.getSimpleName();
    private static String URL_Read="https://android-resume-builder-app.000webhostapp.com/demo/WorkDetailsRead.php";
    private static String URL_Write="https://android-resume-builder-app.000webhostapp.com/demo/WorkDetailsWrite.php";
    private static String URL_Delete="https://android-resume-builder-app.000webhostapp.com/demo/WorkDelete.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_details);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        UserID = user.get(USERID);

        EntOrg = findViewById(R.id.EntOrg);
        EntJob = findViewById(R.id.EntJob);
        EntExp = findViewById(R.id.EntExp);
        EntRole = findViewById(R.id.EntRole);
        WorkSubmit = findViewById(R.id.WorkSubmit);
        WorkDelete = findViewById(R.id.WorkDelete);

        /*-------------------------Read Education if already save in Database--------------*/
        Intent intent = getIntent();
        getJob = intent.getStringExtra("job");
        ReadEducation();

        if (getJob.isEmpty()){
            WorkDelete.setVisibility(View.GONE);
        }
        else{

            WorkDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeleteWork();
                }
            });
        }

        /*-------------------------Submitting Education to Database-----------------------*/
        WorkSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Organization, Job, Experience, Role;
                Organization = EntOrg.getText().toString();
                Job = EntJob.getText().toString();
                Experience = EntExp.getText().toString();
                Role = EntRole.getText().toString();

                if (!Organization.isEmpty() && !Job.isEmpty() && !Experience.isEmpty() && !Role.isEmpty()) {
                    FillEducationDetails(Organization,Job,Experience,Role);
                } else {
                    if (Organization.isEmpty()) {
                        EntOrg.setError("Enter Organization Name !!!");
                    }
                    if (Job.isEmpty()) {
                        EntJob.setError("Enter Job Name !!!");
                    }
                    if (Experience.isEmpty()) {
                        EntExp.setError("Enter Experiences !!!");
                    }
                    if (Role.isEmpty()) {
                        EntRole.setError("Enter Role !!!");
                    }
                }
            }
        });
    }

    private void DeleteWork() {
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
                                Toast.makeText(WorkDetailsActivity.this, "Deleted SuccessFully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(WorkDetailsActivity.this, WorkActivity.class);
                                finish();
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(WorkDetailsActivity.this, "Error in Delete Work Details"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(WorkDetailsActivity.this, "Error in Delete Work Details"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("UserID", UserID);
                params.put("getJob", getJob);

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

                                String Organization = jsonObject.getString("Organization");
                                String Job = jsonObject.getString("Job");
                                String Experience = jsonObject.getString("Experience");
                                String Role = jsonObject.getString("Role");

                                EntOrg.setText(Organization);
                                EntJob.setText(Job);
                                EntExp.setText(Experience);
                                EntRole.setText(Role);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(WorkDetailsActivity.this, "Error Reading Work Details"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(WorkDetailsActivity.this, "Error Reading Work Details"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("UserID", UserID);
                params.put("getJob", getJob);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void FillEducationDetails(final String Organization, final String Job, final String Experience, final String Role) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Write,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("response");
                            if(success.equals("1")) {
                                Toast.makeText(WorkDetailsActivity.this,"Filled SuccessFully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(WorkDetailsActivity.this, WorkActivity.class);
                                finish();
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(WorkDetailsActivity.this,"Try Again",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(WorkDetailsActivity.this," Error "+ e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(WorkDetailsActivity.this,"Volley Error "+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("Organization",Organization);
                params.put("Job",Job);
                params.put("Experience",Experience);
                params.put("Role",Role);
                params.put("UserID", UserID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
