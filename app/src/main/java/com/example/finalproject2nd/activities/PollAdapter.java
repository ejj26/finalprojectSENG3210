package com.example.finalproject2nd.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject2nd.R;
import java.util.List;
import java.util.Map;

public class PollAdapter extends RecyclerView.Adapter<PollAdapter.PollViewHolder> {
    private List<Poll> polls;
    private LayoutInflater inflater;
    private Context context;
    private PollClickListener listener; // Interface for click events

    public PollAdapter(Context context, List<Poll> polls, PollClickListener listener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.polls = polls;
        this.listener = listener;
    }

    @Override
    public PollViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.poll_item, parent, false);
        return new PollViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PollViewHolder holder, int position) {
        Poll currentPoll = polls.get(position);
        holder.question.setText(currentPoll.getQuestion());
        Map<String, Integer> options = currentPoll.getOptions();
        StringBuilder optionsText = new StringBuilder();
        for (Map.Entry<String, Integer> entry : options.entrySet()) {
            optionsText.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        holder.options.setText(optionsText.toString());
    }

    @Override
    public int getItemCount() {
        return polls.size();
    }

    public void updatePolls(List<Poll> newPolls) {
        this.polls.clear();
        this.polls.addAll(newPolls);
        notifyDataSetChanged();
    }

    public interface PollClickListener {
        void onPollClick(Poll poll);
    }

    class PollViewHolder extends RecyclerView.ViewHolder {
        TextView question;
        TextView options;

        public PollViewHolder(View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.poll_question);
            options = itemView.findViewById(R.id.poll_option);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    Poll clickedPoll = polls.get(position);
                    listener.onPollClick(clickedPoll);
                }
            });
        }
    }
}
