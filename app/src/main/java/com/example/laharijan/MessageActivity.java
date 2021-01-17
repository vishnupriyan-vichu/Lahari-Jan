package com.example.laharijan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txtReceiverName, txtLastSeen;
    private String receiverName, receiverId;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance("https://lahari-jan-default-rtdb.firebaseio.com/").getReference().child("Name");

        toolbar = findViewById(R.id.toolBarMessage);
        txtReceiverName = findViewById(R.id.txtReceiverName);
        txtLastSeen = findViewById(R.id.lastSeen);

        Intent intent = getIntent();
        receiverId = intent.getStringExtra("ReceiverId");
        receiverName = intent.getStringExtra("ReceiverName");
        txtReceiverName.setText(receiverName);
        userRef.child(receiverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null && dataSnapshot.hasChild("Status")) {
                    if(dataSnapshot.child("Status").hasChild("Online")) {
                        txtLastSeen.setText("Online");
                        txtLastSeen.setTextColor(getResources().getColor(R.color.green));
                    } else {
                        Object objTimeStamp = dataSnapshot.child("Status").child("Offline").getValue();
                        SimpleDateFormat sDF = new SimpleDateFormat("yyyy-mm-dd");
                        String lastSeen = sDF.format(objTimeStamp);
                        String currentDateString = sDF.format(new Date());
                        Date currentData = null, lastSeenDate = null;
                        try {
                            currentData = sDF.parse(currentDateString);
                            lastSeenDate = sDF.parse(lastSeen);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(currentData.compareTo(lastSeenDate) == 0) {
                            String time = new SimpleDateFormat("h:mm a").format(objTimeStamp);
                            txtLastSeen.setText("last Seen : "+time);
                            txtLastSeen.setTextColor(getResources().getColor(R.color.white));
                        } else {
                            String date = new SimpleDateFormat("yyyy-mm-dd").format(objTimeStamp);
                            txtLastSeen.setText("last seen : "+date);
                            txtLastSeen.setTextColor(getResources().getColor(R.color.white));
                        }
                    }
                } else if(dataSnapshot==null) {
                    txtLastSeen.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        txtLastSeen.setText(receiverName);
    }
}