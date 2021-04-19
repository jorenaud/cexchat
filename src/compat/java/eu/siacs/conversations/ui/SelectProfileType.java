package eu.siacs.conversations.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.concurrent.atomic.AtomicBoolean;

import eu.siacs.conversations.R;
import eu.siacs.conversations.services.XmppConnectionService;
import static eu.siacs.conversations.ui.PublishProfilePictureActivity.cropUri;

public class SelectProfileType extends XmppActivity implements XmppConnectionService.OnAccountUpdate {
    @Override
    public void onAccountUpdate() {

    }

    @Override
    protected void refreshUiReal() {

    }

    @Override
    void onBackendConnected() {

    }

    private boolean mInitialAccountSetup;
    private final AtomicBoolean handledExternalUri = new AtomicBoolean(false);
    TextView tvcustomer,tvServiceprovider;
    @Override
    protected void onStart() {
        super.onStart();
        final Intent intent = getIntent();
        this.mInitialAccountSetup = intent != null && intent.getBooleanExtra("setup", false);

        final Uri uri = intent != null ? intent.getData() : null;

        if (uri != null && handledExternalUri.compareAndSet(false,true)) {
            cropUri(this, uri);
            return;
        }
    }

    CheckBox check_customer, check_service_provider;
    TextView tv_next;
    ConstraintLayout lay_first,layout_second;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_select_usertype);

        check_customer = findViewById(R.id.cb_account_customer);
        check_service_provider = findViewById(R.id.cb_account_service);
        tv_next = findViewById(R.id.btn_next);
        lay_first = findViewById(R.id.layout_first);
        tvServiceprovider = findViewById(R.id.tvServiceprovider);
        tvcustomer = findViewById(R.id.tvcustomer);
        layout_second = findViewById(R.id.layout_second);

        check_customer.setChecked(false);
        check_service_provider.setChecked(false);

        tvcustomer.setText(Html.fromHtml("I am <br>Customer"));

        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!check_customer.isChecked() && !check_service_provider.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Please Select at least one Type", Toast.LENGTH_SHORT).show();
                    return;
                } else if (check_customer.isChecked() && !check_service_provider.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Customer type", Toast.LENGTH_SHORT).show();
                } else if (!check_customer.isChecked() && check_service_provider.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Provider Type", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "selected both", Toast.LENGTH_SHORT).show();
                }

                lay_first.setVisibility(View.INVISIBLE);
                layout_second.setVisibility(View.VISIBLE);

            }
        });

    }
}
