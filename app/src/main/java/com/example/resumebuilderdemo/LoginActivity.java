package com.example.resumebuilderdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {
    EditText LogMobile,LogPassword;
    Button RegisterButton,LogButton;
    private static String URL_Login = "https://android-resume-builder-app.000webhostapp.com/demo/login.php";

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        LogMobile=findViewById(R.id.LogMobile);
        LogPassword=findViewById(R.id.LogPassword);
        RegisterButton=findViewById(R.id.RegButton);
        LogButton=findViewById(R.id.Logbutton);

        /*--------------------move to Registration Activity---------------*/
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        /*------------------End of move to Registration Activity----------*/

        /*----------------------------Login Process-----------------------*/
        LogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Mobile, Password;
                Mobile= LogMobile.getText().toString();
                Password=LogPassword.getText().toString();

                int PassLen = Password.length();
                double MobLen = Mobile.length();

                if (!Mobile.isEmpty() && MobLen==10 && !Password.isEmpty() && PassLen>5){
                    /*-----Call to Login Method*/
                        Login(Mobile,Password);
                }
                else {
                    if (Mobile.isEmpty() || MobLen < 10 || MobLen > 10){
                        LogMobile.setError("Incorrect Mobile No. !!!");
                    }
                    if(Password.isEmpty()){
                            LogPassword.setError("Incorrect Password !!!");
                    }
                }
            }
        });
        /*----------------------------End of Login Process----------------*/
    }

    /*-------------------------- On back button press ----------------------*/
    @Override
    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Are you really want to EXIT");
        builder.setCancelable(true);
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("YES !", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    /*-------------------------- End of back button press process ----------------------*/

    /*-------------------------Login method-----------------------------*/
    private void Login(final String Mobile, final String Password) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String check = jsonObject.getString("response");

                            if (check.equals("1"))
                            {
                                String UserID = jsonObject.getString("UserID");
                                Toast.makeText(LoginActivity.this,"Success LOGIN"+UserID, Toast.LENGTH_SHORT).show();

                                sessionManager.createSession(UserID);

                                    /*----- after successful login call to home page-----------*/

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                finishAffinity();
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(LoginActivity.this,"LOGIN Failed, Try Again...!!!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Error"+ e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Error Volley"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();

                params.put("mobile", Mobile);
                params.put("password", Password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
