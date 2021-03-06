package com.letion.arch;

/**
 * Created by Chatikyan on 07.07.2017.
 */

public interface BaseLoadingMVPContract {

    interface View extends BaseMVPContract.View {

        void showLoading();

        void hideLoading();

        void showError(String errorMessage);
    }

    interface Presenter extends BaseMVPContract.Presenter<View> {

    }

}
