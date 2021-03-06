package com.owjmedia.faaz.profile;

import com.owjmedia.faaz.profile.model.ProfileResponse;
import com.owjmedia.faaz.profile.model.UpdateProfileRequest;
import com.owjmedia.faaz.general.networking.Injector;
import com.owjmedia.faaz.general.networking.ApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by salman on 11/9/17.
 */

public class ProfilePresenter implements ProfileContract.Presenter {

    private final ProfileContract.View mProfileView;

    public ProfilePresenter(ProfileContract.View profileView) {
        mProfileView = profileView;
    }


    @Override
    public void updateProfile(int gender, String city, int year_of_birth) {
        UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest(gender, city, year_of_birth);
        Call<ResponseBody> call = Injector.provideApiService().updateProfile(updateProfileRequest);
        mProfileView.setLoadingIndicator(true);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mProfileView.setLoadingIndicator(false);
                if (response.code() == 200)
                    mProfileView.profileUpdatedSuccessfully();
                else
                    mProfileView.showMessage(response.message());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mProfileView.setLoadingIndicator(false);
                mProfileView.showMessage(t.getMessage());
            }
        });
    }

    @Override
    public void getProfile() {
        Call<ProfileResponse> call = Injector.provideApiService().getProfile();
        mProfileView.setLoadingIndicator(true);

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                mProfileView.setLoadingIndicator(false);
                if (response.code() == 200)
                    mProfileView.showProfile(response.body());
                else
                    mProfileView.showMessage(response.message());
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                mProfileView.setLoadingIndicator(false);
                mProfileView.showMessage(t.getMessage());
            }
        });

    }
}
