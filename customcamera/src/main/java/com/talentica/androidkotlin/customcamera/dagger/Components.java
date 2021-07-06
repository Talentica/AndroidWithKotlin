package com.talentica.androidkotlin.customcamera.dagger;

import android.app.Activity;
import android.app.Application;
import androidx.annotation.NonNull;

import com.talentica.androidkotlin.customcamera.BaseApplication;

public class Components {

    private Components() {
        throw new AssertionError("No instances.");
    }

    public static <T> T from(@NonNull Activity activity) {
        return ((HasComponent<T>) activity).getComponent();
    }

    public static ApplicationComponent from(@NonNull Application application) {
        return ((BaseApplication) application).getComponent();
    }
}
