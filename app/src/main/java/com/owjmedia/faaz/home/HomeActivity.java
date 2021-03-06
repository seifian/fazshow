package com.owjmedia.faaz.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.owjmedia.faaz.BuildConfig;
import com.owjmedia.faaz.R;
import com.owjmedia.faaz.appinfo.AppInfoActivity;
import com.owjmedia.faaz.ar.ArActivity;
import com.owjmedia.faaz.authenticate.AuthenticateActivity;
import com.owjmedia.faaz.galleries.GalleryActivity;
import com.owjmedia.faaz.general.Constants;
import com.owjmedia.faaz.general.Global;
import com.owjmedia.faaz.general.utils.ActivityUtils;
import com.owjmedia.faaz.general.utils.AppManager;
import com.owjmedia.faaz.general.utils.AuthenticationDialog;
import com.owjmedia.faaz.general.utils.CustomWidgets.TypefaceTextView;
import com.owjmedia.faaz.general.utils.ImageHelper;
import com.owjmedia.faaz.lottery.LotteryActivity;
import com.owjmedia.faaz.movie.MovieActivity;
import com.owjmedia.faaz.news.NewsActivity;
import com.owjmedia.faaz.videodetail.VideoActivity;
import com.owjmedia.faaz.vote.VoteActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_act);
        ButterKnife.bind(this);

        // Change status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));
        }

        // Set up the toolbar.
        setSupportActionBar(toolbar);
        toolbar.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayShowTitleEnabled(false);
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        ImageHelper.getInstance(this).imageLoader(AppManager.getString(this, Constants.KEYS.HOMEPAGE_IMAGE), imgBanner);
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

    @Override
    protected void onResume() {
        super.onResume();
        setupDrawer();
    }

    @OnClick(R.id.img_banner)
    public void goVideoPlayer() {
        startActivity(new Intent(HomeActivity.this, VideoActivity.class));
    }

    @OnClick(R.id.rl_profile)
    public void goToProfile() {
        Intent profileIntent = new Intent(HomeActivity.this, AuthenticateActivity.class);
        startActivity(profileIntent);
    }

    @OnClick(R.id.rl_vote)
    public void goToVote() {
        Intent votingIntent = new Intent(HomeActivity.this, VoteActivity.class);
        startActivity(votingIntent);
    }

    @OnClick(R.id.rl_lottery)
    public void goToCompetition() {
        if (Global.isLogin()) {
            startActivity(new Intent(HomeActivity.this, LotteryActivity.class));
        } else {
            new AuthenticationDialog().show(getSupportFragmentManager(), null);
        }
    }

    @OnClick(R.id.rl_club)
    public void goToClub() {
        ActivityUtils.showToast(this, getString(R.string.soon), "emoji_wink.json");
    }

    @OnClick(R.id.rl_info)
    public void goToInfo() {
        Intent infoActivity = new Intent(HomeActivity.this, AppInfoActivity.class);
        startActivity(infoActivity);
    }

    @OnClick(R.id.rl_exit)
    public void logout() {
        AppManager.setString(this, Constants.KEYS.TOKEN, "");
        mDrawerLayout.closeDrawers();
        setupDrawer();
    }

    @OnClick(R.id.card_news)
    public void goToNews() {
        Intent newsActivity = new Intent(HomeActivity.this, NewsActivity.class);
        startActivity(newsActivity);
    }

    @OnClick(R.id.card_ar)
    public void goToAr() {
        Intent ar = new Intent(HomeActivity.this, ArActivity.class);
        startActivity(ar);
    }

    @OnClick(R.id.card_gallery_image)
    public void goToImageGallery() {
        Intent gallery = new Intent(HomeActivity.this, GalleryActivity.class);
        gallery.putExtra(Constants.KEYS.IMAGE_GALLERY, true);
        startActivity(gallery);
    }

    @OnClick(R.id.card_gallery_videos)
    public void goToVideoGallery() {
        Intent gallery = new Intent(HomeActivity.this, GalleryActivity.class);
        gallery.putExtra(Constants.KEYS.IMAGE_GALLERY, false);
        startActivity(gallery);
    }

    @OnClick(R.id.card_movie)
    public void goToMoviePage() {
        Intent movie = new Intent(HomeActivity.this, MovieActivity.class);
        startActivity(movie);
    }

    private void setupDrawer() {
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

        if (BuildConfig.UPLOAD)
            findViewById(R.id.rl_faz_meter).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.rl_faz_meter).setVisibility(View.GONE);


    }


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.txt_profile)
    TypefaceTextView mTxtProfile;

    @BindView(R.id.rl_vote)
    ViewGroup mRlVote;

    @BindView(R.id.rl_lottery)
    ViewGroup mRlLottery;

    @BindView(R.id.rl_exit)
    ViewGroup mRlExit;

    @BindView(R.id.img_banner)
    ImageView imgBanner;

}
