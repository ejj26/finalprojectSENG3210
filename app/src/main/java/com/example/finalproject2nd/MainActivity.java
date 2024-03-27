package com.example.finalproject2nd;

import com.example.finalproject2nd.activities.PollAdapter;
import com.example.finalproject2nd.activities.Poll;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.finalproject2nd.activities.CreatePollActivity;
import com.example.finalproject2nd.activities.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements PollAdapter.PollClickListener {
    private Button createPollButton;
    private RecyclerView pollsRecyclerView;
    private PollAdapter pollAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        createPollButton = findViewById(R.id.create_poll_button);
        pollsRecyclerView = findViewById(R.id.polls_recyclerview);

        if (user != null) {
            String userEmail = user.getEmail();
            TextView textView = findViewById(R.id.user_details);
            textView.setText(userEmail);

            if ("admin@gmail.com".equals(userEmail)) {
                createPollButton.setVisibility(View.VISIBLE);
            } else {
                createPollButton.setVisibility(View.GONE);
            }
        } else {
            startActivity(new Intent(this, Login.class));
            finish();
        }

        Button logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(this, Login.class));
            finish();
        });

        createPollButton.setOnClickListener(v -> startActivity(new Intent(this, CreatePollActivity.class)));

        // Set up the RecyclerView
        pollsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pollAdapter = new PollAdapter(this, new ArrayList<>(), this);
        pollsRecyclerView.setAdapter(pollAdapter);

        // Load polls from Firebase
        loadPolls();
    }

    @Override
    public void onPollClick(Poll poll) {
        Intent intent = new Intent(MainActivity.this, PollDetailActivity.class); // Fixed this line
        intent.putExtra("pollId", poll.getId()); // Make sure your Poll class has getId() method.
        ArrayList<String> options = new ArrayList<>(poll.getOptions().keySet());
        intent.putStringArrayListExtra("options", options);
        startActivity(intent);
    }


    private void loadPolls() {
        DatabaseReference pollsRef = FirebaseDatabase.getInstance().getReference("polls");
        pollsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Poll> newPolls = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, Integer> options = new HashMap<>();
                    DataSnapshot optionsSnapshot = snapshot.child("options");
                    for (DataSnapshot optionSnapshot : optionsSnapshot.getChildren()) {
                        options.put(optionSnapshot.getKey(), optionSnapshot.getValue(Integer.class));
                    }
                    Poll poll = new Poll(snapshot.getKey(), snapshot.child("question").getValue(String.class), options);
                    newPolls.add(poll);
                }
                pollAdapter.updatePolls(newPolls);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log error or show an error message
                System.err.println("Failed to read polls: " + databaseError.getMessage());
            }
        });
    }
}
