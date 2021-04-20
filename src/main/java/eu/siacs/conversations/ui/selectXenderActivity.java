package eu.siacs.conversations.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

import eu.siacs.conversations.R;
import eu.siacs.conversations.services.XmppConnectionService;
import eu.siacs.conversations.ui.widget.ImmediateAutoCompleteTextView;

public class selectXenderActivity extends XmppActivity implements XmppConnectionService.OnAccountUpdate {

    @Override
    protected void refreshUiReal() {

    }

    @Override
    void onBackendConnected() {

    }


    DatePickerDialog picker;
    TextInputLayout accountdob;
    ImmediateAutoCompleteTextView tv_dob;
    CheckBox checkboxMale, checkboxFemale, checkboxOther;
    TextView btn_next, materialTextView2;
    String gender, dateOfBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_xender);

        accountdob = findViewById(R.id.account_dob);
        tv_dob = findViewById(R.id.tv_account_dob);
        checkboxMale = findViewById(R.id.checkboxMale);
        checkboxFemale = findViewById(R.id.checkboxFemale);
        checkboxOther = findViewById(R.id.checkboxOther);
        btn_next = findViewById(R.id.btn_next);
        materialTextView2 = findViewById(R.id.materialTextView2);

        tv_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(selectXenderActivity.this, R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tv_dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);

                accountdob.setError(null);

                picker.show();
            }
        });

        checkboxMale.setChecked(true);


        checkboxMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkboxFemale.setChecked(false);
                    checkboxOther.setChecked(false);
                    materialTextView2.setError(null);
                    gender = "male";
                }
            }
        });
        checkboxFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkboxMale.setChecked(false);
                    checkboxOther.setChecked(false);
                    materialTextView2.setError(null);
                    gender = "female";
                }
            }
        });
        checkboxOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkboxFemale.setChecked(false);
                    checkboxMale.setChecked(false);
                    materialTextView2.setError(null);
                    gender = "other";
                }
            }
        });



        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkboxFemale.isChecked() || checkboxMale.isChecked() || checkboxOther.isChecked()) {

                    if (tv_dob.getText().toString().equals("")) {
                        accountdob.setError("Select Your Date of Birth");
//                        Toast.makeText(selectXenderActivity.this,"Please Select Your Date o",Toast.LENGTH_SHORT).show();

                    } else {
                        dateOfBirth = tv_dob.getText().toString();
                        Intent intent = new Intent(selectXenderActivity.this, SelectProfileType.class);
                        intent.putExtra("gender", gender);
                        intent.putExtra("dob", dateOfBirth);
                        startActivity(intent);
                    }

                } else {
                    materialTextView2.setError("Select Your Gender");
//                    Toast.makeText(selectXenderActivity.this,"Please Select Your Gender",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onAccountUpdate() {

    }
}