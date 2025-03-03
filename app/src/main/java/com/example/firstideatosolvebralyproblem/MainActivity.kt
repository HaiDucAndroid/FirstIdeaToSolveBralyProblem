package com.example.firstideatosolvebralyproblem

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {
    private lateinit var intArrayMau: Array<IntArray>
    private lateinit var intArrayChinhSua: Array<IntArray>

    private lateinit var tvToFill: TextView
    private lateinit var tvFilled: TextView
    private lateinit var tvUserFilled: TextView
    private lateinit var tvFilledRight: TextView
    private lateinit var tvFilledWrong: TextView

    var numberOfPixelOriginalToFill = 0
    var numberOfPixelUserFilledRight = 0
    var numberOfPixelUserFilledWrong = 0
    var numberOfPixelOriginalFilled = 0

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ImagePagerAdapter
    private val imageList = mutableListOf<Bitmap>()

    private val pickImages = registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris: List<Uri>? ->
        uris?.let {
            for (uri in it) {
                val bitmap = uriToBitmap(uri)
                imageList.add(bitmap)
            }
            adapter.notifyDataSetChanged() // Cập nhật ViewPager2
        }
    }

    private lateinit var bitmapUser: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBitmapMauAndIntArrayMau()

        bitmapUser = getBitmapFromDrawableWithoutScaling(this@MainActivity, R.drawable.demo_6_co_wrong)
//        compareBitmapUserAndIntArrayMau(bitmapUser)
//        compareBitmapUserAndIntArrayMau(bitmapUser)
//        compareBitmapUserAndIntArrayMau(bitmapUser)

        viewPager = findViewById(R.id.viewPager)
        adapter = ImagePagerAdapter(imageList) { selectedBitmap ->
            Log.d("onCreate", "da pick anh moi")
            bitmapUser = selectedBitmap
            Toast.makeText(this, "da doi anh moi", Toast.LENGTH_SHORT).show()
        }
        viewPager.adapter = adapter


        val buttonCompare = findViewById<Button>(R.id.buttonCompare)
        buttonCompare.setOnClickListener {
            numberOfPixelUserFilledRight = 0
            numberOfPixelUserFilledWrong = 0
            numberOfPixelOriginalFilled = 0
            Log.d("onCreate", "numberOfPixelOriginalToFill: ${numberOfPixelOriginalToFill}")
            Log.d("onCreate", "numberOfPixelOriginalFilled: $numberOfPixelOriginalFilled")
            Log.d("onCreate", "numberOfPixelUserFilledRight: $numberOfPixelUserFilledRight")
            Log.d("onCreate", "numberOfPixelUserFilledWrong: $numberOfPixelUserFilledWrong")
            compareBitmapUserAndIntArrayMau(bitmapUser)
        }
//        buttonCompare.setOnLongClickListener {
//            pickImages.launch(arrayOf("image/*")) // Chọn nhiều ảnh từ thư viện
//            true
//        }
//        val buttonReset = findViewById<Button>(R.id.buttonReset)
//        buttonReset.setOnClickListener {
//            numberOfPixelUserFilledRight = 0
//            numberOfPixelUserFilledWrong = 0
//            numberOfPixelOriginalFilled = 0
//            Log.d("onCreate", "numberOfPixelToFill: ${numberOfPixelOriginalToFill}")
//            Log.d("onCreate", "numberOfPixelUserFilledRight: $numberOfPixelUserFilledRight")
//            Log.d("onCreate", "numberOfPixelUserFilledWrong: $numberOfPixelUserFilledWrong")
//        }
        val buttonPickImage = findViewById<Button>(R.id.buttonPickImage)
        buttonPickImage.setOnClickListener {
            pickImages.launch(arrayOf("image/*")) // Chọn nhiều ảnh từ thư viện
        }

        tvToFill = findViewById<TextView>(R.id.tvToFill)
        tvFilled = findViewById<TextView>(R.id.tvFilled)
        tvUserFilled = findViewById<TextView>(R.id.tvUserFilled)
        tvFilledRight = findViewById<TextView>(R.id.tvUserFilledRight)
        tvFilledWrong = findViewById<TextView>(R.id.tvUserFilledWrong)


    }
    private fun uriToBitmap(uri: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(contentResolver, uri)
    }

    fun initBitmapMauAndIntArrayMau() {
        initIntArrayMau()
        initIntArrayChinhSua()
    }

    fun initIntArrayMau() {
        val bitmapMau = getBitmapFromDrawableWithoutScaling(this@MainActivity, R.drawable.demomau)
        intArrayMau = bitmapToBinaryArray(bitmapMau)
    }

    fun initIntArrayChinhSua() {
        val startTime = System.currentTimeMillis()
        val width = intArrayMau.size
        val height = intArrayMau[0].size
        intArrayChinhSua = Array(height) { IntArray(width) }
        for (y in 0 until height) {
            for (x in 0 until width) {
                intArrayChinhSua[y][x] = intArrayMau[y][x]
            }
        }
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        Log.d("LoadData", "Thời gian load dữ liệu: ${duration}ms")
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

        tvFilled.text = "numberOfPixelOriginalFilled: $numberOfPixelOriginalFilled"
        tvUserFilled.text = "numberOfPixelsUserFilled: $numberOfPixelsUser"
        tvToFill.text = "numberOfPixelOriginalToFill: $numberOfPixelOriginalToFill"
        tvFilledRight.text = "numberOfPixelUserFilledRight: $numberOfPixelUserFilledRight"
        tvFilledWrong.text = "numberOfPixelUserFilledWrong: $numberOfPixelUserFilledWrong"

//        Log.d("compareBitmapUserAndIntArrayMau", "numberOfPixelsUserFilled: $numberOfPixelsUser")
//
//        Log.d("compareBitmapUserAndIntArrayMau", "ket thuc")
//
//        Log.d("compareBitmapUserAndIntArrayMau", "numberOfPixelOriginalToFill: ${numberOfPixelOriginalToFill}")
//        Log.d("compareBitmapUserAndIntArrayMau", "numberOfPixelOriginalFilled: ${numberOfPixelOriginalFilled}")
//        Log.d("compareBitmapUserAndIntArrayMau", "numberOfPixelUserFilledRight: $numberOfPixelUserFilledRight")
//        Log.d("compareBitmapUserAndIntArrayMau", "numberOfPixelUserFilledWrong: $numberOfPixelUserFilledWrong")
//

        initIntArrayChinhSua()
    }

    fun checkSurrounding(x: Int, y: Int, pixel: Int = 1, thresholdToCheck: Int = 1) {
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
                            numberOfPixelOriginalFilled++
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
        } else {
            numberOfPixelUserFilledRight++

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
        numberOfPixelOriginalToFill = 0
//        numberOfPixelOriginalFilled = 0
        val width = bitmap.width
        val height = bitmap.height
        val binaryArray = Array(height) { IntArray(width) }
        for (y in 0 until height) {
            for (x in 0 until width) {
                bitmap.getPixel(x, y).apply {
                    if(!isNearlyBlack(this, threshold)) {
                        binaryArray[y][x] = 1
                        numberOfPixelOriginalToFill++
                    } else {
                        binaryArray[y][x] = 0
                    }
                }
            }
        }
        return binaryArray
    }


}