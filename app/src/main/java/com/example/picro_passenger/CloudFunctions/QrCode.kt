package com.example.picro_passenger.CloudFunctions

import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import com.google.zxing.WriterException

/**
 * generate(qrcode)
 *
 * Cara menggunakan?
 * QrCode.generate("STRING_DISINI")
 *
 * @param qrcode : String yang berisi token pembayaran
 * @author zredhard
 */
object QrCode {

    fun generate(token: String) : Bitmap? = generate(token, 450, Color.BLACK, Color.TRANSPARENT)

    fun generate(token: String, dimension: Int) : Bitmap? = generate(token, dimension, Color.BLACK, Color.TRANSPARENT)

    fun generate(token: String, dimension: Int, positiveColor: Int, negativeColor: Int) : Bitmap?{
        val qrgEncoder = QRGEncoder(token, null, QRGContents.Type.TEXT, dimension)
        qrgEncoder.colorBlack = positiveColor
        qrgEncoder.colorWhite = Color.TRANSPARENT

        try {
            /** Menampilkan QR kode dari token */
            val bitmap = qrgEncoder.bitmap
            return bitmap
        } catch (e: WriterException) { }

        return null
    }

}