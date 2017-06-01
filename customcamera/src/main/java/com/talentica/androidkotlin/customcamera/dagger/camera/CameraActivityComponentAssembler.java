package com.talentica.androidkotlin.customcamera.dagger.camera;

import android.app.Application;

import com.talentica.androidkotlin.customcamera.dagger.Components;

public class CameraActivityComponentAssembler {

    private CameraActivityComponentAssembler() {
        throw new AssertionError("No instances.");
    }

    public static CameraActivityComponent assemble(Application application) {
        return DaggerCameraActivityComponent.builder()
                .applicationComponent(Components.from(application))
                .cameraModule(new CameraModule())
                .build();
    }
}
