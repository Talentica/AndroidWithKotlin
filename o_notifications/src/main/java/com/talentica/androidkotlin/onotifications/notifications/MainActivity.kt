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

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.View
import android.widget.*
import com.enrico.colorpicker.colorDialog
import com.talentica.androidkotlin.onotifications.R

class MainActivity : AppCompatActivity(), NotificationContract.View, colorDialog
.ColorSelectedListener, View.OnClickListener {

    private var channelId: EditText? = null
    private var channelImportance: Spinner? = null
    private var isPersonal: CheckBox? = null
    private var channelCreate: Button? = null

    private var notificationTitle: EditText? = null
    private var notificationBody: EditText? = null
    private var notificationChannelId: EditText? = null
    private var isOngoing: CheckBox? = null
    private var notificationColorPicker: ImageView? = null
    private var notificationColor: Int = Color.LTGRAY
    private var notificationCreate: Button? = null

    private var mPresenter: NotificationContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        channelId = findViewById<EditText>(R.id.et_channel_id)
        channelImportance = findViewById<Spinner>(R.id.spinner_channel_importance)
        notificationColorPicker = findViewById<ImageView>(R.id.img_pick_color)
        isPersonal = findViewById<CheckBox>(R.id.chk_channel_group)

        channelCreate = findViewById<Button>(R.id.btn_create_channel)

        notificationTitle = findViewById<EditText>(R.id.et_title)
        notificationBody = findViewById<EditText>(R.id.et_body)
        notificationChannelId = findViewById<EditText>(R.id.et_channel)
        isOngoing = findViewById<CheckBox>(R.id.chk_ongoing_notification)
        notificationCreate = findViewById<Button>(R.id.btn_create_notification)

        channelImportance?.setSelection(3)

        notificationColorPicker?.setOnClickListener(this)
        channelCreate?.setOnClickListener(this)
        notificationCreate?.setOnClickListener(this)

        // Initialize presenter (this immediately calls setPresenter method below)
        // This can easily be replaced with dependency injection framework like dagger
        NotificationPresenter(this, this)

        // Creates 2 notification channel groups. Personal and Business
        mPresenter?.start()
    }

    override fun onColorSelection(dialogFragment: DialogFragment?, color: Int) {
        notificationColor = color
        notificationColorPicker?.setBackgroundColor(color)
    }

    override fun setPresenter(presenter: NotificationContract.Presenter) {
        mPresenter = presenter
    }

    @SuppressLint("WrongConstant")
    override fun displayMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun displayMessage(messageResId: Int) {
        val message = getString(messageResId)
        displayMessage(message)
    }

    override fun clearChannelFields() {
        // not needed as of yet
    }

    override fun clearNotificationFields() {
        notificationTitle?.text?.clear()
        notificationBody?.text?.clear()
        notificationChannelId?.text?.clear()
        isOngoing?.isChecked = false
        notificationColorPicker?.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onDestroy() {
        mPresenter?.destroy()
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.img_pick_color -> {
                    //colorDialog.showColorPicker(this, 1)
                    mPresenter?.launchColorPicker()
                }
                R.id.btn_create_channel -> {
                    val importance = (channelImportance!!).selectedItemPosition
                    val channelIdentifier = channelId?.text.toString()
                    val group = if (isPersonal?.isChecked!!) "Personal" else "Business"
                    mPresenter?.createChannel(channelIdentifier, channelIdentifier, importance,
                            true, group)
                }
                R.id.btn_create_notification -> {
                    mPresenter?.createNotification(notificationChannelId!!.text.toString(),
                            notificationTitle!!.text.toString(), notificationBody!!.text.toString
                    (), isOngoing!!.isChecked, notificationColor)
                }
            }
        }
    }
}
