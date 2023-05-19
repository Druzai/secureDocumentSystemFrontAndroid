package ru.mirea.documenteditor.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.List;

import ru.mirea.documenteditor.R;
import ru.mirea.documenteditor.data.payload.DocumentInfo;
import ru.mirea.documenteditor.data.payload.DocumentInfo;

public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.ViewHolder> {

    private List<DocumentInfo> mDocumentsInfo;
    private boolean enableDeleteButton;

    public DocumentsAdapter(List<DocumentInfo> documentInfoList, boolean enableDeleteButton) {
        mDocumentsInfo = documentInfoList;
        this.enableDeleteButton = enableDeleteButton;
    }

    public void swap(List<DocumentInfo> list){
        if (mDocumentsInfo != null) {
            mDocumentsInfo.clear();
            mDocumentsInfo.addAll(list);
        }
        else {
            mDocumentsInfo = list;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DocumentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_document_id, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DocumentsAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        DocumentInfo DocumentInfo = mDocumentsInfo.get(position);

        // Set item views based on your views and data model
        TextView nameTextView = holder.nameTextView;
        TextView ownerTextView = holder.ownerTextView;

        nameTextView.setText(MessageFormat.format("Документ: {0}", DocumentInfo.getName()));
        ownerTextView.setText(MessageFormat.format("Владелец: {0}", DocumentInfo.getOwner().getUsername()));
        Button openButton = holder.openMessageButton;
        Button deleteButton = holder.deleteMessageButton;
        openButton.setEnabled(true);
        if (enableDeleteButton){
            deleteButton.setEnabled(true);
        } else{
            deleteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mDocumentsInfo.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    private OnItemClickListener openMessageButtonListener;
    private OnItemClickListener deleteMessageButtonListener;

    public void setOnOpenButtonClickListener(OnItemClickListener listener) {
        this.openMessageButtonListener = listener;
    }

    public void setOnDeleteButtonClickListener(OnItemClickListener listener) {
        this.deleteMessageButtonListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView ownerTextView;
        public Button openMessageButton;
        public Button deleteMessageButton;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.document_name);
            ownerTextView = (TextView) itemView.findViewById(R.id.document_owner);
            openMessageButton = (Button) itemView.findViewById(R.id.bt_document_open);
            deleteMessageButton = (Button) itemView.findViewById(R.id.bt_document_delete);

            openMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (openMessageButtonListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            openMessageButtonListener.onItemClick(itemView, position);
                        }
                    }
                }
            });

            deleteMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (deleteMessageButtonListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            deleteMessageButtonListener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }
}