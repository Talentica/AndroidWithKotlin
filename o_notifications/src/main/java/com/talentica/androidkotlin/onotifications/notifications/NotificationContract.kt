package org.drulabs.notificationswithkotlin.android_o

import android.graphics.Color
import org.drulabs.notificationswithkotlin.BasePresenter
import org.drulabs.notificationswithkotlin.BaseView


interface NotificationContract {

    interface View : BaseView<Presenter> {

        fun displayMessage(message: String)

        fun displayMessage(messageResId: Int)

        fun clearChannelFields()

        fun clearNotificationFields()

    }

    interface Presenter : BasePresenter {

        fun createChannel(id: String, name: CharSequence, importance: Int, showBadge: Boolean,
                          group: String = "Personal", vibrationPattern: LongArray = longArrayOf
        (100, 200, 300, 100, 200, 300, 100, 200, 300))

        fun createNotification(channel: String, title: String, body: String, onGoing: Boolean,
                               color: Int = Color.GREEN)

        fun launchColorPicker(tag: Int = 1)

    }
}