package com.example.finalproject2nd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.DataSnapshot;

import java.util.Map;
public class PollDetailActivity extends AppCompatActivity {

    private TextView questionTextView;
    private RadioGroup optionsGroup;
    private Button voteButton;
    private String pollId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_detail);

        questionTextView = findViewById(R.id.question);
        optionsGroup = findViewById(R.id.optionsGroup);
        voteButton = findViewById(R.id.voteButton);

        // Retrieve data from intent
        pollId = getIntent().getStringExtra("pollId");
        String question = getIntent().getStringExtra("question");
        Map<String, Integer> options = (Map<String, Integer>) getIntent().getSerializableExtra("options");

        // Set the question and dynamically add options
        questionTextView.setText(question);
        for (String option : options.keySet()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            radioButton.setId(View.generateViewId()); // Optional
            optionsGroup.addView(radioButton);
        }

        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = optionsGroup.getCheckedRadioButtonId();
                if (selectedId != -1) { // Check if any option is selected
                    RadioButton selectedRadioButton = findViewById(selectedId);
                    String option = selectedRadioButton.getText().toString();
                    voteForOption(pollId, option);
                } else {
                    Toast.makeText(PollDetailActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void voteForOption(String pollId, String option) {
        DatabaseReference optionRef = FirebaseDatabase.getInstance()
                .getReference("polls")
                .child(pollId)
                .child("options")
                .child(option)
                .child("count");
        optionRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Integer currentVotes = mutableData.getValue(Integer.class);
                if (currentVotes == null) {
                    mutableData.setValue(1);
                } else {
                    mutableData.setValue(currentVotes + 1);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    Toast.makeText(PollDetailActivity.this, "Failed to vote", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PollDetailActivity.this, "Vote recorded", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
