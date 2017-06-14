/*******************************************************************************
 * Copyright 2017 Talentica Software Pvt. Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.talentica.androidkotlin.onotifications.utils

import android.app.*
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.provider.Settings
import com.talentica.androidkotlin.onotifications.R


internal class NotificationHelper(context: Context) : ContextWrapper(context) {

    companion object {
        val DEFAULT_CHANNEL = "The Default One"
    }

    private val mNotificationManager: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun createChannel(id: String?, name: CharSequence?, importance: Int,
                      showBadge: Boolean, group: String, color: Int, vibrationPattern: LongArray) {

        val channelId = id ?: DEFAULT_CHANNEL
        val channelName = name ?: DEFAULT_CHANNEL

        val notificationChannel = NotificationChannel(channelId, channelName, importance)
        notificationChannel.setShowBadge(showBadge)
        notificationChannel.group = group
        notificationChannel.lightColor = color
        notificationChannel.vibrationPattern = vibrationPattern

        mNotificationManager.createNotificationChannel(notificationChannel)
    }

    fun createNotification(channelId: String, title: String, body: String, ticker: String, onGoing:
    Boolean, color: Int): Int {

        if (getAllNotificationChannels().isEmpty() || channelId.trim().isEmpty()) {
            createChannel(DEFAULT_CHANNEL, DEFAULT_CHANNEL, NotificationManager
                    .IMPORTANCE_DEFAULT, true, "Personal", Color.CYAN, longArrayOf(100, 200, 300,
                    100, 200, 300, 100, 200, 300))
        }

        val channel = if (getAllNotificationChannels().contains(channelId)) channelId else
            DEFAULT_CHANNEL

        val notificationSettingsIntent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        notificationSettingsIntent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)

        val pi = PendingIntent.getActivity(applicationContext, 0, notificationSettingsIntent, 0)

        val builder = Notification.Builder(applicationContext, channel).setContentTitle(title)
                .setContentText(body)
                .setTicker(ticker)
                .setGroup(channel)
                .setColor(color)
                .setContentIntent(pi)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(onGoing)
                .build()
        val notificationId = (System.currentTimeMillis() / 1000).toInt()
        mNotificationManager.notify(notificationId, builder)

        return notificationId
    }

    fun createNotificationChannelGroups(){
        mNotificationManager.createNotificationChannelGroup(NotificationChannelGroup("Personal",
                "Personal"))
        mNotificationManager.createNotificationChannelGroup(NotificationChannelGroup("Business",
                "Business"))
    }

    fun getAllNotificationChannels(): List<String> {
        val allChannelIds = mNotificationManager.notificationChannels.map {
            value ->
            value.id
        }

        return allChannelIds
    }

    fun clearAll() {
        mNotificationManager.cancelAll()
    }

}