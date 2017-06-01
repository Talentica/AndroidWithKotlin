package com.talentica.androidkotlin.customcamera.dagger.landing;

import dagger.Component;
import com.talentica.androidkotlin.customcamera.dagger.ApplicationComponent;
import com.talentica.androidkotlin.customcamera.ui.landing.LandingActivity;

@LandingActivityScope
@Component (
        dependencies = ApplicationComponent.class,
        modules = LandingModule.class
)
public interface LandingActivityComponent {
        void inject(LandingActivity activity);
}
