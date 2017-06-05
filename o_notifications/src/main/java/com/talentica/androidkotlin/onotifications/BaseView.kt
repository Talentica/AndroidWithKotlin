package org.drulabs.notificationswithkotlin

interface BaseView<T> {

    fun setPresenter(presenter: T)

}