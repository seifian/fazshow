package com.owjmedia.faaz.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.owjmedia.faaz.R;
import com.owjmedia.faaz.general.Constants;
import com.owjmedia.faaz.general.utils.AppSettings;
import com.owjmedia.faaz.general.utils.CustomWidgets.TypefacedEditText;
import com.owjmedia.faaz.general.utils.CustomWidgets.TypefacedTextView;

import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by salman on 11/9/17.
 */

public class ProfileFragment extends Fragment implements ProfileContract.View {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_frg, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mProfilePresenter = new ProfilePresenter(this);
    }

    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {
        mProfilePresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            btnContinue.setVisibility(View.INVISIBLE);
            prg.setVisibility(View.VISIBLE);
        } else {
            btnContinue.setVisibility(View.INVISIBLE);
            prg.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showResponseCode(String code) {
        Snackbar.make(getView(), code, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void profileUpdatedSuccessfully() {
        Snackbar.make(getView(), "Updated Successfully!", Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnContinue)
    public void saveProfile() {
        mProfilePresenter.updateProfile(AppSettings.getString(getContext(), Constants.KEYS.TOKEN), edtGender.getText().toString(), edtCity.getText().toString(),
                Integer.parseInt(edtBirthYear.getText().toString()));
    }


    private ProfileContract.Presenter mProfilePresenter;

    @BindView(R.id.edtGender)
    TypefacedEditText edtGender;

    @BindView(R.id.edtCity)
    TypefacedEditText edtCity;

    @BindView(R.id.edtBirthYear)
    TypefacedEditText edtBirthYear;

    @BindView(R.id.btnContinue)
    TypefacedTextView btnContinue;

    @BindView(R.id.prg)
    ProgressBar prg;
}