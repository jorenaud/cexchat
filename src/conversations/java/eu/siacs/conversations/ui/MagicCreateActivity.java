package eu.siacs.conversations.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import eu.siacs.conversations.Config;
import eu.siacs.conversations.R;
import eu.siacs.conversations.SmsListener;
import eu.siacs.conversations.SmsReceiver;
import eu.siacs.conversations.databinding.MagicCreateBinding;
import eu.siacs.conversations.entities.Account;
import eu.siacs.conversations.utils.CryptoHelper;
import eu.siacs.conversations.utils.InstallReferrerUtils;
import eu.siacs.conversations.xmpp.Jid;

public class MagicCreateActivity extends XmppActivity implements TextWatcher, OnCountryPickerListener {

    public static final String EXTRA_DOMAIN = "domain";
    public static final String EXTRA_PRE_AUTH = "pre_auth";
    public static final String EXTRA_USERNAME = "username";


    private MagicCreateBinding binding;
    private String domain;
    private String username;
    private String preAuth;
    private FirebaseAuth mAuth;

    @Override
    protected void refreshUiReal() {

    }

    @Override
    void onBackendConnected() {

    }

    @Override
    public void onStart() {
        super.onStart();
        final int theme = findTheme();
        if (this.mTheme != theme) {
            recreate();
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        final Intent data = getIntent();
        this.domain = data == null ? null : data.getStringExtra(EXTRA_DOMAIN);
        this.preAuth = data == null ? null : data.getStringExtra(EXTRA_PRE_AUTH);
        this.username = data == null ? null : data.getStringExtra(EXTRA_USERNAME);
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }


        super.onCreate(savedInstanceState);

        mAuth=FirebaseAuth.getInstance();
        this.binding = DataBindingUtil.setContentView(this, R.layout.magic_create);

        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                binding.etOtp.setText(messageText);
            }
        });

        binding.tvVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode(binding.etOtp.getText().toString());
            }
        });

        intializeCountryPicker();

        binding.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoveToVerify();
            }
        });


        SpannableString spannableString = new SpannableString(getString(R.string.resend_otp));

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
//             Resend OTP
                Log.e("KING----", "onClick: "+phoneNum +"-----"+resendToken);
                resendVerificationCode(phoneNum,resendToken);
                binding.tvResendOTP.setText(getString(R.string.otp_resend_success));
            }
        };

        spannableString.setSpan(clickableSpan1, 16, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.tvResendOTP.setText(spannableString);

        binding.tvResendOTP.setMovementMethod(LinkMovementMethod.getInstance());


        binding.tvCountryPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker();
            }
        });
        setSupportActionBar(this.binding.toolbar);
        configureActionBar(getSupportActionBar(), this.domain == null);
        if (username != null && domain != null) {
            binding.title.setText(R.string.your_server_invitation);
            binding.instructions.setText(getString(R.string.magic_create_text_fixed, domain));
            binding.finePrint.setVisibility(View.INVISIBLE);
            binding.username.setEnabled(false);
            binding.username.setText(this.username);
            updateFullJidInformation(this.username);
        } else if (domain != null) {
            binding.instructions.setText(getString(R.string.magic_create_text_on_x, domain));
            binding.finePrint.setVisibility(View.INVISIBLE);
        }
        binding.createAccount.setOnClickListener(v -> {
            try {
                final String username = binding.username.getText().toString();
                final Jid jid;
                final boolean fixedUsername;
                if (this.domain != null && this.username != null) {
                    fixedUsername = true;
                    jid = Jid.ofLocalAndDomainEscaped(this.username, this.domain);
                } else if (this.domain != null) {
                    fixedUsername = false;
                    jid = Jid.ofLocalAndDomainEscaped(username, this.domain);
                } else {
                    fixedUsername = false;
                    jid = Jid.ofLocalAndDomainEscaped(username, Config.MAGIC_CREATE_DOMAIN);
                }
                if (!jid.getEscapedLocal().equals(jid.getLocal()) || (this.username == null && username.length() < 3)) {
                    binding.username.setError(getString(R.string.invalid_username));
                    binding.username.requestFocus();
                } else {
                    binding.username.setError(null);
                    Account account = xmppConnectionService.findAccountByJid(jid);
                    if (account == null) {
                        account = new Account(jid, CryptoHelper.createPassword(new SecureRandom()));
                        account.setOption(Account.OPTION_REGISTER, true);
                        account.setOption(Account.OPTION_DISABLED, true);
                        account.setOption(Account.OPTION_MAGIC_CREATE, true);
                        account.setOption(Account.OPTION_FIXED_USERNAME, fixedUsername);
                        if (this.preAuth != null) {
                            account.setKey(Account.PRE_AUTH_REGISTRATION_TOKEN, this.preAuth);
                        }
                        xmppConnectionService.createAccount(account);
                    }
                    Intent intent = new Intent(MagicCreateActivity.this, EditAccountActivity.class);
                    intent.putExtra("jid", account.getJid().asBareJid().toString());
                    intent.putExtra("init", true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Toast.makeText(MagicCreateActivity.this, R.string.secure_password_generated, Toast.LENGTH_SHORT).show();
                    StartConversationActivity.addInviteUri(intent, getIntent());
                    startActivity(intent);
                }
            } catch (IllegalArgumentException e) {
                binding.username.setError(getString(R.string.invalid_username));
                binding.username.requestFocus();
            }
        });
        binding.username.addTextChangedListener(this);
    }



    CountryPicker countryPicker;

    public void intializeCountryPicker() {
        CountryPicker.Builder builder =
                new CountryPicker.Builder().with(MagicCreateActivity.this)
                        .listener(MagicCreateActivity.this);

        countryPicker = builder.build();
    }

    private void showPicker() {

//            countryPicker.showBottomSheet(MainActivity.this);
        countryPicker.showDialog(MagicCreateActivity.this);

    }


    @Override
    public void onDestroy() {
        InstallReferrerUtils.markInstallReferrerExecuted(this);
        super.onDestroy();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(final Editable s) {
        updateFullJidInformation(s.toString());
    }

    private void updateFullJidInformation(final String username) {
        if (username.trim().isEmpty()) {
            binding.fullJid.setVisibility(View.INVISIBLE);
        } else {
            try {
                binding.fullJid.setVisibility(View.VISIBLE);
                final Jid jid;
                if (this.domain == null) {
                    jid = Jid.ofLocalAndDomainEscaped(username, Config.MAGIC_CREATE_DOMAIN);
                } else {
                    jid = Jid.ofLocalAndDomainEscaped(username, this.domain);
                }
                binding.fullJid.setText(getString(R.string.your_full_jid_will_be, jid.toEscapedString()));
            } catch (IllegalArgumentException e) {
                binding.fullJid.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onSelectCountry(Country country) {

        String dialcode = country.getDialCode();
        String[] arrOfStr = dialcode.split(Pattern.quote("+"));

        binding.tvCountryPicker.setText("" + country.getName());
        binding.tvCountryCode.setText("+" + arrOfStr[1]);
        binding.etPhoneNumber.requestFocus();
    }


    String phoneNum;
    private String verificationId;

    public void MoveToVerify() {

        phoneNum = binding.tvCountryCode.getText().toString() + binding.etPhoneNumber.getText().toString();
        sendVerificationCode(phoneNum);

        binding.layoutMobileNumber.setVisibility(View.INVISIBLE);
        binding.layoutVerify.setVisibility(View.VISIBLE);
    }


    PhoneAuthProvider.ForceResendingToken resendToken;


    public void resendVerificationCode(String phoneNumber,
                                       PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,           //a reference to an activity if this method is in a custom service
                mCallBack,
                token);        // resending with token got at previous call's `callbacks` method `onCodeSent`
        // [END start_phone_auth]
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            resendToken= forceResendingToken;



        }
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                binding.etOtp.setText(code);


                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(MagicCreateActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };


    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent i = new Intent(MagicCreateActivity.this, EditAccountActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(MagicCreateActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        signInWithCredential(credential);
    }


    private void sendVerificationCode(String phoneNum) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNum,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBack
        );
    }



    @Override
    public void onBackPressed() {

        if (binding.layoutVerify.getVisibility() == View.VISIBLE) {
            binding.layoutMobileNumber.setVisibility(View.VISIBLE);
            binding.layoutVerify.setVisibility(View.INVISIBLE);
        } else {
            super.onBackPressed();
        }

    }
}
