package com.jads.geometrydefense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        EditText menuText;

        menuText = findViewById(R.id.menu);
        menuText.setFocusable(false);
        menuText.setClickable(true);
        menuText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menuPage = new Intent(ScoreActivity.this, MainActivity.class);
                startActivity(menuPage);
            }
        });
    }
}
