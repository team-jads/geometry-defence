package com.jads.geometrydefense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FindPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_player);

        EditText menuText;
        Button gotoUserCode;

        menuText = findViewById(R.id.goto_menu);
        menuText.setFocusable(false);
        menuText.setClickable(true);
        menuText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menuPage = new Intent(FindPlayerActivity.this, MainActivity.class);
                startActivity(menuPage);
            }
        });


        gotoUserCode = findViewById(R.id.your_code);
        gotoUserCode.setFocusable(false);
        gotoUserCode.setClickable(true);
        gotoUserCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userCodePage = new Intent(FindPlayerActivity.this, UserQRCodeActivity.class);
                startActivity(userCodePage);
            }
        });

    }

}
