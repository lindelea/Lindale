package org.lindelin.lindale.supports

import android.graphics.BitmapFactory
import android.widget.ImageView
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