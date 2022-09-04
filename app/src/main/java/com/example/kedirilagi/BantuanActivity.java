package com.example.kedirilagi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class BantuanActivity extends AppCompatActivity {
    Button btWa,btGmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitvity_bantuan);

        btWa = findViewById(R.id.btWa);
        btGmail = findViewById(R.id.btGmail);

        btWa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "+6285854108393";
                Uri uri = Uri.parse(String.format(
                        "https://api.whatsapp.com/send?phone=%s",number
                ));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"rahmanisaze@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Email dari Aplikasi Android");
                intent.putExtra(Intent.EXTRA_TEXT, "Hai, silahkan tulis sesuatu yang ingin Anda sampaikan terkait dengan aplikasi");
                startActivity(intent);
            }
        });

    }
}
