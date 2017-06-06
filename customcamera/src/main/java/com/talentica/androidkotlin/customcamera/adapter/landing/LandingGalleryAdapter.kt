package com.talentica.androidkotlin.customcamera.adapter.landing

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.talentica.androidkotlin.customcamera.R
import com.talentica.androidkotlin.customcamera.utils.Utils

/**
 * Created by suyashg on 03/06/17.
 */
class LandingGalleryAdapter(private val mContext: Context) : BaseAdapter() {
    internal var itemList = ArrayList<String>()

    override fun getCount(): Int {
        return itemList.count()
    }

    override fun getItem(pos: Int): String {
        return itemList.get(pos)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var viewItem : View?
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            val mInflater: LayoutInflater = LayoutInflater.from(mContext)
            viewItem = mInflater.inflate(R.layout.item_grid, null)
        } else {
            viewItem = convertView
        }

        val bm = decodeSampledBitmapFromUri(itemList[position], 220, 220)
        val imv:ImageView = viewItem?.findViewById<ImageView>(R.id.pic)!!
        imv.setImageBitmap(bm)
        return viewItem!!
    }

    fun decodeSampledBitmapFromUri(path: String, reqWidth: Int, reqHeight: Int): Bitmap {
        var bm: Bitmap? = null
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        bm = BitmapFactory.decodeFile(path, options)
        return bm
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round(height.toFloat() / reqHeight.toFloat())
            } else {
                inSampleSize = Math.round(width.toFloat() / reqWidth.toFloat())
            }
        }
        return inSampleSize
    }

    internal fun add(path: String) {
        itemList.add(path)
    }

    internal fun refreshFilesFromFolder() {
        val files = Utils().storageDir.listFiles()
        itemList = ArrayList<String>()
        if (files != null) {
            itemList = ArrayList<String>()
            for (file in files) {
                add(file.getAbsolutePath())
            }
        }
        this.notifyDataSetChanged()
    }
}
