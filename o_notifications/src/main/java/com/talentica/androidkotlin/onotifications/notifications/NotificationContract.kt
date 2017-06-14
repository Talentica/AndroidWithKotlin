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

package com.talentica.androidkotlin.onotifications.notifications

import android.graphics.Color
import com.talentica.androidkotlin.onotifications.BasePresenter
import com.talentica.androidkotlin.onotifications.BaseView

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