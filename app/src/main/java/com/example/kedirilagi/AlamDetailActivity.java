package com.example.kedirilagi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AlamDetailActivity extends AppCompatActivity {
    ImageView imgAlam,imgHighlight;
    TextView txtNama,txtDesc,txtAlamat,txtHtm,txtHighlight,txtOpen;
    Button btUpdate,btDirect;
    String AlamKey;
    DatabaseReference getReference;
    FirebaseDatabase getDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alam_detail);

        //FloatingActionButton fab = findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(),Home.class);
                //startActivity(intent);
            //}
        //});

        //ini views
        imgAlam = findViewById(R.id.detail_image);
        //imgHighlight = findViewById(R.id.imgHighlight);
        txtNama = findViewById(R.id.detail_nama);
        txtDesc = findViewById(R.id.detail_desc);
        txtAlamat = findViewById(R.id.detail_alamat);
        txtHtm = findViewById(R.id.detail_htm);
        txtOpen = findViewById(R.id.detail_open);
        //txtHighlight = findViewById(R.id.detail_highlight);
        btDirect = findViewById(R.id.btDirect);

        String AlamKey = getIntent().getExtras().getString("alamKey");


        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //now it can get alam data

        String alamImage = getIntent().getExtras().getString("alamImage");
        Glide.with(this).load(alamImage).into(imgAlam);

        String nama = getIntent().getExtras().getString("nama");
        txtNama.setText(nama);

        String desc = getIntent().getExtras().getString("description");
        txtDesc.setText(desc);

        String alamat = getIntent().getExtras().getString("alamat");
        txtAlamat.setText(alamat);

        String htm = getIntent().getExtras().getString("htm") ;
        txtHtm.setText(htm);

        String open = getIntent().getExtras().getString("open");
        txtOpen.setText(open);

        //String highlight = getIntent().getExtras().getString("highlight");
        //txtHighlight.setText(highlight);


        btDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = getIntent().getExtras().getString("nama");
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+nama+"");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(mapIntent);
            }
        });




    }


}
