package com.example.kedirilagi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginRegister extends AppCompatActivity {

    private Button btLogin;
    private Button btRegistrasi;
    private FirebaseAuth mAuth;
    private Intent HomeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        btLogin = (Button) findViewById(R.id.btLogin);
        btRegistrasi = (Button) findViewById(R.id.btRegister);

        mAuth = FirebaseAuth.getInstance();
        HomeActivity = new Intent(this,MainActivity.class);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginRegister.this, LoginActivity.class);

                Pair<View,String> p1 = Pair.create((View)btLogin,"loginTransition");

                ActivityOptionsCompat optionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(LoginRegister.this, p1);;
                startActivity(intent,optionsCompat.toBundle());


            }
        });

        btRegistrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginRegister.this, RegistrasiActivity.class);

                Pair<View,String> p1 = Pair.create((View)btRegistrasi,"registrasiTransition");
                //Pair<View,String> p2 = Pair.create((View)btBack,"backTransition");

                ActivityOptionsCompat optionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(LoginRegister.this, p1);

                startActivity(intent,optionsCompat.toBundle());


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            updateUI();
        }

    }

    private void updateUI() {

        startActivity(HomeActivity);
        finish();

    }
}
