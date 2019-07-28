package com.sololearn.android.home.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sololearn.android.R;
import com.sololearn.android.home.database.entity.SavedDataModel;

import java.util.List;

public class SavedRecyclerViewAdapter extends RecyclerView.Adapter<SavedRecyclerViewAdapter.SavedViewHolder> {
    private Context context;
    private List<SavedDataModel> savedData;

    public SavedRecyclerViewAdapter(Context context, List<SavedDataModel> resultList) {
        this.context = context;
        savedData = resultList;
    }

    @NonNull
    @Override
    public SavedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_item, parent, false);
        return new SavedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedViewHolder holder, int position) {
        addContent(holder, position);
    }

    private void addContent(SavedViewHolder holder, int position) {
        holder.pin.setVisibility(View.GONE);
        holder.section.setText(savedData.get(position).getSectionName());
        holder.title.setText(savedData.get(position).getTitle());
        // load Image
        Glide
                .with(context)
                .load(savedData.get(position).getImageUrl())
                .into(holder.thumbnail);
    }


    @Override
    public int getItemCount() {
        return savedData.size();
    }

    class SavedViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;
        TextView section;
        ConstraintLayout rootLayout;
        ConstraintLayout pin;

        SavedViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.imageViewContent);
            title = itemView.findViewById(R.id.textViewTitle);
            section = itemView.findViewById(R.id.textCategory);
            rootLayout = itemView.findViewById(R.id.rootLayoutID);
            pin = itemView.findViewById(R.id.pinLayoutID);
        }
    }

}
