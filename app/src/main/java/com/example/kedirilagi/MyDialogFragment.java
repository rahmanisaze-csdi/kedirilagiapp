package com.example.kedirilagi;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyDialogFragment extends DialogFragment implements RecyclerViewClickListener{

    RecyclerView itineraryRV,suggestedRV;
    RecyclerView.Adapter adapter;
    ItineraryAdapter itineraryAdapter;
    ArrayList<SuggestedModel> suggestedModels = new ArrayList<>();
    ProgressBar progressBar;
    TextView replaceTitle;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<ItineraryModel> itineraryModelList;
    Button btOke;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.itinerarypopup,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itineraryRV = view.findViewById(R.id.recyclerview);
        itineraryRV.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        //itineraryRV.setHasFixedSize(true);

        suggestedRV = view.findViewById(R.id.suggested_recyclerview);
        progressBar = view.findViewById(R.id.progress_bar);
        replaceTitle = view.findViewById(R.id.objectTitle);

        btOke = view.findViewById(R.id.btOke);
        btOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Alam");
        itineraryItems();

        suggestedItems();
    }

    private void suggestedItems() {

        //suggestedRV.setHasFixedSize(true);
        suggestedRV.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL,false));

        suggestedModels.add(new SuggestedModel(R.drawable.alam2, "Alam/Buatan"));
        suggestedModels.add(new SuggestedModel(R.drawable.sejarah, "Sejarah"));
        suggestedModels.add(new SuggestedModel(R.drawable.religi2, "Religi"));

        adapter = new SuggestedAdapter(suggestedModels,MyDialogFragment.this);
        suggestedRV.setAdapter(adapter);

    }

    private void itineraryItems() {
        progressBar.setVisibility(View.VISIBLE);

        //Get list Alam from the database

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                itineraryModelList = new ArrayList<>();
                for (DataSnapshot itinerarysnap: dataSnapshot.getChildren()){

                    ItineraryModel itineraryModel = itinerarysnap.getValue(ItineraryModel.class);
                    itineraryModel.setKey(itinerarysnap.getKey());
                    GetLocation getLocation = new GetLocation(getContext());
                    Location location = getLocation.getLocation();
                    if (location != null) {
                        double latUser = location.getLatitude();
                        double longUser = location.getLongitude();
                        double latDestination = Double.valueOf(itineraryModel.getLatitude());
                        double longDestination = Double.valueOf(itineraryModel.getLongitude());
                        itineraryModel.setDistance(distanceFunc(latDestination, longDestination, latUser, longUser));
                    }
                    itineraryModelList.add(itineraryModel);
                    Collections.sort(itineraryModelList,(itineraryModel1, t1) -> Double.compare(itineraryModel1.getDistance(),t1.getDistance()));

                }

                itineraryAdapter = new ItineraryAdapter(getContext(), itineraryModelList);
                itineraryRV.setAdapter(itineraryAdapter);

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
    public void onItemClick(int position) {
        progressBar.setVisibility(View.VISIBLE);
        if (position == 0){
            replaceTitle.setText("Alam/Buatan");
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Alam");
            itineraryModelList.clear();
            itineraryItems();
            progressBar.setVisibility(View.GONE);
        }else if (position == 1){
            replaceTitle.setText("Sejarah");
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Sejarah");
            itineraryModelList.clear();
            itineraryItems();
            progressBar.setVisibility(View.GONE);
        }else {
            replaceTitle.setText("Religi");
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Religi");
            itineraryModelList.clear();
            itineraryItems();
            progressBar.setVisibility(View.GONE);
        }
    }
}
