package ru.mirea.documenteditor.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mirea.documenteditor.R;
import ru.mirea.documenteditor.data.payload.DocumentInfo;
import ru.mirea.documenteditor.data.payload.UserInfo;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private List<UserInfo> mUsers;

    public UsersAdapter(List<UserInfo> users) {
        mUsers = users;
    }

    public void swap(List<UserInfo> list){
        if (mUsers != null) {
            mUsers.clear();
            mUsers.addAll(list);
        }
        else {
            mUsers = list;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_user_id, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UsersAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        UserInfo userInfo = mUsers.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        textView.setText(userInfo.getUsername());
        Button button = holder.messageButton;
        button.setEnabled(true);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public Button messageButton;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.user_name);
            messageButton = (Button) itemView.findViewById(R.id.bt_user_details);

            messageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }
}