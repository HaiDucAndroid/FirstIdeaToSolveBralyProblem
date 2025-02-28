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
//        val intArrayUser = bitmapToBinaryArray(bitmapUser)
        intArrayMau = bitmapToBinaryArray(bitmapMau)
//        compare2IntArray(intArrayUser, intArrayMau)
//        bitmapKhongCanToBinaryArray(bitmapUser, bitmapMau)
        compareBitmapUserAndIntArrayMau(bitmapUser)

//        bitmap?.let { printBitmapPixels(it) }

    }
    fun getBitmapFromDrawableWithoutScaling(context: Context, drawableId: Int): Bitmap {
        val options = BitmapFactory.Options().apply {
            inScaled = false // Tắt scale tự động
        }
        return BitmapFactory.decodeResource(context.resources, drawableId, options)
    }

//    fun printBitmapPixels(bitmap: Bitmap) {
//        var i = 0;
//        var j = 0;
//        var k = 0;
//
//        for (y in 0 until bitmap.height) {
//            for (x in 0 until bitmap.width) {
//                val pixel = bitmap.getPixel(x, y)
//                i++;
//                val char = when {
//                    isNearlyBlack(pixel) -> {
//                        k++
//                        '1'
//                    }
//                    else -> {
//                        j++
//                        '@'
//                    }
//                }
//
//                print(char)
//            }
//            println("\n") // Xuống dòng sau mỗi hàng pixel
//        }
//        Log.d("Main", "" + bitmap.height + " " + bitmap.width);
//        Log.d("asdfg", "" + i + " " + j + " " + k);
//    }

//    fun isNearlyWhite(pixel: Int, threshold: Int = 245): Boolean {
//        val red = Color.red(pixel)
//        val green = Color.green(pixel)
//        val blue = Color.blue(pixel)
//        return red > threshold && green > threshold && blue > threshold
//    }

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

//    fun bitmapToBinaryArrayTest(bitmap: Bitmap, threshold: Int = 10): Array<IntArray> {
//        Log.d("bitmapToBinaryArray", "bat dau")
//        val width = bitmap.width
//        val height = bitmap.height
//        val binaryArray = Array(height) { IntArray(width) }
//        var z = 0
//        for (y in 0 until height) {
//            for (x in 0 until width) {
//                val pixel = bitmap.getPixel(x, y)
//                binaryArray[y][x] = if (isNearlyBlack(pixel, threshold)) 1 else {
//                    z++
//                    8
//                }
//            }
//        }
//        Log.d("bitmapToBinaryArray", "$z")
//
//        return binaryArray
//    }

    //    fun bitmapKhongCanToBinaryArray(bitmapUser: Bitmap, bitmapMau: Bitmap, threshold: Int = 10): Array<IntArray> {
//        val width = bitmapUser.width.apply {
//            if (this != bitmapMau.width) {
//                Log.d("bitmapKhongCanToBinaryArray", "width khac nhau")
//            }
//        }
//        val height = bitmapUser.height.apply {
//            if (this != bitmapMau.height) {
//                Log.d("bitmapKhongCanToBinaryArray", "height khac nhau")
//            }
//        }
//        Log.d("bitmapKhongCanToBinaryArray", "bat dau")
//        for (y in 0 until height) {
////            println("$y")
//            for (x in 0 until width) {
//                val pixelUser = bitmapUser.getPixel(x, y)
//                val pixelMau = bitmapMau.getPixel(x, y)
//                if(!isNearlyBlack(pixelUser, threshold) && pixelMau == pixelUser) {
////                    print("$x ")
//                }
//            }
////            println("")
//        }
//        Log.d("bitmapKhongCanToBinaryArray", "ket thuc")
//        val binaryArray = Array(height) { IntArray(width) }
//
//        return binaryArray
//    }
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


    fun printBinaryArray(array: Array<IntArray>) {
        for (row in array) {
            println(row.joinToString(" "))
        }
    }


    fun compare2IntArray(intArrayUser: Array<IntArray>, intArrayMau: Array<IntArray>) {
        var i = 0
        for (y in 0 until intArrayUser.size) {
            for (x in 0 until intArrayUser[0].size) {
                if (intArrayUser[y][x] == intArrayMau[y][x] && intArrayMau[y][x] == 8) {
                    i++
                }
            }
        }
        Log.d("check compare2IntArray", "$i")
    }


}