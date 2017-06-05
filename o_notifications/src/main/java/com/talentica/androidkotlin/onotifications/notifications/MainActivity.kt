package com.talentica.androidkotlin.onotifications.notifications

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.*
import com.enrico.colorpicker.colorDialog
import com.talentica.androidkotlin.onotifications.R
import org.drulabs.notificationswithkotlin.android_o.NotificationContract
import org.drulabs.notificationswithkotlin.android_o.NotificationPresenter

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

        channelId = findViewById(R.id.et_channel_id) as EditText
        channelImportance = findViewById(R.id.spinner_channel_importance) as Spinner
        notificationColorPicker = findViewById(R.id.img_pick_color) as ImageView
        isPersonal = findViewById(R.id.chk_channel_group) as CheckBox

        channelCreate = findViewById(R.id.btn_create_channel) as Button

        notificationTitle = findViewById(R.id.et_title) as EditText
        notificationBody = findViewById(R.id.et_body) as EditText
        notificationChannelId = findViewById(R.id.et_channel) as EditText
        isOngoing = findViewById(R.id.chk_ongoing_notification) as CheckBox
        notificationCreate = findViewById(R.id.btn_create_notification) as Button

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
        TODO("not implemented")
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