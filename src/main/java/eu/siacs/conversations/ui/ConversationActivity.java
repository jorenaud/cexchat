package eu.siacs.conversations.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import eu.siacs.conversations.Config;
import eu.siacs.conversations.R;
import eu.siacs.conversations.entities.Account;
import eu.siacs.conversations.services.XmppConnectionService;
import eu.siacs.conversations.utils.AccountUtils;
import eu.siacs.conversations.utils.SignupUtils;

import static eu.siacs.conversations.utils.SignupUtils.getSignUpIntent;

public class ConversationActivity extends XmppActivity {

    @Override
    protected void refreshUiReal() {

    }

    @Override
    void onBackendConnected() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
//                startActivity(new Intent(ConversationActivity.this, ConversationsActivity.class));
//                finish();

                startApp();
                finish();
            }
        }, 3000);


    }

    void startApp() {
        Intent intent;
        final XmppConnectionService service = xmppConnectionService;
        try {
            if (service.getAccounts().size() == 0 && !Config.X509_VERIFICATION && Config.MAGIC_CREATE_DOMAIN != null) {
                Log.e("CUSTOM----", "getSignUpIntent: ++++++  ===");
                intent = getSignUpIntent(ConversationActivity.this);

            } else {
                intent = new Intent(ConversationActivity.this, ConversationsActivity.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            intent = new Intent(ConversationActivity.this, ConversationsActivity.class);
        }

        startActivity(intent);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
