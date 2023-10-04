package com.example.resumebuilderdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class PersonalActivity extends AppCompatActivity {
    EditText EntName,EntAddress,EntDOB,EntNationality,EntLanguages;
    RadioGroup radioGroup;
    RadioButton EntMale,EntFemale,radioButton;
    Button PerSubSubmit;
    SessionManager sessionManager;
    String UserID;
    private String TAG= PersonalActivity.class.getSimpleName();
    private static String URL_Read="https://android-resume-builder-app.000webhostapp.com/demo/PersonalRead.php";
    private static String URL_Write="https://android-resume-builder-app.000webhostapp.com/demo/PersonalWrite.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        UserID = user.get(USERID);

        EntName=findViewById(R.id.EntName);
        EntAddress=findViewById(R.id.EntAddress);
        EntDOB=findViewById(R.id.EntDOB);
        EntNationality=findViewById(R.id.EntNationality);
        EntLanguages=findViewById(R.id.EntLanguages);
        radioGroup=findViewById(R.id.radioGroup);
        EntMale=findViewById(R.id.EntMale);
        EntFemale=findViewById(R.id.EntFemale);
        PerSubSubmit=findViewById(R.id.PerSubButton);

        /*-------------------------Method to read details from Databse---------------------*/
            ReadPersonalDetails();

        /*------------------------Method to submit details to database----------------------*/
        PerSubSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name,Address,Gender,DOB,Nationality,Languages;
                Name = EntName.getText().toString();
                Address= EntAddress.getText().toString();
                DOB = EntDOB.getText().toString();
                Nationality = EntNationality.getText().toString();
                Languages= EntLanguages.getText().toString();

                int radioID = radioGroup.getCheckedRadioButtonId();
                radioButton =findViewById(radioID);
                Gender = radioButton.getText().toString();


                if (!Name.isEmpty() && !Address.isEmpty()&& !Gender.isEmpty() && !DOB.isEmpty() && !Nationality.isEmpty() && !Languages.isEmpty()){
                    FillPersonalDetails(Name,Address,Gender,DOB,Nationality,Languages);
                }
                else {
                    if(Name.isEmpty()){
                        EntName.setError("Enter Name");
                    }
                    if (Address.isEmpty()){
                        EntAddress.setError("Enter Address");
                    }
                    if (DOB.isEmpty()){
                        EntDOB.setError("Enter Date Of Birth");
                    }
                    if (Nationality.isEmpty()) {
                        EntNationality.setError("Enter Nationality");
                    }
                    if (Languages.isEmpty()){
                        EntLanguages.setError("Enter Language's You can understand");
                    }
                    }
            }
        });

    }

    /*-------------------------------------Methods----------------------------*/

    /*----------------------Reading Details Method----------------------------*/
    private void ReadPersonalDetails() {

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

                                    String Name = jsonObject.getString("name");
                                    String Address = jsonObject.getString("address");
                                    String Gender = jsonObject.getString("gender");
                                    String DOB = jsonObject.getString("dob");
                                    String Nationality = jsonObject.getString("nationality");
                                    String Languages = jsonObject.getString("languages");

                                    EntName.setText(Name);
                                    EntAddress.setText((Address));
                                    EntDOB.setText(DOB);
                                    EntNationality.setText(Nationality);
                                    EntLanguages.setText(Languages);
                                    if (Gender.equals("Male")){
                                        EntMale.setChecked(true);
                                    }
                                    else{
                                        EntFemale.setChecked(true);
                                    }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(PersonalActivity.this, "Error Reading Basic Details"+e.toString()+UserID, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(PersonalActivity.this, "Volley Error Reading Basic Details"+error.toString(), Toast.LENGTH_SHORT).show();
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

    /*----------------------Writing Details Method---------------------------*/
    private void FillPersonalDetails(final String Name, final String Address, final String Gender, final String DOB, final String Nationality, final String Languages) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Write,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("response");
                            if(success.equals("1")) {
                                Toast.makeText(PersonalActivity.this,"Filled SuccessFully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PersonalActivity.this, EducationActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(PersonalActivity.this,"Try Again",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PersonalActivity.this," Error "+ e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PersonalActivity.this,"Volley Error "+ error.toString(),Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("Name",Name);
                params.put("Address",Address);
                params.put("DOB",DOB);
                params.put("Nationality",Nationality);
                params.put("Languages",Languages);
                params.put("Gender",Gender);
                params.put("UserID", UserID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
