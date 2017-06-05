package org.drulabs.notificationswithkotlin.android_o

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import com.enrico.colorpicker.colorDialog
import com.talentica.androidkotlin.onotifications.R
import org.drulabs.notificationswithkotlin.utils.NotificationHelper


class NotificationPresenter(context: Activity, view: NotificationContract.View) : NotificationContract.Presenter {

    private val view: NotificationContract.View = view
    private val mNotificationHelper: NotificationHelper
    private val context: Context

    init {
        this.view.setPresenter(this)
        this.mNotificationHelper = NotificationHelper(context)
        this.context = context
    }

    override fun start() {
        // start
        mNotificationHelper.createNotificationChannelGroups()
    }

    override fun destroy() {
        // destroy
        mNotificationHelper.clearAll()
    }

    override fun createChannel(id: String, name: CharSequence, importance: Int, showBadge:
    Boolean, group: String, vibrationPattern: LongArray) {

        if (id.trim().isEmpty() || name.trim().isEmpty()) {
            view.displayMessage(R.string.empty_params_msg)
            return
        }

        if (mNotificationHelper.getAllNotificationChannels().contains(id)) {
            view.displayMessage(R.string.channel_already_exists_msg)
            return
        }

        var channelImportance = 0

        when (importance) {
            in 0..5 -> channelImportance = importance
            else -> channelImportance = NotificationManager.IMPORTANCE_UNSPECIFIED
        }

        mNotificationHelper.createChannel(id, name, channelImportance, showBadge, group, Color.CYAN,
                vibrationPattern)

        // end this with notifying the view
        view.displayMessage(R.string.channel_created_msg)
        view.clearNotificationFields()
    }

    override fun createNotification(channel: String, title: String, body: String, onGoing:
    Boolean, color: Int) {

        if (!mNotificationHelper.getAllNotificationChannels().contains(channel)) {
            view.displayMessage(R.string.channel_doesnt_exist_msg)
            return
        }

        val notifTitle = if (title.trim().isEmpty()) context.getString(R.string
                .default_noification_title) else title
        val notifBody = if (body.trim().isEmpty()) context.getString(R.string
                .default_noification_body) else body

        mNotificationHelper.createNotification(channel, notifTitle, notifBody, notifTitle,
                onGoing, color)

        // end this with notifying the view
        // view.displayMessage(/*Some message*/)
    }

    override fun launchColorPicker(tag: Int) {
        colorDialog.showColorPicker(context as AppCompatActivity?, tag)
    }
}