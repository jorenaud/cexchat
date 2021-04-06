package eu.siacs.conversations.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import eu.siacs.conversations.R;

public class WelcomActivityNew extends AppCompatActivity {
TextView tv_agree;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom_new);

        tv_agree= findViewById(R.id.tv_agree);

        tv_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomActivityNew.this, WelcomeActivity.class));
                finish();
            }
        });

    }
}