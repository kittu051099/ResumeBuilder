package com.example.resumebuilderdemo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;
import static com.example.resumebuilderdemo.SessionManager.USERID;

public class HomeActivity extends AppCompatActivity {
    ImageView BuildHere,DownloadHere;
    Button LogoutButton;
    TextView UserIDBox;
    SessionManager sessionManager;
    String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);
        boolean z=sessionManager.isLogin();
        if(!z)
        {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }
        HashMap<String, String> user = sessionManager.getUserDetails();
        UserID = user.get(USERID);

        BuildHere= findViewById(R.id.BuildHere);
        DownloadHere= findViewById(R.id.DownloadHere);
        LogoutButton= findViewById(R.id.LogoutButton);
        UserIDBox = findViewById(R.id.UserIDBox);

        /*-----------User ID Display to user ----------------*/
        UserIDBox.setText(UserID);

        /*------------------------user turn off his session here----------------------*/
        LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logout();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });
        /*------------------------end of user turn off his session here----------------*/

        /*-----------------------Resume build start here------------------------------*/
        BuildHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,PersonalActivity.class);
                startActivity(intent);
            }
        });
        /*-----------------------End of Resume build process------------------------------*/

        /*-----------------------Download resume process---------------------------------*/
        DownloadHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfDownload();
            }
        });
    }

    private void pdfDownload() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,

                Uri.parse("https://android-resume-builder-app.000webhostapp.com/demo/index.php?user_ID="+UserID));
        startActivity(browserIntent);
    }
}
