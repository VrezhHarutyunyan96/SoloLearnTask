package com.sololearn.android.home.view.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sololearn.android.R;
import com.sololearn.android.home.model.HomeDataResponseModel;

public class HomeRecyclerPagingAdapter extends PagedListAdapter<HomeDataResponseModel, HomeRecyclerPagingAdapter.HomeViewHolder> {
    private static DiffUtil.ItemCallback<HomeDataResponseModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<HomeDataResponseModel>() {
                @Override
                public boolean areItemsTheSame(HomeDataResponseModel oldItem, HomeDataResponseModel newItem) {
                    return oldItem.getResponse().getPageSize().equals(newItem.getResponse().getPageSize());
                }

                @Override
                public boolean areContentsTheSame(HomeDataResponseModel oldItem, HomeDataResponseModel newItem) {
                    return oldItem.equals(newItem);
                }
            };
    private Context context;

    public HomeRecyclerPagingAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_item, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        addContent(holder, position);
    }

    private void addContent(HomeViewHolder holder, int position) {
        HomeDataResponseModel data = getItem(position);
        if (data != null && data.getResponse().getResults() != null) {
            HomeDataResponseModel.Result result;
            try {
                result = data.getResponse().getResults().get(position);
            } catch (Exception e) {
                result = data.getResponse().getResults().get(0);
            }
            // download image
            downloadImage(holder.thumbnail, result);
            // add corresponding text
            addText(result.getWebTitle(), holder.title);
            addText(result.getSectionName(), holder.section);
        }
    }

    private void addText(String text, TextView textView) {
        if (text != null && textView != null) {
            textView.setText(text);
        }
    }

    private void downloadImage(ImageView imageView, HomeDataResponseModel.Result result) {
        if (imageView != null && result != null) {
            RequestOptions requestOptions = new
                    RequestOptions()
                    .placeholder(R.drawable.placeholder);
            Glide
                    .with(context)
                    .load(result.getFields().getThumbnail())
                    .apply(requestOptions)
                    .into(imageView);
        }
    }

    class HomeViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;
        TextView section;
        ConstraintLayout rootLayout;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.imageViewContent);
            title = itemView.findViewById(R.id.textViewTitle);
            section = itemView.findViewById(R.id.textCategory);
            rootLayout = itemView.findViewById(R.id.rootLayoutID);
        }
    }
}