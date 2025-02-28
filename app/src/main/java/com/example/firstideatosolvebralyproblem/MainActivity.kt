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
    private lateinit var intArrayChinhSua: Array<IntArray>

    var numberOfPixelToFill = 0
    var numberOfPixelUserFilledRight = 0
    var numberOfPixelUserFilledWrong = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBitmapMauAndIntArrayMau()

        val bitmapUser = getBitmapFromDrawableWithoutScaling(this@MainActivity, R.drawable.demo_6_co_wrong)
        compareBitmapUserAndIntArrayMau(bitmapUser)


    }

    fun initBitmapMauAndIntArrayMau() {
        val bitmapMau = getBitmapFromDrawableWithoutScaling(this@MainActivity, R.drawable.demomau)

        intArrayMau = bitmapToBinaryArray(bitmapMau)
        intArrayChinhSua = bitmapToBinaryArray(bitmapMau)
    }

    //TODO: check intArrayMau co empty khong roi moi duoc intArrayMau[0].size k la bi null exception
    fun compareBitmapUserAndIntArrayMau(bitmapUser: Bitmap, threshold: Int = 10) {
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
        var numberOfPixelsUser = 0

        for (y in 0 until height) {
            for (x in 0 until width) {
                bitmapUser.getPixel(x, y). apply {
                    if(!isNearlyBlack(this, threshold)) {
                        numberOfPixelsUser++
//                        Log.d("compareBitmapUserAndIntArrayMau", "tim duoc net ve user tại điểm ($x, $y) pixel = $this")
                        checkSurrounding(x, y)
                    }
                }
            }
        }
        Log.d("compareBitmapUserAndIntArrayMau", "numberOfPixelsUser: $numberOfPixelsUser")

        Log.d("compareBitmapUserAndIntArrayMau", "ket thuc")

        Log.d("compareBitmapUserAndIntArrayMau", "numberOfPixelToFill: ${numberOfPixelToFill / 2}")
        Log.d("compareBitmapUserAndIntArrayMau", "numberOfPixelUserFilledRight: $numberOfPixelUserFilledRight")
        Log.d("compareBitmapUserAndIntArrayMau", "numberOfPixelUserFilledWrong: $numberOfPixelUserFilledWrong")

    }

    fun checkSurrounding(x: Int, y: Int, pixel: Int = 1, thresholdToCheck: Int = 2) {
//        val sb: StringBuilder = StringBuilder()
        var isRightPixel = false
        for (i in (x - thresholdToCheck)..(x + thresholdToCheck)) {
            for (j in (y - thresholdToCheck)..(y + thresholdToCheck)) {
                if (i in 0 until intArrayMau[0].size && j in 0 until intArrayMau.size) { // Kiểm tra trong giới hạn mảng
                    if (pixel == intArrayMau[j][i]) {
//                        sb.append("($i, $j) ")
                        isRightPixel = true
                        if (pixel == intArrayChinhSua[j][i]) {
//                            sb.append("ok+1 ")
                            //pixel nay khac mau den (check qua !isNearlyBlack) r nen k can lo nua
                            intArrayChinhSua[j][i] = -1
                            numberOfPixelUserFilledRight++
                        } else {
//                            sb.append("da co ")
                        }
                    }
                }
            }
        }

        if (!isRightPixel) {
//            sb.append("sai roi")

            numberOfPixelUserFilledWrong++
        }
//        Log.d("checkSurrounding", sb.toString())
    }

    fun getBitmapFromDrawableWithoutScaling(context: Context, drawableId: Int): Bitmap {
        val options = BitmapFactory.Options().apply {
            inScaled = false // Tắt scale tự động
        }
        return BitmapFactory.decodeResource(context.resources, drawableId, options)
    }

    fun isNearlyBlack(pixel: Int, threshold: Int = 100): Boolean {
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
                bitmap.getPixel(x, y).apply {
                    if(!isNearlyBlack(this, threshold)) {
                        binaryArray[y][x] = 1
                        numberOfPixelToFill++
                    } else {
                        binaryArray[y][x] = 0
                    }
                }
            }
        }
        return binaryArray
    }


}