package com.example.laharijan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private ListView listView;
    private EditText edtSearch;
    private ArrayAdapter arrayAdapter;
    private ArrayList<String> usersArrayList;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    private ArrayList<String> userIds;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        listView = view.findViewById(R.id.listViewUsers);
        edtSearch = view.findViewById(R.id.edtSearch);
        usersArrayList = new ArrayList<>();
        userIds = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, usersArrayList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), MessageActivity.class);
                intent.putExtra("ReceiverId", userIds.get(position));
                intent.putExtra("ReceiverName", usersArrayList.get(position));
                startActivity(intent);
            }
        });
        userRef = FirebaseDatabase.getInstance("https://lahari-jan-default-rtdb.firebaseio.com/").getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        ProgressDialog progress = new ProgressDialog(getContext());
        progress.setTitle("Getting users!");
        progress.setMessage("Please wait!");
        progress.show();
        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && !dataSnapshot.getKey().equals(mAuth.getCurrentUser().getUid())) {
                    usersArrayList.add(dataSnapshot.child("Name").getValue().toString());
                    userIds.add(dataSnapshot.getKey());
                }
                arrayAdapter.notifyDataSetChanged();
                progress.dismiss();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void searchUsers(String enterText) {
        usersArrayList.clear();
        userIds.clear();
        arrayAdapter.notifyDataSetChanged();

        userRef.orderByChild("Name").startAt(enterText).endAt(enterText+"\uf8ff")
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersArrayList.clear();
                userIds.clear();
                if(dataSnapshot.exists()) {
                    for(DataSnapshot userSnap:dataSnapshot.getChildren()) {
                        if(!userSnap.getKey().equals(mAuth.getCurrentUser().getUid())) {
                            usersArrayList.add(userSnap.child("Name").getValue().toString());
                            userIds.add(userSnap.getKey());
                        }
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}