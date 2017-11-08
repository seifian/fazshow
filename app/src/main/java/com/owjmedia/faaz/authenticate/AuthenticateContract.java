package com.owjmedia.faaz.authenticate;

import com.owjmedia.faaz.general.BasePresenter;
import com.owjmedia.faaz.general.BaseView;



public interface AuthenticateContract {

    interface View extends BaseView<Presenter>{

        void setLoadingIndicator();

        void showPhoneNumeberSentSuccessfully();

        void showAuthenticationCompleted();

        void showMessage(String message);


    }

    interface Presenter extends BasePresenter{

        void sendPhoneNumber(String phoneNumber);

        void sendVerificationCode(String code);

    }
}
