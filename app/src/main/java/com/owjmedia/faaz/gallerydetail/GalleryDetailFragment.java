package com.owjmedia.faaz.gallerydetail;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.owjmedia.faaz.R;
import com.owjmedia.faaz.gallerydetail.model.GalleryDetailResponse;
import com.owjmedia.faaz.gallerydetail.model.GalleryItem;
import com.owjmedia.faaz.general.Constants;
import com.owjmedia.faaz.general.DetailsTransition;
import com.owjmedia.faaz.general.utils.ActivityUtils;
import com.owjmedia.faaz.general.utils.AppManager;
import com.owjmedia.faaz.general.utils.ProgressDialog;
import com.owjmedia.faaz.general.utils.SpaceItemDecoration;
import com.owjmedia.faaz.imagedetail.ImageFragment;
import com.owjmedia.faaz.videodetail.VideoFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by salman on 11/23/17.
 */

public class GalleryDetailFragment extends Fragment implements GalleryDetailContract.View {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.galleries_frg, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mGalleryDetailPresenter = new GalleryDetailPresenter(this);
        mProgressDialog = new ProgressDialog(getActivity());

        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(16));

        mGalleryDetailAdapter = new GalleryDetailAdapter(mGalleryItems, getArguments().getBoolean(Constants.KEYS.IMAGE_GALLERY), new GalleryDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GalleryItem galleryItem, ImageView imageView) {
                Fragment fragment;
                if (getArguments().getBoolean(Constants.KEYS.IMAGE_GALLERY)) {
                    fragment = new ImageFragment();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        fragment.setSharedElementEnterTransition(new DetailsTransition());
                        fragment.setEnterTransition(new Fade());
                        fragment.setSharedElementReturnTransition(new DetailsTransition());
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.KEYS.IMAGE_PATH, galleryItem.getImage());
                    fragment.setArguments(bundle);
                } else {
                    fragment = new VideoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.KEYS.VIDEO_PATH, galleryItem.getVideo());
                    fragment.setArguments(bundle);
                }

                ActivityUtils.addFragmentToActivity(getFragmentManager(), fragment, R.id.contentFrame, imageView, getString(R.string.image_transition));

            }
        });
        mRecyclerView.setAdapter(mGalleryDetailAdapter);

        if (getArguments().getBoolean(Constants.KEYS.IMAGE_GALLERY))
            mGalleryDetailPresenter.getImageGalleryDetail(getArguments().getString(Constants.KEYS.GALLEY_ID));
        else
            mGalleryDetailPresenter.getVideoGalleryDetail(getArguments().getString(Constants.KEYS.GALLEY_ID));
    }


    @Override
    public void setLoadingIndicator(boolean active) {
        if (active)
            mProgressDialog.show();
        else
            mProgressDialog.cancel();
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showConnectionError() {

    }

    @Override
    public void showGalleryDetailResponse(GalleryDetailResponse galleryDetailResponse) {
        mGalleryDetailAdapter.update(galleryDetailResponse.getItems());
        mRecyclerView.scheduleLayoutAnimation();
    }

    GalleryDetailContract.Presenter mGalleryDetailPresenter;
    GalleryDetailAdapter mGalleryDetailAdapter;
    List<GalleryItem> mGalleryItems;
    private ProgressDialog mProgressDialog;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

}
