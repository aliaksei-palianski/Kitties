package com.github.rtyvZ.kitties.ui.sendPhoto

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.Strings
import com.github.rtyvZ.kitties.ui.services.SendCatService
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.photo_preview.*
import javax.inject.Inject

class TakePhotoActivity @Inject constructor() : AppCompatActivity(R.layout.photo_preview) {

    @Inject
    lateinit var takePhotoRepository: TakeAPhotoRepository

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: TakeAPhotoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        sendPhotoFab.setOnClickListener {
            startService()
            sendResult()
            finish()
        }

        takeFullPhoto()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ACTIVITY_RESULT_CODE && resultCode == RESULT_OK) {
            takePhotoRepository.getImage()?.let {
                photoACat.setImageBitmap(it)
            }
        } else {
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun takeFullPhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(packageManager)?.also {
                val file = takePhotoRepository.createTempFile()
                file?.also {
                    val photoURI: Uri = takePhotoRepository.getPhotoUri(it)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(intent, ACTIVITY_RESULT_CODE)
                }
            }
        }
    }


    private fun startService() {
        val sentPhotoIntent = Intent(this, SendCatService::class.java)
        sentPhotoIntent.data = takePhotoRepository.getPhotoUri()
        startService(sentPhotoIntent)
    }

    private fun sendResult() {
        val resultIntent = Intent()
        resultIntent.putExtra(
            Strings.IntentConsts.EXTRA_SEND_UPLOAD,
            getString(R.string.data_sent)
        )
        setResult(ACTIVITY_RESULT_CODE, resultIntent)
    }

    companion object {
        const val ACTIVITY_RESULT_CODE = 1
    }
}