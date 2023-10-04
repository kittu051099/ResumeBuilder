package com.example.resumebuilderdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Map;

import static com.example.resumebuilderdemo.SessionManager.USERID;

public class EducationActivity extends AppCompatActivity {
    LinearLayout EduBox;
    Button EduNextButton,AddEdu;
    LinearLayout dynamicview;
    SessionManager sessionManager;
    String UserID;
    private String TAG= EducationActivity.class.getSimpleName();
    private static String URL_Read="https://android-resume-builder-app.000webhostapp.com/demo/EducationRead.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        UserID = user.get(USERID);

        EduBox = findViewById(R.id.EduBox);
        AddEdu = findViewById(R.id.AddEdu);
        EduNextButton =findViewById(R.id.EduNextButton);

        /*---------------------Checking Education if available in Database------*/
            ReadEducation();


        /*--------------------------------Add new Education Process--------------------*/
        AddEdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EducationActivity.this, ActivityFormEducation.class);
                intent.putExtra("course","");
                finish();
                startActivity(intent);
                startActivity(intent);
            }
        });

        /*------------------------------To Next Activity-----------------------------*/
        EduNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EducationActivity.this, WorkActivity.class);
                startActivity(intent);
            }
        });

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
                            String check = jsonObject.getString("response");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (check.equals("1")){

                                    dynamicview = findViewById(R.id.EduBox);
                                    LinearLayout.LayoutParams lprams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);

                                    final String[] strCourse = new String[jsonArray.length()];
                                    for(int i=0;i<jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        strCourse[i] = object.getString("Course").trim();

                                        Button Course = new Button(EducationActivity.this);
                                        //background color set R G Y combination
                                        Course.setBackgroundColor(2552150);
                                        Course.setId(i+1);
                                        final String CourseName = strCourse[i];
                                        Course.setText(strCourse[i]);
                                        Course.setLayoutParams(lprams);

                                        Course.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                Intent intent = new Intent(EducationActivity.this, ActivityFormEducation.class);
                                                intent.putExtra("course",CourseName);
                                                finish();
                                                startActivity(intent);
                                            }
                                        });
                                        dynamicview.addView(Course);
                                    }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(EducationActivity.this, "Error Reading Academic Details"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(EducationActivity.this, "Error Reading Academic Details"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("UserID", UserID);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
