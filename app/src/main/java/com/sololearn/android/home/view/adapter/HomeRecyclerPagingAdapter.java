package com.sololearn.android.home.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sololearn.android.R;
import com.sololearn.android.constants.AppConstants;
import com.sololearn.android.home.model.HomeDataResponseModel;
import com.sololearn.android.home.view.fragment.DetailItemFragment;
import com.sololearn.android.home.viewmodel.SavedDataViewModel;

public class HomeRecyclerPagingAdapter extends PagedListAdapter<HomeDataResponseModel, HomeRecyclerPagingAdapter.HomeViewHolder> {
    private Context context;
    private String networkState;
    private SavedDataViewModel savedDataViewModel;

    /*
     * The DiffUtil is defined in the constructor
     */
    public HomeRecyclerPagingAdapter(Context context) {
        super(HomeDataResponseModel.DIFF_CALLBACK);
        this.context = context;
    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        initViewModel();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_item, parent, false);
        return new HomeViewHolder(view);
    }

    private void initViewModel() {
        savedDataViewModel = ViewModelProviders.of((FragmentActivity) context).get(SavedDataViewModel.class);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        addContent(holder, position);
        onItemClick(holder, position);
    }

    private void onItemClick(HomeViewHolder holder, int position) {
        if (holder != null) {
            holder.rootLayout.setOnClickListener(v -> {
                HomeDataResponseModel data = getItem(position);
                if (data != null) {
                    HomeDataResponseModel.Response response = data.getResponse();
                    String imageUrl = response.getResults().get(position).getFields().getThumbnail();
                    Fragment detailItemFragment = new DetailItemFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConstants.DETAIL_IMAGE, imageUrl);
                    detailItemFragment.setArguments(bundle);
                    // show detail fragment
                    createFragment(R.id.fragmentContainer, detailItemFragment, v);
                }
            });

            holder.pin.setOnClickListener(v -> {
                HomeDataResponseModel data = getItem(position);
                if (data != null) {
                    HomeDataResponseModel.Result result = data.getResponse().getResults().get(position);
                    String sectionName = result.getSectionName();
                    String title = result.getWebTitle();
                    String imageUrl = result.getFields().getThumbnail();
                    pin(sectionName, title, imageUrl);
                }
            });
        }
    }

    private void pin(String sectionName, String title, String imageUrl) {
        savedDataViewModel.save(sectionName, title, imageUrl);
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
            if (result.getFields() != null && result.getFields().getThumbnail() != null) {
                Glide
                        .with(context)
                        .load(result.getFields().getThumbnail())
                        .into(imageView);
            }
        }
    }

    private void createFragment(int resId, Fragment fragment, View view) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        activity
                .getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("")
                .replace(resId, fragment)
                .commit();
    }

    public void setNetworkState(String newNetworkState) {
        String previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && !previousState.equals(newNetworkState)) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    private boolean hasExtraRow() {
        if (networkState != null && !networkState.equals("LOADED")) {
            return true;
        } else {
            return false;
        }
    }

    class HomeViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;
        TextView section;
        ConstraintLayout rootLayout;
        ConstraintLayout pin;

        HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.imageViewContent);
            title = itemView.findViewById(R.id.textViewTitle);
            section = itemView.findViewById(R.id.textCategory);
            rootLayout = itemView.findViewById(R.id.rootClick);
            pin = itemView.findViewById(R.id.pinLayoutID);
        }
    }
}
