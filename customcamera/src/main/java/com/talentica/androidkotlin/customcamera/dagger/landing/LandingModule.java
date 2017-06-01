package com.talentica.androidkotlin.customcamera.dagger.landing;

import dagger.Module;
import dagger.Provides;
import com.talentica.androidkotlin.customcamera.presenter.landing.LandingActivityPresenter;
import com.talentica.androidkotlin.customcamera.presenter.landing.LandingActivityPresenterImpl;

@Module
public class LandingModule {

    @LandingActivityScope
    @Provides
    LandingActivityPresenter provideLandingActivityPresenter() {

        return new LandingActivityPresenterImpl();
    }
}
