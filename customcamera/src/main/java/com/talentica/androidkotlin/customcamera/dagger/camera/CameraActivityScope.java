package com.talentica.androidkotlin.customcamera.dagger.camera;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

@Retention(RetentionPolicy.RUNTIME)
@Scope
public @interface CameraActivityScope {
}
