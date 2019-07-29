package com.sololearn.android.home.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class HomeRecyclerPagingAdapter extends PagedListAdapter<HomeDataResponseModel, RecyclerView.ViewHolder> {
    private Context context;
    private String networkState;
    private SavedDataViewModel savedDataViewModel;
    private HomeDataResponseModel data;
    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    /*
     * The DiffUtil is defined in the constructor
     */
    public HomeRecyclerPagingAdapter(Context context) {
        super(HomeDataResponseModel.DIFF_CALLBACK);
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        initViewModel();
        if (viewType == TYPE_PROGRESS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_progress, parent, false);
            return new NetworkStateItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_item, parent, false);
            return new HomeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HomeViewHolder) {
            this.data = getItem(position);
            ((HomeViewHolder) holder).bindTo(data, position);
        } else {
            ((NetworkStateItemViewHolder) holder).bindView(networkState);
        }
    }

    private void initViewModel() {
        savedDataViewModel = ViewModelProviders.of((FragmentActivity) context).get(SavedDataViewModel.class);
    }

    private void onItemClick(HomeViewHolder holder, int position) {
        if (holder != null) {
            holder.rootLayout.setOnClickListener(v -> {
                data = getItem(position);
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
                data = getItem(position);
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

        public void bindTo(HomeDataResponseModel data, int position) {
            if (data != null && data.getResponse().getResults() != null) {
                HomeDataResponseModel.Result result;
                try {
                    result = data.getResponse().getResults().get(position);
                } catch (Exception e) {
                    result = data.getResponse().getResults().get(0);
                }
                // download image
                downloadImage(thumbnail, result);
                // add corresponding text
                addText(result.getWebTitle(), title);
                addText(result.getSectionName(), section);
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
    }

    class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public NetworkStateItemViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }

        public void bindView(String networkState) {
            if (getItemCount() > 0 && getItemCount() % 10 == 0) {
                networkState = "LOADING";
            }

            if (networkState != null && networkState.equals("LOADING")) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }

        }
    }
}
