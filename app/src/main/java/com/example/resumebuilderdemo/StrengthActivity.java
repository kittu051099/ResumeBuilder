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

public class StrengthActivity extends AppCompatActivity {
    EditText EntFOI1,EntFOI2,EntSkill1,EntSkill2,EntStrength1,EntStrength2;
    Button StrengthSubmit;
    SessionManager sessionManager;
    String UserID;
    private String TAG= StrengthActivity.class.getSimpleName();
    private static String URL_Read="https://android-resume-builder-app.000webhostapp.com/demo/StrengthRead.php";
    private static String URL_Write="https://android-resume-builder-app.000webhostapp.com/demo/StrengthWrite.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strength);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        UserID = user.get(USERID);

        EntFOI1=findViewById(R.id.EntFOI1);
        EntFOI2=findViewById(R.id.EntFOI2);
        EntSkill1=findViewById(R.id.EntSkill1);
        EntSkill2=findViewById(R.id.EntSkill2);
        EntStrength1=findViewById(R.id.EntStrength1);
        EntStrength2=findViewById(R.id.EntStrength2);
        StrengthSubmit=findViewById(R.id.StrenthSubmit);

        /*---------------------Checking Strength if available in Database------*/
        ReadStrength();

        StrengthSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FOI1,FOI2,Skill1,Skill2,Strength1,Strength2;
                FOI1 = EntFOI1.getText().toString();
                FOI2 = EntFOI2.getText().toString();
                Skill1 =EntSkill1.getText().toString();
                Skill2 = EntSkill2.getText().toString();
                Strength1 = EntStrength1.getText().toString();
                Strength2 = EntStrength2.getText().toString();

                String FOI = FOI1 + "#" + FOI2;
                String Skill = Skill1 + "#" + Skill2;
                String Strength = Strength1 + "#" +Strength2;

                FillStrength(FOI,Skill,Strength);
            }
        });

    }

    private void ReadStrength() {

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

                                String FOI = jsonObject.getString("FOI");
                                String Skill = jsonObject.getString("Skill");
                                String Strength = jsonObject.getString("Strength");

                                String[] arrOfStr = FOI.split("#", 5);
                                String[] arrFOI = new String[5];
                                int j=0;
                                for (String a : arrOfStr) {
                                    arrFOI[j]=a;
                                    j++;
                                }

                                EntFOI1.setText(arrFOI[0]);
                                EntFOI2.setText(arrFOI[1]);

                                String[] arrOfStr1 = Skill.split("#", 5);
                                String[] arrSkill = new String[5];
                                 j=0;
                                for (String a : arrOfStr1) {
                                    arrSkill[j]=a;
                                    j++;
                                }

                                EntSkill1.setText(arrSkill[0]);
                                EntSkill2.setText(arrSkill[1]);

                                EntFOI1.setText(arrFOI[0]);
                                EntFOI2.setText(arrFOI[1]);

                                String[] arrOfStr2 = Strength.split("#", 5);
                                String[] arrStrength = new String[5];
                                 j=0;
                                for (String a : arrOfStr2) {
                                    arrStrength[j]=a;
                                    j++;
                                }

                                EntStrength1.setText(arrStrength[0]);
                                EntStrength2.setText(arrStrength[1]);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(StrengthActivity.this, "Error Reading Strength Details"+e.toString()+UserID, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(StrengthActivity.this, "Volley Error Reading Basic Details"+error.toString(), Toast.LENGTH_SHORT).show();
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

    private void FillStrength(final String FOI, final String Skill, final String Strength) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Write,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("response");
                            if(success.equals("1")) {
                                Toast.makeText(StrengthActivity.this,"Filled SuccessFully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(StrengthActivity.this, DeclarationActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(StrengthActivity.this,"Try Again",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(StrengthActivity.this," Error "+ e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StrengthActivity.this,"Volley Error "+ error.toString(),Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("Skill",Skill);
                params.put("FOI",FOI);
                params.put("Strength",Strength);
                params.put("UserID", UserID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
