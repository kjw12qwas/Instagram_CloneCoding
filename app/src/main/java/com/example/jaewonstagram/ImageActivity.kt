package com.example.jaewonstagram

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jaewonstagram.model.domain.UploadPictureResponse
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.util.*


class ImageActivity : AppCompatActivity() {
    var imgName = "osz.png" // 이미지 이름

    private val PICK_IMAGE_FROM_ALBUM = 0
    private val REQUEST_CODE = 1
    private var postPath: String? = null
    private var imagePath: String? = null
    private lateinit var img : Bitmap
    private lateinit var selectedImage : Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(this@ImageActivity, "권한 허가", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String?>) {
                Toast.makeText(this@ImageActivity, "권한 거부\n$deniedPermissions", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permissionlistener)
            .setRationaleMessage("구글 로그인을 하기 위해서는 주소록 접근 권한이 필요해요")
            .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .check()

        val imageView: ImageView = findViewById(R.id.showImage_ImageView)
        val image_button : Button = findViewById(R.id.open_Gallery_button)
        image_button.setOnClickListener { openGallery() }
        val upload_button : Button = findViewById(R.id.upload_Gallery_button)
        upload_button.setOnClickListener { uploadImage() }
        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val imageView : ImageView = findViewById(R.id.showImage_ImageView)
            if (resultCode == Activity.RESULT_OK) {
                selectedImage = data?.data!!
                try {
                    val ins : InputStream? = contentResolver.openInputStream(selectedImage)
                    img = BitmapFactory.decodeStream(ins)
                    imageView.setImageBitmap(img)
                    ins?.close()
                    absolutelyPath(img)
                    Log.d("path", postPath.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            //커서 사용해서 경로 확인
        } else {
            Toast.makeText(this, "사진 업로드 실패", Toast.LENGTH_LONG).show()
        }
    }

    fun absolutelyPath(path: Bitmap){
        val tempFile : File = File(cacheDir, imgName)
        try {
            tempFile.createNewFile()
            val out : FileOutputStream = FileOutputStream(tempFile)
            path.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()
            Toast.makeText(applicationContext, "파일 저장 성공", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "파일 저장 실패", Toast.LENGTH_SHORT).show()
        }
    }

    fun uploadImage() {
        /*val timeStamp = SimpleDateFormat("yyyymmdd_HHmmss").format(Date())
        val imageFileName = "IMAGE_" + timeStamp + "_.png"*/

        val token : String = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MjQyMTc1NjQsImlhdCI6MTYyNDEzMTE1OSwic3ViIjoyfQ.POAczanbCl3SdrKW9bN1mYIzdfWJVarB6IcWjPmOC1U"
        // create RequestBody instance from file
        val file = File(cacheDir,imgName)
        // create RequestBody instance from file
        val requestFile = RequestBody.create(
            MediaType.parse("image/jpeg"),
            file
        )
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        Log.d("file", body.toString())
        RetrofitHelper().getPictureAPI().uploadPicture(
            token,
            body
        )
            .enqueue(object : Callback<UploadPictureResponse> {
                override fun onResponse(
                    call: Call<UploadPictureResponse>?,
                    response: Response<UploadPictureResponse>?
                ) {
                    Log.d("success", "통신성공")
                    Log.d("success", body.toString())
                    if (response != null) {
                        if (response.isSuccessful) {

                            Log.e("success : ", response.body().image)
                        }
                    } else {
                        Toast.makeText(applicationContext, "에러", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UploadPictureResponse>?, t: Throwable?) {
                    if (t != null) {
                        t.message?.let { Log.e("error : ", it) }
                    }
                }

            })
    }
}