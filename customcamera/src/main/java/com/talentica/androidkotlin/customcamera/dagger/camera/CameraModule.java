package com.talentica.androidkotlin.customcamera.dagger.camera;

import dagger.Module;
import dagger.Provides;
import com.talentica.androidkotlin.customcamera.model.camera.CameraActivityModel;
import com.talentica.androidkotlin.customcamera.model.camera.CameraAdapter;
import com.talentica.androidkotlin.customcamera.presenter.camera.CameraActivityPresenter;
import com.talentica.androidkotlin.customcamera.presenter.camera.CameraActivityPresenterImpl;

@Module
public class CameraModule {

    @CameraActivityScope
    @Provides
    CameraActivityPresenter provideCameraActivityPresenter(CameraActivityModel cameraActivityModel) {
        return new CameraActivityPresenterImpl(
                cameraActivityModel
        );
    }

    @CameraActivityScope
    @Provides
    CameraActivityModel provideCameraActivityModel(CameraAdapter cameraAdapter) {
        return new CameraActivityModel(
                cameraAdapter
        );
    }

    @CameraActivityScope
    @Provides
    CameraAdapter provideCameraAdapter() {
        return new CameraAdapter();
    }
}
