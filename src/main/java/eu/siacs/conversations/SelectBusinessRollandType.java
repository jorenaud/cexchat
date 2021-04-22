package eu.siacs.conversations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import eu.siacs.conversations.ui.adapter.AdapterForRoll;


public class SelectBusinessRollandType extends AppCompatActivity {


    public static ArrayList<AdapterForRoll.Roll> dataModel = new ArrayList<>();
    public static ArrayList<AdapterForRoll.Roll> dataModeltag = new ArrayList<>();
    TextView btnAddRoll, tv_selectAllRoles, tv_next;
    EditText et_roll;
    private static RecyclerView rolllayout;
    AdapterForRoll adapter;


    TextView btnAddTag, tv_selectAllTag;
    EditText et_tag;
    private static RecyclerView taglayout;
    AdapterForTag tagadapter;

    TextView tv_error;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_business_rolland_type);

        ArrayList<String> aaa = new ArrayList<>();
        ArrayList<String> bbb = new ArrayList<>();

        Intent intent = getIntent();
        String gender = intent.getStringExtra("gender");
        String dob = intent.getStringExtra("dob");
        String Customer_type = intent.getStringExtra("customertype");


        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);

        FlexboxLayoutManager layoutManager2 = new FlexboxLayoutManager(this);
        layoutManager2.setFlexDirection(FlexDirection.ROW);
        layoutManager2.setJustifyContent(JustifyContent.SPACE_EVENLY);

        btnAddRoll = findViewById(R.id.btn_add_role);
        et_roll = findViewById(R.id.et_roll);
        rolllayout = (RecyclerView) findViewById(R.id.rolllayout);
        tv_selectAllRoles = findViewById(R.id.tv_selectAllRoles);
        tv_next = findViewById(R.id.tv_next);
        tv_error = findViewById(R.id.tv_error);


        btnAddTag = findViewById(R.id.btn_add_tag);
        et_tag = findViewById(R.id.et_tag);
        taglayout = (RecyclerView) findViewById(R.id.tagsLayout);
        tv_selectAllTag = findViewById(R.id.tv_selectAllTags);



        rolllayout.setHasFixedSize(true);
        rolllayout.setItemAnimator(new DefaultItemAnimator());

        adapter = new AdapterForRoll(this);
        rolllayout.setLayoutManager(layoutManager);
        rolllayout.setAdapter(adapter);


        taglayout.setHasFixedSize(true);
        taglayout.setItemAnimator(new DefaultItemAnimator());

        tagadapter = new AdapterForTag(this);
        taglayout.setLayoutManager(layoutManager2);
        taglayout.setAdapter(tagadapter);


        dataModel.add(new AdapterForRoll.Roll("Designer", false));
        dataModel.add(new AdapterForRoll.Roll("Developer", false));
        dataModel.add(new AdapterForRoll.Roll("Writer", false));
        dataModel.add(new AdapterForRoll.Roll("VideoEditor", false));
        dataModel.add(new AdapterForRoll.Roll("Musician", false));
        dataModel.add(new AdapterForRoll.Roll("Trader", false));
        dataModel.add(new AdapterForRoll.Roll("Marketer", false));
        dataModel.add(new AdapterForRoll.Roll("Manager", false));
        dataModel.add(new AdapterForRoll.Roll("Teacher", false));
        dataModel.add(new AdapterForRoll.Roll("Director", false));
        dataModel.add(new AdapterForRoll.Roll("Sexual Girl", false));

        dataModeltag.add(new AdapterForRoll.Roll("Web", false));
        dataModeltag.add(new AdapterForRoll.Roll("Mobile", false));
        dataModeltag.add(new AdapterForRoll.Roll("iOS", false));
        dataModeltag.add(new AdapterForRoll.Roll("Language", false));
        dataModeltag.add(new AdapterForRoll.Roll("English", false));
        dataModeltag.add(new AdapterForRoll.Roll("Chinese", false));
        dataModeltag.add(new AdapterForRoll.Roll("Korean", false));
        dataModeltag.add(new AdapterForRoll.Roll("Article", false));
        dataModeltag.add(new AdapterForRoll.Roll("Forex", false));
        dataModeltag.add(new AdapterForRoll.Roll("Stock", false));
        dataModeltag.add(new AdapterForRoll.Roll("Crypto", false));
        dataModeltag.add(new AdapterForRoll.Roll("Sex", false));
        dataModeltag.add(new AdapterForRoll.Roll("Date", false));
        dataModeltag.add(new AdapterForRoll.Roll("Orgasm", false));

        btnAddRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_roll.getText().toString().isEmpty()) {
                    dataModel.add(new AdapterForRoll.Roll(et_roll.getText().toString().trim(), false));
                    checkSlectedData();
                    adapter.notifyDataSetChanged();
                    et_roll.setText("");
                }
            }
        });

        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_tag.getText().toString().isEmpty()) {
                    dataModeltag.add(new AdapterForRoll.Roll(et_tag.getText().toString().trim(), false));
                    checkSlectedDataforTAG();
                    tagadapter.notifyDataSetChanged();
                    et_tag.setText("");
                }
            }
        });

        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aaa.clear();
                bbb.clear();
                for (int i = 0; i < dataModel.size(); i++) {
                    if (dataModel.get(i).isFlag()) {
                        aaa.add(dataModel.get(i).getName());
                    }
                }

                for (int i = 0; i < dataModeltag.size(); i++) {
                    if (dataModeltag.get(i).isFlag()) {
                        bbb.add(dataModeltag.get(i).getName());
                    }
                }

                if (aaa.size() == 0) {
                    tv_error.setVisibility(View.VISIBLE);
                    tv_error.setText("Please Select Business Role");

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tv_error.setVisibility(View.GONE);
                        }
                    }, 2000);
                    return;
                }

                if (bbb.size() == 0) {
                    tv_error.setVisibility(View.VISIBLE);
                    tv_error.setText("Please Select Business Tag");
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tv_error.setVisibility(View.GONE);
                        }
                    }, 2000);
                    return;
                }

                Toast.makeText(SelectBusinessRollandType.this, "Gender: " + gender + ", DOB: " + dob + ", CUSTOMER TYPE: " + Customer_type + ", Role: " + aaa.toString() + ", Tag: " + bbb.toString(), Toast.LENGTH_LONG).show();
            }
        });

        tv_selectAllRoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean f = tv_selectAllRoles.getText().equals("Select All");

                for (int i = 0; i < dataModel.size(); i++) {
                    dataModel.get(i).setFlag(f);
                    Log.e("CUSTOM---->", i + "-------->" + dataModel.get(i).getName() + "---------->" + dataModel.get(i).isFlag());
                }

                if (f)
                    tv_selectAllRoles.setText("Unselect All");
                else
                    tv_selectAllRoles.setText("Select All");


                adapter.notifyDataSetChanged();
                Log.e("CUSTOM---->", "SUCCESS");
            }
        });


        tv_selectAllTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean f = tv_selectAllTag.getText().equals("Select All");

                for (int i = 0; i < dataModeltag.size(); i++) {
                    dataModeltag.get(i).setFlag(f);
                    Log.e("CUSTOM---->", i + "-------->" + dataModeltag.get(i).getName() + "---------->" + dataModeltag.get(i).isFlag());
                }

                if (f)
                    tv_selectAllTag.setText("Unselect All");
                else
                    tv_selectAllTag.setText("Select All");


                tagadapter.notifyDataSetChanged();
            }
        });


    }

    public void checkSlectedData() {
        int flag = 0;
        for (int i = 0; i < dataModel.size(); i++) {
            if (!dataModel.get(i).isFlag()) {
                flag = 1;
            }
        }
        if (flag == 0) {
            tv_selectAllRoles.setText("Unselect All");
        } else {
            tv_selectAllRoles.setText("Select All");
        }
    }

    public void checkSlectedDataforTAG() {
        int flag = 0;
        for (int i = 0; i < dataModeltag.size(); i++) {
            if (!dataModeltag.get(i).isFlag()) {
                flag = 1;
            }
        }
        if (flag == 0) {
            tv_selectAllTag.setText("Unselect All");
        } else {
            tv_selectAllTag.setText("Select All");
        }
    }
}