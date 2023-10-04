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

public class WorkActivity extends AppCompatActivity {
    LinearLayout WorkBox;
    Button WorkNextButton,AddWork;
    LinearLayout dynamicWorkview;
    SessionManager sessionManager;
    String UserID;
    private String TAG= WorkActivity.class.getSimpleName();
    private static String URL_Read="https://android-resume-builder-app.000webhostapp.com/demo/WorkRead.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        UserID = user.get(USERID);

        WorkBox = findViewById(R.id.WorkBox);
        AddWork = findViewById(R.id.AddWork);
        WorkNextButton =findViewById(R.id.WorkNextButton);

        /*---------------------Checking Education if available in Database------*/
        ReadWork();


        /*--------------------------------Add new Education Process--------------------*/
        AddWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkActivity.this, WorkDetailsActivity.class);
                intent.putExtra("job","");
                finish();
                startActivity(intent);
                startActivity(intent);
            }
        });

        /*------------------------------To Next Activity-----------------------------*/
        WorkNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkActivity.this,StrengthActivity.class);
                startActivity(intent);
            }
        });

    }

    private void ReadWork() {

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

                                dynamicWorkview = findViewById(R.id.WorkBox);
                                LinearLayout.LayoutParams lprams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);

                                final String[] strJob = new String[jsonArray.length()];
                                for(int i=0;i<jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    strJob[i] = object.getString("Job").trim();

                                    Button Job = new Button(WorkActivity.this);
                                    //RGY COLOR COMBINATION USED HERE 255 215 0
                                    Job.setBackgroundColor(2552150);
                                    Job.setId(i+1);
                                    final String JobName = strJob[i];
                                    Job.setText(strJob[i]);
                                    Job.setLayoutParams(lprams);

                                    Job.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(WorkActivity.this, WorkDetailsActivity.class);
                                            intent.putExtra("job",JobName);
                                            startActivity(intent);
                                        }
                                    });
                                    dynamicWorkview.addView(Job);
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(WorkActivity.this, "Error Reading Work Details"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(WorkActivity.this, "Error Reading Work Details"+error.toString(), Toast.LENGTH_SHORT).show();
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
