package com.example.firstideatosolvebralyproblem

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var intArrayMau: Array<IntArray>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bitmapUser = getBitmapFromDrawableWithoutScaling(this@MainActivity, R.drawable.demo4)
        val bitmapMau = getBitmapFromDrawableWithoutScaling(this@MainActivity, R.drawable.demomau)
        intArrayMau = bitmapToBinaryArray(bitmapMau)
        compareBitmapUserAndIntArrayMau(bitmapUser)


    }
    fun getBitmapFromDrawableWithoutScaling(context: Context, drawableId: Int): Bitmap {
        val options = BitmapFactory.Options().apply {
            inScaled = false // Tắt scale tự động
        }
        return BitmapFactory.decodeResource(context.resources, drawableId, options)
    }

    fun isNearlyBlack(pixel: Int, threshold: Int = 10): Boolean {
        val red = Color.red(pixel)
        val green = Color.green(pixel)
        val blue = Color.blue(pixel)
        return red < threshold && green < threshold && blue < threshold
    }

    fun bitmapToBinaryArray(bitmap: Bitmap, threshold: Int = 10): Array<IntArray> {
        val width = bitmap.width
        val height = bitmap.height
        val binaryArray = Array(height) { IntArray(width) }
        for (y in 0 until height) {
            for (x in 0 until width) {
                binaryArray[y][x] = bitmap.getPixel(x, y)
            }
        }

        return binaryArray
    }

    fun compareBitmapUserAndIntArrayMau(bitmapUser: Bitmap, threshold: Int = 10): Array<IntArray> {
        val width = bitmapUser.width.apply {
            if (this != intArrayMau[0].size) {
                Log.d("compareBitmapUserAndIntArrayMau", "width khac nhau")
            } else {
                Log.d("compareBitmapUserAndIntArrayMau", "width bang nhau")
            }
        }
        val height = bitmapUser.height.apply {
            if (this != intArrayMau.size) {
                Log.d("compareBitmapUserAndIntArrayMau", "height khac nhau")
            } else {
                Log.d("compareBitmapUserAndIntArrayMau", "height bang nhau")
            }
        }
        Log.d("compareBitmapUserAndIntArrayMau", "bat dau")
        for (y in 0 until height) {
            println("$y")
            for (x in 0 until width) {
                val pixelUser = bitmapUser.getPixel(x, y)
                val pixelMau = intArrayMau[y][x]
                if(!isNearlyBlack(pixelUser, threshold) && pixelMau == pixelUser) {
                    print("$x ")
                }
            }
            println("")
        }
        Log.d("compareBitmapUserAndIntArrayMau", "ket thuc")
        val binaryArray = Array(height) { IntArray(width) }

        return binaryArray
    }

}