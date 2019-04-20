package com.ziggy.kdo.ui.fragment.gallery


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.isseiaoki.simplecropview.CropImageView
import com.isseiaoki.simplecropview.callback.CropCallback
import com.isseiaoki.simplecropview.callback.LoadCallback
import com.ziggy.kdo.R
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.navigation.Navigation
import com.isseiaoki.simplecropview.callback.SaveCallback
import com.ziggy.kdo.ui.fragment.camera.ARG_FILE_PATH
import com.ziggy.kdo.utils.FileUtils


/**
 * A simple [Fragment] subclass.
 *
 */
class CropFragment : Fragment(), LoadCallback, View.OnClickListener {

    companion object {
        val EXTRA_IMG_CROP = "extra_img_crop"
    }

    private lateinit var mCropView: CropImageView

    private lateinit var mButtonBack: Button

    private lateinit var mButtonValidate: Button

    private lateinit var mPathImage: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crop, container, false)

        mCropView = view.findViewById(R.id.cropImageView)

        mButtonValidate = view.findViewById(R.id.crop_validate)

        mButtonValidate.setOnClickListener(this)

        mPathImage = arguments?.getString(EXTRA_IMG_CROP)!!

        mCropView.load(Uri.parse("file://$mPathImage")).execute(this)

        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.crop_validate -> {
                mCropView.crop(Uri.parse("file://$mPathImage")).execute(object : CropCallback {
                    override fun onSuccess(cropped: Bitmap) {
                        mCropView.save(cropped)
                            .execute(
                                Uri.fromFile(
                                    FileUtils.getOutputMediaFile(
                                        activity!!,
                                        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                                    )
                                ), object : SaveCallback {
                                    override fun onSuccess(uri: Uri?) {

                                        val path  = uri.toString().replace("file://", "")
                                        val args = Bundle()

                                        args.putString(ARG_FILE_PATH, path)

                                        Navigation.findNavController(view!!)
                                            .navigate(R.id.action_cropFragment_to_addGiftFragment2, args)
                                    }

                                    override fun onError(e: Throwable?) {
                                        Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
                                    }

                                }
                            )
                    }

                    override fun onError(e: Throwable) {
                    }
                })
            }
        }
    }

    override fun onSuccess() {
    }

    override fun onError(e: Throwable?) {
    }
}
