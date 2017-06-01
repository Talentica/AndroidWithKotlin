package com.talentica.androidkotlin.customcamera.dagger

interface HasComponent<out T> {
    val component: T
}
