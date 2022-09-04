package com.example.kedirilagi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SejarahFragment extends Fragment {

    RecyclerView sejarahRecyclerView;
    SearchSejarahAdapter sejarahAdapter;
    EditText InputSearch;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Sejarahs> sejarahList;

    public SejarahFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_sejarah, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sejarahRecyclerView = view.findViewById(R.id.ObjectRV);
        sejarahRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        sejarahRecyclerView.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Sejarah");

    }

    @Override
    public void onStart() {
        super.onStart();

        //Get list Alam from the database

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sejarahList = new ArrayList<>();
                for (DataSnapshot sejarahsnap: dataSnapshot.getChildren()){

                    Sejarahs sejarahs = sejarahsnap.getValue(Sejarahs.class);
                    sejarahs.setSejarahKey(sejarahsnap.getKey());
                    sejarahList.add(sejarahs);

                }

                sejarahAdapter = new SearchSejarahAdapter(getContext(),sejarahList);
                sejarahRecyclerView.setAdapter(sejarahAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.searchmenu,menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sejarahAdapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}
