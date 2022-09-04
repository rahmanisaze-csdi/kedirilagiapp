package com.example.kedirilagi;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItineraryManual extends Fragment {

    RecyclerView itineraryRV;
    NestedScrollView nestedScrollView;
    ItineraryListAdapterManual itineraryListAdapter;
    List<ItineraryModel> itineraryModelList;
    TextView tvTotal,tvDurasiMobil,tvDurasiMotor;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public ItineraryManual() {
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
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_itinerary_manual, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},0);
        //requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},0);

        tvTotal = view.findViewById(R.id.tvTotal);
        nestedScrollView = view.findViewById(R.id.nestedSV);

        itineraryRV = view.findViewById(R.id.itineraryRV);
        itineraryRV.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        itineraryRV.setHasFixedSize(true);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserid = user.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("itineraryList").child(currentUserid);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogFragment newFragment = new MyDialogFragment();
                newFragment.show(getFragmentManager(),"MyFragment");
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //durationsList = new ArrayList<>();
                itineraryModelList = new ArrayList<>();
                for (DataSnapshot itinerarylist: dataSnapshot.getChildren()){

                    ItineraryModel itineraryModel = itinerarylist.getValue(ItineraryModel.class);
                    itineraryModel.setKey(itinerarylist.getKey());
                    if (itineraryModel.getLatitude()!=0.0 && itineraryModel.getLongitude()!=0.0){
                        GetLocation getLocation = new GetLocation(getContext());
                        Location location = getLocation.getLocation();
                        if (location != null) {
                            double latUser = location.getLatitude();
                            double longUser = location.getLongitude();
                            double latDestination = Double.valueOf(itineraryModel.getLatitude());
                            double longDestination = Double.valueOf(itineraryModel.getLongitude());
                            itineraryModel.setDistance(distanceFunc(latDestination, longDestination, latUser, longUser));
                        }
                    }else {
                        int a = 0;
                        int b = 0;
                        itineraryModel.setWaktu(a);
                        itineraryModel.setWaktu(b);
                        itineraryModel.setDistance(0);
                    }

                    itineraryModelList.add(itineraryModel);

                }

                Collections.sort(itineraryModelList,(itineraryModel1, t1) -> Double.compare(itineraryModel1.getDistance(),t1.getDistance()));

                total();

                itineraryListAdapter = new ItineraryListAdapterManual(getContext(),itineraryModelList);
                itineraryRV.setAdapter(itineraryListAdapter);

            }

            private void total() {
                if (itineraryModelList!=null){
                    tvTotal.setText(""+itineraryModelList.size());
                }else {
                    tvTotal.setText("0");
                }
            }

            private double distanceFunc(double latDestination, double longDestination, double latUser, double longUser) {
                Double lat1 = latDestination;
                Double lon1 = longDestination;
                Double lat2 = latUser;
                Double lon2 = longUser;
                final int R = 6371;

                Double latRad1 = lat1 * (Math.PI / 180);
                Double latRad2 = lat2 * (Math.PI / 180);
                Double deltaLatRad = (lat2 - lat1) * (Math.PI / 180);
                Double deltaLonRad = (lon2 - lon1) * (Math.PI / 180);
                //rumus haversine//
                Double a = Math.sin(deltaLatRad / 2)
                        * Math.sin(deltaLatRad / 2)
                        + Math.cos(latRad1)
                        * Math.cos(latRad2)
                        * Math.sin(deltaLonRad / 2)
                        * Math.sin(deltaLonRad / 2);
                Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                Double s = R * c;
                return s;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();


    }
}
