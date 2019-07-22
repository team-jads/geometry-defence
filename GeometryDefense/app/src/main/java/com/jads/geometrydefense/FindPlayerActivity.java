package com.jads.geometrydefense;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FindPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_player);

        findViewById(R.id.goto_menu).setOnClickListener(view -> finish());

        findViewById(R.id.scan_code).setOnClickListener(view -> {
            new IntentIntegrator(this)
                    .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
                    .setBeepEnabled(true)
                    .setPrompt("Scan a code")
                    .initiateScan();
        });

        initializeQRCode();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String opponentUid = result.getContents();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DocumentReference queue = db.collection("queue").document(opponentUid);
                findViewById(R.id.qr_code_view).setVisibility(View.INVISIBLE);
                findViewById(R.id.qr_code_backdrop).setVisibility(View.INVISIBLE);
                queue.update("opponent", uid);
                queue.addSnapshotListener((snapshot, e) -> {
                    if (snapshot.get("game") != null) {
                        Intent intent = new Intent(this, GamePageActivity.class);
                        intent.putExtra("game_id", (String)snapshot.get("game"));
                        queue.delete();
                        finish();
                        startActivity(intent);
                    }
                });
            } else {
                new AlertDialog.Builder(this)
                        .setMessage("Could not read code, please try again.")
                        .create();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initializeQRCode() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference queue = db.collection("queue").document(uid);
        queue.set(new HashMap<String, Object>() {{
            put("started", Calendar.getInstance().getTime());
        }}).addOnSuccessListener(ref -> {
            try {
                ByteMatrix bitMatrix = Encoder.encode(uid, ErrorCorrectionLevel.L).getMatrix();
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        if (bitMatrix.get(x, y) == 0) {
                            bmp.setPixel(x, y, Color.WHITE);
                        } else {
                            bmp.setPixel(x, y, Color.BLACK);
                        }
                    }
                }
                ImageView bmpv = findViewById(R.id.qr_code_view);
                bmpv.setImageBitmap(Bitmap.createScaledBitmap(bmp, 500, 500, false));
            } catch (WriterException e) {
                new AlertDialog.Builder(this)
                        .setMessage("Could not generate code, single player mode only, sorry.")
                        .create();
            }
        }).addOnCompleteListener(result -> {
            int a = 1;
        });
        queue.addSnapshotListener((snapshot, e) -> {
            if (snapshot.get("opponent") != null && snapshot.get("game") == null) {
                String opponentUid = (String)snapshot.get("opponent");
                Calendar gameStart = Calendar.getInstance();
                gameStart.add(Calendar.SECOND, 10);
                db.collection("game")
                        .add(new HashMap<String, Object>() {{
                            put("players", Arrays.asList(opponentUid, uid));
                            put(uid + "_health", 1);
                            put(opponentUid + "_health", 1);
                        }})
                        .addOnSuccessListener(ref -> {
                            queue.update("game", ref.getId());
                            Intent intent = new Intent(this, GamePageActivity.class);
                            intent.putExtra("game_id", ref.getId());
                            finish();
                            startActivity(intent);
                        });
            }
        });
    }
}
