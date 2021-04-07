package eu.siacs.conversations.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import eu.siacs.conversations.R;

public class WelcomActivityNew extends AppCompatActivity {
TextView tv_signup,tv_privacy,tv_signin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom_new);

        tv_signup= findViewById(R.id.tv_signup);
        tv_privacy= findViewById(R.id.tv_privacy);
        tv_signin= findViewById(R.id.tv_signin);

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomActivityNew.this, MagicCreateActivity.class));

            }
        });

        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Toast.makeText(getApplicationContext(),"sign in clicked",Toast.LENGTH_SHORT).show();

            }
        });


        SpannableString spannableString = new SpannableString(getString(R.string.terms_condition));

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Uri uri = Uri.parse(getString(R.string.privacy_policy));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        };

        spannableString.setSpan(clickableSpan1, 9,24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_privacy.setText(spannableString);

        tv_privacy.setMovementMethod(LinkMovementMethod.getInstance());



    }
}