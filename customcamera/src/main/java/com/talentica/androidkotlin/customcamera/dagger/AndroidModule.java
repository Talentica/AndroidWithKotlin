package com.talentica.androidkotlin.customcamera.dagger;

import android.app.Application;
import android.content.Context;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

import com.talentica.androidkotlin.customcamera.BaseApplication;

@Module
public class AndroidModule {

    @Provides
    @Singleton
    Application provideApplication() {
        return BaseApplication.getInstance();
    }

    @Provides
    @Singleton
    Context provideContext() {
        return provideApplication();
    }
}
