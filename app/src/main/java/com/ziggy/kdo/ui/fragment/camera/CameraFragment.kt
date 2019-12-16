package com.ziggy.kdo.ui.fragment.camera


import android.os.Bundle
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Flash
import com.ziggy.kdo.R
import com.ziggy.kdo.utils.FileUtils
import java.io.FileOutputStream


/**
 * A simple [Fragment] subclass.
 *
 */
class CameraFragment : Fragment(), View.OnClickListener {

    private lateinit var mButtonFlash: ImageButton

    private lateinit var mContainer: ConstraintLayout

    lateinit var mCameraView: CameraView

    private val mPicture = object : CameraListener() {
        override fun onPictureTaken(result: PictureResult) {
            val data = result.data
            val path = FileUtils.getOutputMediaFile(activity!!, MEDIA_TYPE_IMAGE)
            val fos = FileOutputStream(path)
            fos.write(data)
            fos.close()

            val args = Bundle()
            args.putString(ARG_FILE_PATH, path!!.path)

            Navigation.findNavController(view!!).navigate(R.id.action_cameraFragment_to_addGiftFragment, args)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_camera, container, false)

        val button = view.findViewById<ImageButton>(R.id.button_capture)

        mCameraView = view.findViewById(R.id.camera)
        mCameraView.run {
            setLifecycleOwner(viewLifecycleOwner)
            this.addCameraListener(mPicture)
        }

        view.findViewById<Toolbar>(R.id.toolbar_camera)

        mButtonFlash = view.findViewById(R.id.camera_button_flash)
        mContainer = view.findViewById(R.id.camera_container_button)

        button.setOnClickListener(this@CameraFragment)
        mButtonFlash.setOnClickListener(this@CameraFragment)

        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_capture -> {
                mCameraView.takePictureSnapshot()
            }
            R.id.camera_button_flash -> {
                if (!mButtonFlash.isSelected) {
                    mButtonFlash.isSelected = true
                    mCameraView.flash = Flash.TORCH
                } else {
                    mButtonFlash.isSelected = false
                    mCameraView.flash = Flash.OFF
                }
            }
        }
    }
}
