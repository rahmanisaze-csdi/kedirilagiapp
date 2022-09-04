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

public class ReligiDetailActivity extends AppCompatActivity {

    ImageView imgReligi,imgHighlight;
    TextView txtNama,txtDesc,txtAlamat,txtHtm,txtHighlight,txtOpen;
    Button btUpdate,btDirect;
    String AlamKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_religi);

        //FloatingActionButton fab = findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(),Home.class);
                //startActivity(intent);
            //}
        //});

        //ini views
        imgReligi = findViewById(R.id.detail_image);
        //imgHighlight = findViewById(R.id.imgHighlight);
        txtNama = findViewById(R.id.detail_nama);
        txtDesc = findViewById(R.id.detail_desc);
        txtAlamat = findViewById(R.id.detail_alamat);
        txtOpen = findViewById(R.id.detail_open);
        //txtHighlight = findViewById(R.id.detail_highlight);
        btDirect = findViewById(R.id.btDirect);

        String ReligiKey = getIntent().getExtras().getString("religiKey");


        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //now it can get alam data

        String religiImage = getIntent().getExtras().getString("religiImage");
        Glide.with(this).load(religiImage).into(imgReligi);

        String nama = getIntent().getExtras().getString("nama");
        txtNama.setText(nama);

        String desc = getIntent().getExtras().getString("description");
        txtDesc.setText(desc);

        String alamat = getIntent().getExtras().getString("alamat");
        txtAlamat.setText(alamat);

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

