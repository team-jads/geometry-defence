package com.jads.geometrydefense;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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

public class UserQRCodeActivity extends AppCompatActivity {
    String username;
    ImageView qrCodeView;

    public Bitmap generateQRbitmap(String value){
        try{
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(username, BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap qrBitmap = barcodeEncoder.createBitmap(bitMatrix);
            return qrBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_qrcode);

        Button gotoScanCode;
        // get username
        SharedPreferences sharedpreferences = getSharedPreferences("Username", Context.MODE_PRIVATE);
        username = sharedpreferences.getString("Username", null);

        // set the qr code image
        qrCodeView = findViewById(R.id.qr_code_view);
        qrCodeView.setFocusable(false);
        qrCodeView.setImageBitmap(generateQRbitmap(username));

        gotoScanCode = findViewById(R.id.scan_code);
        gotoScanCode.setFocusable(false);
        gotoScanCode.setClickable(true);
        gotoScanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scanCodePage = new Intent(UserQRCodeActivity.this, FindPlayerActivity.class);
                startActivity(scanCodePage);
            }
        });

    }
}
