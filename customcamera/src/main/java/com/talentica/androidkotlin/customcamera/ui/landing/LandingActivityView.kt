package com.talentica.androidkotlin.customcamera.ui.landing

import com.talentica.androidkotlin.customcamera.adapter.landing.LandingGalleryAdapter

interface LandingActivityView {
    open fun closeDrawer()
    fun setAdapter(adapter:LandingGalleryAdapter)
}