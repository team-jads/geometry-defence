package com.jads.geometrydefense;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateUserState();

        findViewById(R.id.start_ai_game).setOnClickListener(view -> startActivity(new Intent(this, GamePageActivity.class)));

        findViewById(R.id.login_logout).setOnClickListener(view -> {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                createSignInIntent();
            } else {
                signOut();
            }
        });

        findViewById(R.id.leader_board).setOnClickListener(view -> startActivity(new Intent(this, ScoreActivity.class)));

        findViewById(R.id.find_player).setOnClickListener(view -> startActivity(new Intent(this, FindPlayerActivity.class)));

        findViewById(R.id.quit_app).setOnClickListener(view -> finish());
    }

    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().setRequireName(false).build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.tower_icon)
                        .enableAnonymousUsersAutoUpgrade()
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            updateUserState();
        }
    }

    private void updateUserState() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ((Button)findViewById(R.id.find_player)).setEnabled(user != null);
        ((Button)findViewById(R.id.login_logout)).setText(user == null ? "Login" : "Logout");
        ((TextView)findViewById(R.id.current_user_string)).setText(user == null ? "Login to save high scores!" : String.format("Welcome %s", user.getDisplayName()));
    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> updateUserState());
    }

}
