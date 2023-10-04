package com.example.resumebuilderdemo;

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
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    EditText RegMobile,RegMail,RegPassword;
    Button RegButton,LogButton;
    private static String URL_Register = "https://android-resume-builder-app.000webhostapp.com/demo/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RegMobile=findViewById(R.id.RegMobile);
        RegMail=findViewById(R.id.RegMail);
        RegPassword=findViewById(R.id.RegPassword);
        RegButton=findViewById(R.id.RegButton);
        LogButton=findViewById(R.id.Logbutton);

     /*Start of Move From Register Activity to Login Activity */
        LogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
     /*End of Move From Register Activity to Login Activity */

        /*Start of Registration Process*/
            RegButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String Mobile,Mail,Password;

                    Mobile = RegMobile.getText().toString();
                    Mail = RegMail.getText().toString();
                    Password = RegPassword.getText().toString();

                    int PassLen = Password.length();
                    double MobLen = Mobile.length();

                    if (!Mobile.isEmpty() && MobLen==10 && isValid(Mail) && !Password.isEmpty() && PassLen>5 ) {
                        /*call to registration method*/
                            Register(Mobile,Mail,Password);
                    }
                    else {
                        if (Mobile.isEmpty() || MobLen < 10 || MobLen > 10){
                            RegMobile.setError("Please Enter Valid Mobile No. !!!");
                        }
                        if (Mail.isEmpty() || !isValid(Mail)){
                                RegMail.setError("Please Enter Valid Mail Address !!!");
                        }
                        if (Password.isEmpty() || PassLen<6){
                                    RegPassword.setError("Password Length at least 6 !!!");
                        }
                    }
                }
            });
        /*End of Registration Process*/
    }

    /*-----------------------Methods Are here----------------------*/

    /*-----------------Registration Method-------------------------*/
    private void Register(final String Mobile, final String Mail, final String Password) {


        StringRequest stringRequest = new StringRequest( Request.Method.POST, URL_Register,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("response");
                            if(success.equals("1")) {
                                Toast.makeText(MainActivity.this,"Register Success",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                            }
                            else{
                                if(success.equals("0")){
                                Toast.makeText(MainActivity.this,"Already Register",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                Toast.makeText(MainActivity.this,"Register Failed",Toast.LENGTH_SHORT).show();
                                    }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this,"Register Error "+ e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Register Volley Error "+ error.toString(),Toast.LENGTH_SHORT).show();
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mobile",Mobile);
                params.put("mail",Mail);
                params.put("password", Password);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    /*-----------------End of Registration Method------------------*/

    /*-----------------Method To validation of Mail----------------*/
    private static boolean isValid(String Mail){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                             "[a-zA-Z0-9_+&*-]+)*@" +
                              "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                               "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (Mail == null)
            return false;
        return pat.matcher(Mail).matches();
    }
    /*---------------End of Method To validation of Mail------------*/

    /*----------------------- End of Methods ----------------------*/
}
