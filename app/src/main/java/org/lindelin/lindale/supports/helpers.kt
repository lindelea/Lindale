package org.lindelin.lindale.supports

import android.graphics.BitmapFactory
import android.widget.ImageView
import com.github.kittinunf.fuel.httpGet
import kotlinx.android.synthetic.main.nav_header_main.view.*

fun ImageView.setImageFromUrl(url: String?) {
    url?.let {
        val httpAsync = it.httpGet().response { result ->

            val (data, _) = result

            data?.let {
                setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
            }
        }

        httpAsync.join()
    }
}