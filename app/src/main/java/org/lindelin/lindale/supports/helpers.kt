package org.lindelin.lindale.supports

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Base64
import android.webkit.WebView
import android.widget.ImageView
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

fun ImageView.setImageFromUrl(url: String?) {
    url?.let {
        doAsync {
            val imageUrl = URL(it)
            val connection = imageUrl.openConnection()
            connection.connectTimeout = 6000
            connection.doInput = true
            connection.useCaches = true
            connection.connect()
            val imgBit = connection.getInputStream()
            val bmp = BitmapFactory.decodeStream(imgBit)
            imgBit.close()

            uiThread {
               setImageBitmap(bmp)
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
fun WebView.loadHtmlString(html: String) {
    settings.javaScriptEnabled = true
    val encodedHtml = Base64.encodeToString(html.toByteArray(), Base64.NO_PADDING)
    loadData(encodedHtml, "text/html", "base64")
}