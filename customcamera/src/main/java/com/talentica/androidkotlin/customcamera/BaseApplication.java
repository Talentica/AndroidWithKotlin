package com.talentica.androidkotlin.customcamera;

import android.app.Application;
import com.talentica.androidkotlin.customcamera.dagger.AndroidModule;
import com.talentica.androidkotlin.customcamera.dagger.ApplicationComponent;
import com.talentica.androidkotlin.customcamera.dagger.DaggerApplicationComponent;

import timber.log.Timber;

public class BaseApplication extends Application {

    protected ApplicationComponent component;
    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        component = provideDependencyComponent();
        provideLogging();
        provideCrashlytics();
    }

    protected void provideCrashlytics() {
    }

    public ApplicationComponent getComponent() {
        return component;
    }

    protected ApplicationComponent provideDependencyComponent() {
         return DaggerInitializer.init();
    }

    private final static class DaggerInitializer {
        private static ApplicationComponent init() {
            return DaggerApplicationComponent.builder()
                    .androidModule(new AndroidModule())
                    .build();
        }
    }

    protected void provideLogging() {
        Timber.plant(new Timber.HollowTree());
    }

    public static BaseApplication getInstance() {
        return instance;
    }
}
