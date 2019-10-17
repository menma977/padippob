package com.padippob

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import org.json.JSONArray
import java.net.URL
import java.util.ArrayList

class ImageAdapter internal constructor(private val thisContext: Context, private val arrayBitmap : ArrayList<Bitmap>) : PagerAdapter() {
    private val imageId = arrayBitmap

    override fun getCount(): Int {
        return imageId.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(thisContext)
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        imageView.setImageBitmap(arrayBitmap[position])
        container.addView(imageView, 0)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ImageView)
    }



    @SuppressLint("NewApi")
    class SetBackgroundImage(responseData : JSONArray) : AsyncTask<Void, Void, ArrayList<Bitmap>>() {
        private val response = responseData
        override fun doInBackground(vararg params: Void?): ArrayList<Bitmap>? {
            val arrayBitmap = ArrayList<Bitmap>()
            for (value in 0 until response.length()) {
                val url = URL("https://www.padippob.com/banner/${response.getJSONObject(value)["name"]}")
                arrayBitmap.add(BitmapFactory.decodeStream(url.openConnection().getInputStream()))
            }
            return arrayBitmap
        }
    }
}
