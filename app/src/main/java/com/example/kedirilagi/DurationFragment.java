package com.example.kedirilagi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DurationFragment extends DialogFragment {

    TextView duration;
    Button btOke;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.durationpopup,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        duration = view.findViewById(R.id.etDurasi);
        btOke = view.findViewById(R.id.btOke);

        btOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!duration.getText().toString().isEmpty()){
                    update();

                }else {
                    Toast.makeText(getActivity(), "Please input the field", Toast.LENGTH_SHORT).show();
                    btOke.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void update() {

        FirebaseAuth auth;
        FirebaseUser currentuser;

        auth = FirebaseAuth.getInstance();
        currentuser = auth.getCurrentUser();
        String currentUserid = currentuser.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("itineraryList").child(currentUserid);
        String nama = getArguments().getString("nama");
        final int durations = Integer.parseInt(duration.getText().toString());
        Query query = myRef.orderByChild("nama").equalTo(nama);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ds.getRef().child("duration").setValue(durations);

                }
                btOke.setVisibility(View.VISIBLE);
                dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //myRef.child(durations).setValue(durations).addOnSuccessListener(new OnSuccessListener<Void>() {
          //  @Override
            //public void onSuccess(Void aVoid) {

            //}
        //});

    }
}
