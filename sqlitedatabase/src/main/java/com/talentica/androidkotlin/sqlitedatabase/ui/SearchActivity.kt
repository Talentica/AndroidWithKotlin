package com.talentica.androidkotlin.sqlitedatabase.ui

import android.app.LoaderManager
import android.content.Context
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import com.google.gson.JsonObject
import com.talentica.androidkotlin.sqlitedatabase.MainApplication
import com.talentica.androidkotlin.sqlitedatabase.R
import com.talentica.androidkotlin.sqlitedatabase.adapter.LogCursorAdapter
import com.talentica.androidkotlin.sqlitedatabase.model.User
import com.talentica.androidkotlin.sqlitedatabase.server.MockUserService
import rx.android.schedulers.AndroidSchedulers
import rx.android.view.OnClickEvent
import rx.android.view.ViewObservable
import rx.android.widget.OnTextChangeEvent
import rx.android.widget.WidgetObservable
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class SearchActivity : LoaderManager.LoaderCallbacks<Cursor>, AppCompatActivity() {
    private val TAG = "SearchActivity"

    /** Delegates */
    private var mApp: MainApplication by Delegates.notNull()
    private var mAdapter: LogCursorAdapter by Delegates.notNull()

    /** Mocking the server requests by using custom json responses */
    val mServer = MockUserService()

    /** Called whenever a new character is typed in the edittext*/
    val mNameObservable: BehaviorSubject<String> = BehaviorSubject.create()

    /** Called whenever we receive a response from the server about a name */
    val mUserObservable: BehaviorSubject<JsonObject> = BehaviorSubject.create()

    /** intermediate pojo of user*/
    var mUser: User? = null

    //mainThread
    fun mainThread(): String = "Main thread: " + (Looper.getMainLooper() == Looper.myLooper())

    //UI Widgets
    var list: ListView? = null
    var editText: EditText? = null
    var addFriendButton: Button? = null
    var loading: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //initialize the UI widgets
        list = findViewById<ListView>(R.id.list)
        editText = findViewById<EditText>(R.id.editText)
        addFriendButton = findViewById<Button>(R.id.addFriendButton)
        loading = findViewById<ProgressBar>(R.id.loading)

        //initialize the delegates
        mApp = application as MainApplication
        mAdapter = LogCursorAdapter(this, mApp.dbHelper.getLogs())

        //set the adapter
        list?.setAdapter(mAdapter)

        //init loader
        loaderManager.initLoader(0, null, this)

        //and the observers and ENJOY!
        addSearchFieldObservers()
        addUserObservers()
    }

    private fun addSearchFieldObservers() {
        // Whenever a new character is typed
        WidgetObservable.text(editText)
                .doOnNext { e: OnTextChangeEvent ->
                    addFriendButton?.setEnabled(false)
                    loading?.setVisibility(View.INVISIBLE)
                }
                .map { it.text().toString() }
                .filter { it.length >= 3 }
                .debounce(800, TimeUnit.MILLISECONDS)
                .subscribe {
                    mNameObservable.onNext(it)
                }

        //save the name to the logs db and refresh the list using adapter
        mNameObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mApp.dbHelper.log("${it}")
                    mAdapter.changeCursor(mApp.dbHelper.getLogs())
                    mAdapter.notifyDataSetChanged()
                }

        // We have a new name to search, ask the server about it (on the IO thread)
        mNameObservable
                .observeOn(Schedulers.io())
                .subscribe {
                    Log.d(TAG, "Sending to server: ${it} " + mainThread())
                    mServer.findUser(it).subscribe {
                        mUserObservable.onNext(it)
                    }
                }

        // ... and show our loading icon (on the main thread)
        mNameObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    loading?.setVisibility(View.VISIBLE)
                }
    }

    private fun addUserObservers() {
        // Manage the response from the server to "Search", turn the JsonObject into a User,
        // if the response is "ok"
        mUserObservable
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    if (mServer.isOk(it)) {
                        User(it.get("id").asString, it.get("name").asString)
                    } else {
                        null
                    }
                }
                .subscribe {
                    Log.d(TAG, "Enabling add friend")
                    if(it != null) {
                        addFriendButton?.setEnabled(true)
                        val toast = Toast.makeText(this, "Friend found", Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.TOP, 0, 200)
                        toast.show()
                    } else {
                        addFriendButton?.setEnabled(false)
                        val toast = Toast.makeText(this, "Friend not found, try john or alice", Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.TOP, 0, 200)
                        toast.show()
                    }

                    Log.d(TAG, "Hiding loading")
                    loading?.setVisibility(View.INVISIBLE)
                    mUser = it
                }

        // If the user presses the "Add friend" button, we know we have a valid User: send
        // the "Add friend" request to the server
        ViewObservable.clicks(addFriendButton)
                .subscribe { e: OnClickEvent ->
                    mServer.addFriend(mUser!!)
                            .subscribe { jo: JsonObject ->
                                val toastText: String
                                if (mServer.isOk(jo)) {
                                    toastText = "Friend added with name: " + jo.get("name").asString
                                    editText?.setText("")
                                } else {
                                    toastText = "Cannot add friend with name:" + jo.get("name").asString
                                }
                                mAdapter.changeCursor(mApp.dbHelper.getLogs())
                                mAdapter.notifyDataSetChanged()
                                val toast = Toast.makeText(this, toastText, Toast.LENGTH_LONG)
                                toast.setGravity(Gravity.TOP, 0, 200)
                                toast.show()
                            }
                }
    }

    companion object {
        class DbCursorLoader(val app: MainApplication, context: Context) : CursorLoader(context) {
            override fun loadInBackground(): Cursor {
                return app.dbHelper.getLogs()
            }
        }
    }

    override fun onCreateLoader(p0: Int, p1: Bundle?): Loader<Cursor>? {
        return DbCursorLoader(mApp, this)
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, cursor: Cursor?) {
        mAdapter.swapCursor(cursor)
    }

    override fun onLoaderReset(p0: Loader<Cursor>?) {
        mAdapter.swapCursor(null)
    }
}