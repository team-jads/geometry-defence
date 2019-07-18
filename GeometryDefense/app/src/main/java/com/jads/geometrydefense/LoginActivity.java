package com.jads.geometrydefense;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;



public class LoginActivity extends AppCompatActivity {

    String username;
    String password;
    Bitmap bitmap;
    SharedPreferences sharedpreferences;

    public Bitmap getQRBitmap() {
        return bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginBtn;
        loginBtn = findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // get username and password
                EditText username_input = (EditText) findViewById(R.id.username_input);
                EditText password_input = (EditText) findViewById(R.id.password_input);

                username = username_input.getText().toString();
                password = password_input.getText().toString();


                sharedpreferences = getApplicationContext().getSharedPreferences("Username", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("Username", username);
                editor.commit();


                // goto main menu
                Intent mainMenu = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mainMenu);
            }
        });

    }


}
