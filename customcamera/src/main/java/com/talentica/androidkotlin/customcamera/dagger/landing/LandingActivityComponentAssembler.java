package com.talentica.androidkotlin.customcamera.dagger.landing;

import android.app.Application;
import com.talentica.androidkotlin.customcamera.dagger.Components;

public class LandingActivityComponentAssembler {

    private LandingActivityComponentAssembler() {
        throw new AssertionError("No instances.");
    }

    public static LandingActivityComponent assemble(Application application) {
        return DaggerLandingActivityComponent.builder()
                .applicationComponent(Components.from(application))
                .build();
    }
}
