package eu.siacs.conversations.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.security.KeyChain;
import android.security.KeyChainAliasCallback;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import eu.siacs.conversations.Config;
import eu.siacs.conversations.R;
import eu.siacs.conversations.databinding.ActivityWelcomeBinding;
import eu.siacs.conversations.entities.Account;
import eu.siacs.conversations.services.XmppConnectionService;
import eu.siacs.conversations.utils.Compatibility;
import eu.siacs.conversations.utils.InstallReferrerUtils;
import eu.siacs.conversations.utils.SignupUtils;
import eu.siacs.conversations.utils.XmppUri;
import eu.siacs.conversations.xmpp.Jid;

import static eu.siacs.conversations.utils.PermissionUtils.allGranted;
import static eu.siacs.conversations.utils.PermissionUtils.writeGranted;

public class WelcomActivityNew extends XmppActivity implements XmppConnectionService.OnAccountCreated, KeyChainAliasCallback {
    TextView tv_signup, tv_privacy, tv_signin;
    public XmppConnectionService xmppConnectionService;
    private static final int REQUEST_IMPORT_BACKUP = 0x63fb;


    private XmppUri inviteUri;

    public static void launch(AppCompatActivity activity) {
        Intent intent = new Intent(activity, MagicCreateActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        addInviteUri(intent);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    @Override
    public void onStart() {
        super.onStart();
        final int theme = findTheme();
        if (this.mTheme != theme) {
            recreate();
        }
        new InstallReferrerUtils(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        UriHandlerActivity.onRequestPermissionResult(this, requestCode, grantResults);
        if (grantResults.length > 0) {
            if (allGranted(grantResults)) {
                switch (requestCode) {
                    case REQUEST_IMPORT_BACKUP:
                        startActivity(new Intent(this, ImportBackupActivity.class));
                        break;
                }
            } else if (Arrays.asList(permissions).contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, R.string.no_storage_permission, Toast.LENGTH_SHORT).show();
            }
        }
        if (writeGranted(grantResults, permissions)) {
            if (xmppConnectionService != null) {
                xmppConnectionService.restartFileObserver();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            setIntent(intent);
        }
    }

    private void processXmppUri(final XmppUri xmppUri) {
        if (!xmppUri.isValidJid()) {
            return;
        }
        final String preAuth = xmppUri.getParameter(XmppUri.PARAMETER_PRE_AUTH);
        final Jid jid = xmppUri.getJid();
        final Intent intent;
        if (xmppUri.isAction(XmppUri.ACTION_REGISTER)) {
            intent = SignupUtils.getTokenRegistrationIntent(this, jid, preAuth);
        } else if (xmppUri.isAction(XmppUri.ACTION_ROSTER) && "y".equals(xmppUri.getParameter(XmppUri.PARAMETER_IBR))) {
            intent = SignupUtils.getTokenRegistrationIntent(this, jid.getDomain(), preAuth);
            intent.putExtra(StartConversationActivity.EXTRA_INVITE_URI, xmppUri.toString());
        } else {
            intent = null;
        }
        if (intent != null) {
            startActivity(intent);
            finish();
            return;
        }
        this.inviteUri = xmppUri;
    }

    @Override
    protected void refreshUiReal() {

    }

    @Override
    void onBackendConnected() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_welcom_new);
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setSupportActionBar(findViewById(R.id.toolbar));
        configureActionBar(getSupportActionBar(), false);

        tv_signup = findViewById(R.id.tv_signup);
        tv_privacy = findViewById(R.id.tv_privacy);
        tv_signin = findViewById(R.id.tv_signin);

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(WelcomActivityNew.this, MagicCreateActivity.class));

                final Intent intent = new Intent(WelcomActivityNew.this, MagicCreateActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                addInviteUri(intent);
                startActivity(intent);

//                startActivity(new Intent(WelcomActivityNew.this, EditAccountActivity.class));

            }
        });

        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(WelcomActivityNew.this, EditAccountActivity.class));
//               Toast.makeText(getApplicationContext(),"sign in clicked",Toast.LENGTH_SHORT).show();

//                final List<Account> accounts = xmppConnectionService.getAccounts();
//                Intent intent = new Intent(WelcomActivityNew.this, EditAccountActivity.class);
//                intent.putExtra(EditAccountActivity.EXTRA_FORCE_REGISTER, false);
//                if (accounts.size() == 1) {
//                    intent.putExtra("jid", accounts.get(0).getJid().asBareJid().toString());
//                    intent.putExtra("init", true);
//                } else if (accounts.size() >= 1) {
//                    intent = new Intent(WelcomActivityNew.this, ManageAccountActivity.class);
//                }
//                addInviteUri(intent);

//                final List<Account> accounts = xmppConnectionService.getAccounts();
//                Log.e("KING123456", "onCreate: welcome lis " + accounts.toString());

                Intent intent = new Intent(WelcomActivityNew.this, EditAccountActivity.class);
                intent.putExtra(EditAccountActivity.EXTRA_FORCE_REGISTER, false);

//                intent.putExtra("jid", accounts.get(0).getJid().asBareJid().toString());
                intent.putExtra("init", true);
                intent.putExtra("signin", true);

                addInviteUri(intent);
                startActivity(intent);
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

        spannableString.setSpan(clickableSpan1, 9, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_privacy.setText(spannableString);

        tv_privacy.setMovementMethod(LinkMovementMethod.getInstance());


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.welcome_menu, menu);
        final MenuItem scan = menu.findItem(R.id.action_scan_qr_code);
        scan.setVisible(Compatibility.hasFeatureCamera(this));
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_import_backup:
                if (hasStoragePermission(REQUEST_IMPORT_BACKUP)) {
                    startActivity(new Intent(this, ImportBackupActivity.class));
                }
                break;
            case R.id.action_scan_qr_code:
                UriHandlerActivity.scan(this, true);
                break;
            case R.id.action_add_account_with_cert:
                addAccountFromKey();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addAccountFromKey() {
        try {
            KeyChain.choosePrivateKeyAlias(this, this, null, null, null, -1, null);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.device_does_not_support_certificates, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void alias(final String alias) {
        if (alias != null) {
            xmppConnectionService.createAccountFromKey(alias, this);
        }
    }

    @Override
    public void onAccountCreated(final Account account) {
        final Intent intent = new Intent(this, EditAccountActivity.class);
        intent.putExtra("jid", account.getJid().asBareJid().toEscapedString());
        intent.putExtra("init", true);
        addInviteUri(intent);
        startActivity(intent);
    }

    @Override
    public void informUser(final int r) {
        runOnUiThread(() -> Toast.makeText(this, r, Toast.LENGTH_LONG).show());
    }

//    public void addInviteUri(Intent to) {
//        final Intent from = getIntent();
//        if (from != null && from.hasExtra(StartConversationActivity.EXTRA_INVITE_URI)) {
//            final String invite = from.getStringExtra(StartConversationActivity.EXTRA_INVITE_URI);
//            to.putExtra(StartConversationActivity.EXTRA_INVITE_URI, invite);
//        } else if (this.inviteUri != null) {
//            Log.d(Config.LOGTAG, "injecting referrer uri into on-boarding flow");
//            to.putExtra(StartConversationActivity.EXTRA_INVITE_URI, this.inviteUri.toString());
//        }
//    }

    public void addInviteUri(Intent intent) {
        StartConversationActivity.addInviteUri(intent, getIntent());
    }
}