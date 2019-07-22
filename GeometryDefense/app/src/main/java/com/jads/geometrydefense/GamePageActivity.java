package com.jads.geometrydefense;

import android.app.ActivityManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.view.KeyEvent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jads.geometrydefense.entities.attackers.TurretLand;

import com.jads.geometrydefense.entities.attackers.turrets.TurretType;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GamePageActivity extends AppCompatActivity {

    private Button startGame;

    private Button sellTower;
    private Button upgradeTower;
    private GameBoardCanvas canvas;
    private ImageView basicTower;
    private ImageView movementSlowTower;
    private Button circle;
    private Button square;
    private boolean gameStarted = false;

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_CODE = 1000;
    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private static final int PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE = PERMISSION_REQ_ID_RECORD_AUDIO + 1;

    private boolean isPermissible = false;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mScreenDensity;

    private boolean isVideoSd;
    private boolean isAudio;

    private Button record;
    public boolean recording = false;

    private DocumentReference game = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);
        startGame = findViewById(R.id.start);
        canvas = findViewById(R.id.game_view);
        basicTower = findViewById(R.id.basic_tower);
        movementSlowTower = findViewById(R.id.movement_slow_tower);
        sellTower = findViewById(R.id.sell_tower);
        upgradeTower = findViewById(R.id.upgrade_tower);
        circle = findViewById(R.id.circle);
        square = findViewById(R.id.square);

        Intent intent = getIntent();
        String gameId = intent.getStringExtra("game_id");

        if (gameId != null) {
            startGame.setEnabled(false);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            game = db.collection("game").document(gameId);
            game.get().addOnSuccessListener(snapshot -> {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String opponentUid;
                List<String> players = (List<String>)snapshot.get("players");
                if (players.get(0).equals(uid)) {
                    opponentUid = players.get(1);
                } else {
                    opponentUid = players.get(0);
                }
                canvas.setGameReference(game);
                resumeGame();
                startGame.setVisibility(View.GONE);
                GameManager gm = GameManager.getInstance();
                circle.setOnClickListener(e -> {
                    if (gm.purchase(3))
                        game.collection("summon").add(new HashMap<String, Object>() {{
                            put("type", "circle");
                            put("to", opponentUid);
                        }});
                });
                square.setOnClickListener(e -> {
                    if (gm.purchase(5))
                        game.collection("summon").add(new HashMap<String, Object>() {{
                            put("type", "square");
                            put("to", opponentUid);
                        }});
                });
            });
            focusOff();
        }

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!gameStarted) {
                    resumeGame();
                } else {
                    pauseGame();
                }
            }
        });

        // TODO: Write a recycleview or listview here instead of setting up onclick for every tower
        basicTower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.onTowerSelected(TurretType.BASIC_TURRET);
                focusOff();
            }
        });

        movementSlowTower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.onTowerSelected(TurretType.MOVEMENT_SLOW_TURRET);
                focusOff();
            }
        });

        sellTower.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.sellTower();
                focusOff();
            }
        });

        upgradeTower.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.upgradeTower();
                focusOff();
            }
        });

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO) && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE)) {
            isPermissible = true;
        }
        Log.d(TAG, "onCreate: isPermissible = " + isPermissible);
        isVideoSd = true;
        getScreenBaseInfo();


        record = findViewById(R.id.record);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!recording) {
                    record.setText("Stop");
                    if (isPermissible) {
                        startScreenRecording();
                    }
                } else {
                    record.setText("Record");
                    stopScreenRecording();
                }
                recording = !recording;
            }
        });
        focusOff();
    }

    public void focusOn(boolean isOccupied) {
        circle.setVisibility(View.GONE);
        square.setVisibility(View.GONE);
        if (isOccupied) {
            upgradeTower.setVisibility(View.VISIBLE);
            sellTower.setVisibility(View.VISIBLE);
            basicTower.setVisibility(View.GONE);
            movementSlowTower.setVisibility(View.GONE);
        } else {
            upgradeTower.setVisibility(View.GONE);
            sellTower.setVisibility(View.GONE);
            basicTower.setVisibility(View.VISIBLE);
            movementSlowTower.setVisibility(View.VISIBLE);
        }

    }

    public void focusOff() {
        upgradeTower.setVisibility(View.GONE);
        sellTower.setVisibility(View.GONE);
        basicTower.setVisibility(View.GONE);
        movementSlowTower.setVisibility(View.GONE);
        if (game != null) {
            circle.setVisibility(View.VISIBLE);
            square.setVisibility(View.VISIBLE);
        }
    }

    public boolean checkSelfPermission(String permission, int requestCode) {
        Log.d(TAG, "checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (grantResults.length != 0) {
            Log.d(TAG, "onRequestPermissionsResult " + grantResults[0] + " " + requestCode);
            switch (requestCode) {
                case PERMISSION_REQ_ID_RECORD_AUDIO:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE);
                    } else {
                        isPermissible = false;
                        showLongToast("No permission for " + Manifest.permission.RECORD_AUDIO);
                        finish();
                    }
                    break;
                case PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: isPermissible = " + isPermissible);
                        isPermissible = true;
                    } else {
                        isPermissible = false;
                        showLongToast("No permission for " + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        finish();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public final void showLongToast(final String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void getScreenBaseInfo() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        mScreenDensity = metrics.densityDpi;
    }

    private void startScreenRecording() {
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent permissionIntent = mediaProjectionManager != null ? mediaProjectionManager.createScreenCaptureIntent() : null;
        startActivityForResult(permissionIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Intent service = new Intent(this, ScreenRecordService.class);
                service.putExtra("code", resultCode);
                service.putExtra("data", data);
                service.putExtra("audio", isAudio);
                service.putExtra("width", mScreenWidth);
                service.putExtra("height", mScreenHeight);
                service.putExtra("density", mScreenDensity);
                service.putExtra("quality", isVideoSd);
                startService(service);
                Log.i(TAG, "Start screen recording");
            } else {
                Toast.makeText(this, "user cancelled", Toast.LENGTH_LONG).show();
                Log.i(TAG, "User cancelled");
            }
        }
    }

    private void stopScreenRecording() {
        pauseGame();
        Intent service = new Intent(this, ScreenRecordService.class);
        stopService(service);
    }

    public void pauseGame() {
        canvas.pauseGame();
        gameStarted = false;
        startGame.setText("Start");
    }

    public void resumeGame() {
        canvas.resumeGame();
        gameStarted = true;
        startGame.setText("Pause");
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

