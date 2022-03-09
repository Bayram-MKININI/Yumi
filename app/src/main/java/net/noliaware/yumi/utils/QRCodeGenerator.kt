package net.noliaware.yumi.utils

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

class QRCodeGenerator {

    fun encodeAsBitmap(content: String, width: Int, height: Int): Bitmap? {

        val result: BitMatrix

        try {
            result = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, null)
        } catch (iae: IllegalArgumentException) {
            return null
        }

        val resultWidth = result.width
        val resultHeight = result.height
        val pixels = IntArray(resultWidth * resultHeight)

        for (y in 0 until resultHeight) {

            val offset = y * resultWidth

            for (x in 0 until resultWidth) {
                pixels[offset + x] = if (result.get(x, y)) -0x1000000 else -0x1
            }
        }

        val bitmap = Bitmap.createBitmap(resultWidth, resultHeight, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, resultWidth, 0, 0, resultWidth, resultHeight)
        //createImageFile(bitmap)
        return bitmap
    }
}