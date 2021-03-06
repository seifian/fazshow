package com.owjmedia.faaz.news;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.owjmedia.faaz.BuildConfig;
import com.owjmedia.faaz.R;
import com.owjmedia.faaz.appinfo.AppInfoActivity;
import com.owjmedia.faaz.authenticate.AuthenticateActivity;
import com.owjmedia.faaz.galleries.GalleryActivity;
import com.owjmedia.faaz.general.ConnectionErrorDialog;
import com.owjmedia.faaz.general.Constants;
import com.owjmedia.faaz.general.Global;
import com.owjmedia.faaz.general.utils.AuthenticationDialog;
import com.owjmedia.faaz.general.utils.ImageHelper;
import com.owjmedia.faaz.lottery.LotteryActivity;
import com.owjmedia.faaz.news.model.Result;
import com.owjmedia.faaz.general.utils.ActivityUtils;
import com.owjmedia.faaz.general.utils.AppManager;
import com.owjmedia.faaz.general.utils.CustomWidgets.TypefaceTextView;
import com.owjmedia.faaz.general.utils.ProgressDialog;
import com.owjmedia.faaz.newsdetail.NewsDetailActivity;
import com.owjmedia.faaz.poll.PollActivity;
import com.owjmedia.faaz.upload.status.UploadActivity;
import com.owjmedia.faaz.videodetail.VideoActivity;
import com.owjmedia.faaz.vote.VoteActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NewsActivity extends AppCompatActivity implements NewsContract.View {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_act);
        ButterKnife.bind(this);

        // Set up the toolbar.
        setSupportActionBar(toolbar);
        toolbar.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayShowTitleEnabled(false);
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        mNewsPresenter = new NewsPresenter(this);
        mProgressDialog = new ProgressDialog(this);

        ImageHelper.getInstance(this).imageLoader(AppManager.getString(this, Constants.KEYS.HOMEPAGE_IMAGE), imgVideo);


        // Set up recycler view
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupDrawerLayout();
    }

    private void setupDrawerLayout() {
        if (!AppManager.isLogin(this)) {
            mRlExit.setVisibility(View.INVISIBLE);
            mTxtProfile.setText(getString(R.string.login_and_register));
        } else {
            mRlExit.setVisibility(View.VISIBLE);
            mTxtProfile.setText(getString(R.string.profile));
        }

        if (BuildConfig.VOTE)
            mRlVote.setVisibility(View.VISIBLE);
        else
            mRlVote.setVisibility(View.GONE);

        if (BuildConfig.LOTTERY)
            mRlLottery.setVisibility(View.VISIBLE);
        else
            mRlLottery.setVisibility(View.GONE);

        if (BuildConfig.NEWS_MENU)
            findViewById(R.id.llMenu).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.llMenu).setVisibility(View.GONE);

        if (BuildConfig.UPLOAD)
            findViewById(R.id.rl_faz_meter).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.rl_faz_meter).setVisibility(View.GONE);
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mNewsAdapter = new NewsAdapter(mNews, new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Result newsItem, ImageView imgNews, TypefaceTextView txtNews) {
                Intent intent = new Intent(NewsActivity.this, NewsDetailActivity.class);
                intent.putExtra(Constants.KEYS.NEWS_ID, String.valueOf(newsItem.getId()));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Pair<View, String> image = Pair.create((View) imgNews, getString(R.string.news_image));
                    Pair<View, String> title = Pair.create((View) txtNews, getString(R.string.news_title));
                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(NewsActivity.this, image, title);
                    startActivity(intent, transitionActivityOptions.toBundle());
                } else {
                    startActivity(intent);
                }
            }

        });
        mRecyclerView.setAdapter(mNewsAdapter);
        mNewsPresenter.getNews();
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
        new ConnectionErrorDialog().show(getSupportFragmentManager(), null);
    }

    @Override
    public void showNews(List<Result> news) {
        mNewsAdapter.update(news);
        mRecyclerView.scheduleLayoutAnimation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT))
                mDrawerLayout.closeDrawer(Gravity.RIGHT, true);
            else
                mDrawerLayout.openDrawer(Gravity.RIGHT, true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.img_video)
    public void goVideoPlayer() {
        startActivity(new Intent(NewsActivity.this, VideoActivity.class));
    }

    @OnClick({R.id.ll_vote, R.id.rl_vote})
    public void goToVote() {
        startActivity(new Intent(NewsActivity.this, VoteActivity.class));
    }

    @OnClick(R.id.ll_image_gallery)
    public void goToImageGallery() {
        Intent gallery = new Intent(NewsActivity.this, GalleryActivity.class);
        gallery.putExtra(Constants.KEYS.IMAGE_GALLERY, true);
        startActivity(gallery);
    }

    @OnClick(R.id.ll_video_gallery)
    public void goToVideoGallery() {
        Intent gallery = new Intent(NewsActivity.this, GalleryActivity.class);
        gallery.putExtra(Constants.KEYS.IMAGE_GALLERY, false);
        startActivity(gallery);
    }

    @OnClick(R.id.rl_profile)
    public void goToProfile() {
        Intent profileIntent = new Intent(NewsActivity.this, AuthenticateActivity.class);
        startActivity(profileIntent);
    }


    @OnClick({R.id.rl_faz_meter, R.id.txt_faz_meter})
    public void goToFazMeter() {
        if (Global.isLogin()) {
            startActivity(new Intent(NewsActivity.this, UploadActivity.class));
        } else {
            new AuthenticationDialog().show(getSupportFragmentManager(), null);
        }

    }

    @OnClick(R.id.rl_lottery)
    public void goToCompetition() {
        if (Global.isLogin()) {
            startActivity(new Intent(NewsActivity.this, LotteryActivity.class));
        } else {
            new AuthenticationDialog().show(getSupportFragmentManager(), null);
        }
    }

    @OnClick({R.id.rl_poll, R.id.txt_poll})
    public void goToPoll() {
        startActivity(new Intent(NewsActivity.this, PollActivity.class));
    }

    @OnClick(R.id.rl_club)
    public void goToClub() {
        ActivityUtils.showToast(this, getString(R.string.soon), "emoji_wink.json");
    }

    @OnClick(R.id.rl_info)
    public void goToInfo() {
        Intent infoActivity = new Intent(NewsActivity.this, AppInfoActivity.class);
        startActivity(infoActivity);
    }

    @OnClick(R.id.rl_exit)
    public void logout() {
        AppManager.setString(this, Constants.KEYS.TOKEN, null);
        mDrawerLayout.closeDrawer(Gravity.RIGHT, true);
        setupDrawerLayout();
    }


    private NewsContract.Presenter mNewsPresenter;
    private NewsAdapter mNewsAdapter;
    private List<Result> mNews;

    ProgressDialog mProgressDialog;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;


    @BindView(R.id.txt_profile)
    TypefaceTextView mTxtProfile;

    @BindView(R.id.rl_vote)
    ViewGroup mRlVote;

    @BindView(R.id.rl_poll)
    ViewGroup mRlPoll;

    @BindView(R.id.rl_lottery)
    ViewGroup mRlLottery;

    @BindView(R.id.rl_exit)
    ViewGroup mRlExit;

    @BindView(R.id.img_video)
    ImageView imgVideo;

}
