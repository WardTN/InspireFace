package com.example.inspireface_example

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.inspireface_example.bean.FaceBean
import com.example.inspireface_example.databinding.ActivityFaceDetectBinding
import com.example.inspireface_example.dialog.AddFaceDataDialog
import com.example.inspireface_example.model.FaceAnalyModel
import com.example.inspireface_example.provider.ImagePickerProvider
import com.example.inspireface_example.utils.checkBitmapValid
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


class FaceDetectActivity : BaseActivity<ActivityFaceDetectBinding>(),AddFaceDataDialog.AddFaceListener  {

    private var imageUri: Uri? = null
    val Cut_PHOTO = 1

    var faceAnalyModel: FaceAnalyModel? = null



    override fun initView() {
        super.initView()
        faceAnalyModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(FaceAnalyModel::class.java)

        databinding.model = faceAnalyModel
        databinding.btnTakePhoto.setOnClickListener {
            takePhoto()
        }



        databinding.btnAlbum.setOnClickListener {

        }
    }

    override fun getViewId(): Int {
        return R.layout.activity_face_detect
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            try {
                val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri!!))
                if (checkBitmapValid(bitmap)) {
                    detectBitmap(bitmap)
                } else {
                    Toast.makeText(this, "Bitmap is invalid", Toast.LENGTH_SHORT).show()
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace();
            }
        }
    }

    private fun detectBitmap(bitmap: Bitmap) {

    }


    private fun takePhoto() {
        //Create a File object to store the selected photo.
        var outputImage =
            File(getPicPath(), "head_" + System.currentTimeMillis() + ".jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete()
            }
            outputImage.createNewFile();
        } catch (e: IOException) {
            e.printStackTrace();
        }

        imageUri = ImagePickerProvider.getUriForFile(
            this,
            applicationContext.packageName + ".provider",
            outputImage
        )
        //This is for temporary file access in versions greater than or equal to Android 7.0,
        // and not having this will cause an exception.

        //Implicit intent to start the camera.
        var intent = Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        // Start the camera application.
        startActivityForResult(intent, Cut_PHOTO)
    }


    //Get the path of the photo.
    private fun getPicPath(): String {
        return (Environment.getExternalStorageDirectory()
            .toString() + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator);
    }


    override fun getImageUri(uri: Uri?) {
        super.getImageUri(uri)
    }
    var dialog: AddFaceDataDialog? = null

    fun showDialog(path: Uri) {
        runOnUiThread {

            dialog= AddFaceDataDialog()
            val args = Bundle()
            args.putParcelable("image_uri", path)
            dialog?.setArguments(args)

            dialog?.bindData(this)
            dialog?.show(supportFragmentManager, "AddFaceDataDialog")
        }
    }

    override fun addFaceData(bean: FaceBean) {
       Toast.makeText(this, "add face data success", Toast.LENGTH_SHORT).show()
    }


}