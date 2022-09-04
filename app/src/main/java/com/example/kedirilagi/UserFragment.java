package com.example.kedirilagi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserFragment extends Fragment {

    TextView tvUsername,tvEmail;
    ImageView imgUser;
    FirebaseAuth mAuth;
    FirebaseUser currentUSer;
    Button btLogout;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmailUser);
        imgUser = view.findViewById(R.id.imgUser);
        btLogout = view.findViewById(R.id.btLogout);

        mAuth = FirebaseAuth.getInstance();
        currentUSer = mAuth.getCurrentUser();

        tvEmail.setText(currentUSer.getEmail());
        tvUsername.setText(currentUSer.getDisplayName());

        Glide.with(this).load(currentUSer.getPhotoUrl()).into(imgUser);

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent loginActivity = new Intent(getContext(),LoginRegister.class);
                startActivity(loginActivity);

            }
        });


    }
}
