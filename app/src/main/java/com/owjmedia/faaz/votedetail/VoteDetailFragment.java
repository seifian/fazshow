package com.owjmedia.faaz.votedetail;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.owjmedia.faaz.R;
import com.owjmedia.faaz.general.Constants;
import com.owjmedia.faaz.votedetail.model.Item;
import com.owjmedia.faaz.votedetail.model.VoteDetailResponse;
import com.owjmedia.faaz.general.utils.AppManager;
import com.owjmedia.faaz.general.utils.AuthenticationDialog;
import com.owjmedia.faaz.general.utils.CustomWidgets.TypefaceTextView;
import com.owjmedia.faaz.general.utils.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.owjmedia.faaz.R.id.recyclerView;


public class VoteDetailFragment extends Fragment implements VoteDetailContract.View {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.vote_detail_frg, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mVoteDetailPresenter = new VoteDetailPresenter(this);

        mProgressDialog = new ProgressDialog(getActivity());

        // Set up recycler view
        initCarouselLayoutManager();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mVoteDetailPresenter != null)
            mVoteDetailPresenter.getCandidates(getArguments().getString(Constants.KEYS.POLL_ID));
    }


    private void initCarouselLayoutManager() {
        CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new CenterScrollListener());
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        initAdapter();
    }

    private void initAdapter() {
        mVoteDetailAdapter = new VoteDetailAdapter(mVotingItems, new VoteDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, int itemId, ConfirmationDialog.OnVotedListener votedListener) {
                if (!AppManager.isLogin(getContext()))
                    new AuthenticationDialog().show(getFragmentManager(), null);
                else {
                    ConfirmationDialog confirmationDialog = ConfirmationDialog.getInstance(getArguments().getString(Constants.KEYS.POLL_ID), itemId, position);
                    confirmationDialog.setListener(votedListener);
                    confirmationDialog.show(getFragmentManager(), "");

                }
            }
        });

        mRecyclerView.setAdapter(mVoteDetailAdapter);
        mVoteDetailPresenter.getCandidates(getArguments().getString(Constants.KEYS.POLL_ID));
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
    public void showCandidates(VoteDetailResponse voteDetailResponse) {
        txtPhaseTitle.setText(voteDetailResponse.getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtPhaseDescription.setText(Html.fromHtml(voteDetailResponse.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            txtPhaseDescription.setText(Html.fromHtml(voteDetailResponse.getDescription()));
        }
        mVoteDetailAdapter.update(voteDetailResponse.getItems());
    }

    @Override
    public void votedSuccessfully() {
    }

    VoteDetailContract.Presenter mVoteDetailPresenter;

    VoteDetailAdapter mVoteDetailAdapter;
    List<Item> mVotingItems = new ArrayList<>();

    ProgressDialog mProgressDialog;

    @BindView(recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.txt_phase_title)
    TypefaceTextView txtPhaseTitle;

    @BindView(R.id.txt_phase_description)
    TypefaceTextView txtPhaseDescription;

}
