package com.example.finalproject2nd.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.finalproject2nd.R;
import com.example.finalproject2nd.activities.Poll;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CreatePollActivity extends AppCompatActivity {

    // UI components
    private EditText pollQuestion, pollOptionOne, pollOptionTwo, pollOptionThree;
    private FirebaseDatabase database; // Database instance
    private DatabaseReference databaseReference; // Database reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_poll_activity);

        // Initialize UI components
        pollQuestion = findViewById(R.id.pollQuestion);
        pollOptionOne = findViewById(R.id.pollOptionOne);
        pollOptionTwo = findViewById(R.id.pollOptionTwo);
        pollOptionThree = findViewById(R.id.pollOptionThree);

        // Initialize database
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("polls");
    }

    // Called when the user taps the 'Create Poll' button
    public void createPoll(View view) {
        String question = pollQuestion.getText().toString();
        String optionOne = pollOptionOne.getText().toString();
        String optionTwo = pollOptionTwo.getText().toString();
        String optionThree = pollOptionThree.getText().toString();

        // Validate input
        if(question.isEmpty() || optionOne.isEmpty() || optionTwo.isEmpty() || optionThree.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check for duplicate poll
        if (checkForDuplicatePoll(question)) {
            Toast.makeText(this, "Poll with the same question already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create poll object using correct format
        Poll poll = new Poll();
        poll.setQuestion(question);
        // Set options as Map<String, Integer>
        Map<String, Integer> options = new HashMap<>();
        options.put(optionOne, 0); // Initialize count to 0
        options.put(optionTwo, 0);
        options.put(optionThree, 0);
        poll.setOptions(options);

        // Save poll to Firebase
        DatabaseReference newPollRef = databaseReference.push();
        newPollRef.setValue(poll);

        // Clear the form
        pollQuestion.setText("");
        pollOptionOne.setText("");
        pollOptionTwo.setText("");
        pollOptionThree.setText("");

        Toast.makeText(CreatePollActivity.this, "Poll created successfully!", Toast.LENGTH_SHORT).show();
    }

    // Method to check for duplicate poll
    private boolean checkForDuplicatePoll(String question) {
        // Implement your logic to check if a poll with the same question already exists in the database
        // Return true if duplicate found, false otherwise
        // This is just a placeholder method, you need to implement it based on your database structure
        return false;
    }
}
