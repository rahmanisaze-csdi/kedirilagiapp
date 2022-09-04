package com.example.kedirilagi;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SaranActivity extends AppCompatActivity {

    EditText etKomen;
    Button btSend;
    TextView total;
    ImageView imgUser;
    FirebaseAuth mAuth;
    FirebaseUser currentUSer;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<SaranModel> saranModelList;
    SaranAdapter saranAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saran);

        etKomen = findViewById(R.id.etKomen);
        btSend = findViewById(R.id.btSend);
        imgUser = findViewById(R.id.picture);
        total = findViewById(R.id.jumlahSaran);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SaranActivity.this));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Saran");

        mAuth = FirebaseAuth.getInstance();
        currentUSer = mAuth.getCurrentUser();

        Glide.with(this).load(currentUSer.getPhotoUrl()).into(imgUser);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etKomen.getText().toString().isEmpty()){
                    tambah();

                }else {
                    Toast.makeText(SaranActivity.this, "Tidak bisa menambakan saran yang kosong", Toast.LENGTH_SHORT).show();
                    btSend.setVisibility(View.VISIBLE);
                }
                etKomen.setText("");
            }

            private void tambah() {
                String imageUser = currentUSer.getPhotoUrl().toString();
                Calendar cdate = Calendar.getInstance();
                SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
                final String savedate = currentdate.format(cdate.getTime());

                Calendar ctime = Calendar.getInstance();
                SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
                final String savetime = currenttime.format(ctime.getTime());

                String waktu = savedate +":"+ savetime;

                SaranModel saran = new SaranModel(currentUSer.getDisplayName(),
                        etKomen.getText().toString(),
                        imageUser,
                        waktu);
                tambahSaran(saran);
            }
        });
        
        saran();
    }

    private void saran() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                saranModelList = new ArrayList<>();
                for (DataSnapshot saransnap: dataSnapshot.getChildren()) {

                    SaranModel saranModel = saransnap.getValue(SaranModel.class);
                    saranModel.setSaranKey(saransnap.getKey());
                    saranModelList.add(saranModel);

                }

                total();

                saranAdapter = new SaranAdapter(SaranActivity.this, saranModelList);
                recyclerView.setAdapter(saranAdapter);

            }

            private void total() {
                if (saranModelList!=null){
                    total.setText(""+saranModelList.size()+" saran");
                }else {
                    total.setText("0 saran");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void tambahSaran(SaranModel saran) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Saran").push();

        String key = myRef.getKey();
        saran.setSaranKey(key);

        myRef.setValue(saran).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SaranActivity.this, "Saran berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case 101:
                saranAdapter.removeItem(item.getGroupId());
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
