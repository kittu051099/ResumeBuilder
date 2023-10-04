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

public class DeclarationActivity extends AppCompatActivity {
    EditText EntObjective, EntDeclaration, EntDate, EntPlace;
    Button DclSubmit;
    SessionManager sessionManager;
    String UserID;
    private String TAG= DeclarationActivity.class.getSimpleName();
    private static String URL_Read="https://android-resume-builder-app.000webhostapp.com/demo/DeclarationRead.php";
    private static String URL_Write="https://android-resume-builder-app.000webhostapp.com/demo/DeclarationWrite.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declaration);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        UserID = user.get(USERID);

        EntObjective=findViewById(R.id.EntObjective);
        EntDeclaration=findViewById(R.id.EntDeclaration);
        EntDate=findViewById(R.id.EntDate);
        EntPlace=findViewById(R.id.EntPlace);
        DclSubmit=findViewById(R.id.DclSubmit);

        /*----------------- Reading Declataion Details if available ------------------*/

            ReadDeclaration();

        /*----------------------------------------------------------------------------*/

        /*---------------Submiting Education Details-------------------------------*/

        DclSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Objective,Declaration,Date,Place;

                Objective = EntObjective.getText().toString();
                Declaration = EntDeclaration.getText().toString();
                Date = EntDate.getText().toString();
                Place = EntPlace.getText().toString();

                if (!Objective.isEmpty() && !Declaration.isEmpty() && !Date.isEmpty() && !Place.isEmpty()){
                    FillDeclarationDetails(Objective,Declaration,Date,Place);
                }
                else{
                    if(Objective.isEmpty()){
                        EntObjective.setError("Enter Your Objetive !!!");
                    }
                    if (Declaration.isEmpty()){
                        EntDeclaration.setError("Enter Declaration !!!");
                    }
                    if (Date.isEmpty()){
                        EntDate.setError("Enter Date !!!");
                    }
                    if (Place.isEmpty()){
                        EntPlace.setError("Enter Place !!!");
                    }
                }
            }
        });

    }

    private void ReadDeclaration() {

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

                            if (check.equals("1")){

                                String Objective = jsonObject.getString("Objective");
                                String Declaration = jsonObject.getString("Declaration");
                                String Date = jsonObject.getString("Date");
                                String Place = jsonObject.getString("Place");

                                EntObjective.setText(Objective);
                                EntDeclaration.setText((Declaration));
                                EntDate.setText(Date);
                                EntPlace.setText(Place);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(DeclarationActivity.this, "Error Reading ", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(DeclarationActivity.this, "Volley Error Reading"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("UserID", UserID);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void FillDeclarationDetails(final String Objective, final String Declaration, final String Date, final String Place) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Write,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("response");
                            if(success.equals("1")) {
                                Toast.makeText(DeclarationActivity.this,"Filled SuccessFully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DeclarationActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(DeclarationActivity.this,"Try Again",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DeclarationActivity.this," Error "+ e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DeclarationActivity.this,"Volley Error "+ error.toString(),Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("Objective",Objective);
                params.put("Declaration",Declaration);
                params.put("Date",Date);
                params.put("Place",Place);
                params.put("UserID", UserID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
