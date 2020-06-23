package com.example.project2.homescreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.project2.R;
import com.example.project2.data.DatabaseHandler;
import com.example.project2.data.ReportModel;
import com.example.project2.stepcounter.Step_Counter_Activity;

import java.util.ArrayList;

public class Home_Screen_Activity extends AppCompatActivity {

    Context context;
    LinearLayout ll_steps;
    ReportModel reportModel;
    TextView tv_last_steps, tv_heading, tv_step_count;
    CardView card_steps;

    ArrayList<ReportModel> last_steps;
    ArrayList<ReportModel> list_time;
    ArrayList<String> time;
    DatabaseHandler db;

    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        context             = this;
        ll_steps            = findViewById(R.id.ll_steps);
        card_steps          = findViewById(R.id.card_view);

        tv_last_steps       = findViewById(R.id.tv_last_steps);
        tv_heading          = findViewById(R.id.tv_heading);
        tv_step_count       = findViewById(R.id.tv_step_counter);

        reportModel         = new ReportModel();
        list_time           = new ArrayList<ReportModel>();
        time                = new ArrayList<String>();
        db                  = new DatabaseHandler(context);

        card_steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, Step_Counter_Activity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        db = new DatabaseHandler(context);
        last_steps = db.getSteps("0");
        if (!last_steps.isEmpty()) {
            reportModel = last_steps.get(last_steps.size() - 1);
            tv_last_steps.setText(getResources().getString(R.string.Last_steps) + ": " + String.valueOf(reportModel.getTotal_steps()));
        } else
            tv_last_steps.setText(getResources().getString(R.string.Last_steps) + ": " + String.valueOf(0));
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }
}