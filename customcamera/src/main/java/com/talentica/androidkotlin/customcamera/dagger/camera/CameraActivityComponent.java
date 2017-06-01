package com.talentica.androidkotlin.customcamera.dagger.camera;

import dagger.Component;
import com.talentica.androidkotlin.customcamera.dagger.ApplicationComponent;
import com.talentica.androidkotlin.customcamera.ui.camera.CameraActivity;

@CameraActivityScope
@Component (
        dependencies = ApplicationComponent.class,
        modules = CameraModule.class
)
public interface CameraActivityComponent {
        void inject(CameraActivity activity);
}
