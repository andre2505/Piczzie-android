package com.ziggy.kdo.ui.fragment.camera


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.FragmentAddGiftBinding
import com.ziggy.kdo.enums.Error
import com.ziggy.kdo.ui.activity.camera.CameraViewModel
import com.ziggy.kdo.ui.activity.main.MainActivity
import java.io.File


const val ARG_FILE_PATH = "file_path"

/**
 * A simple [Fragment] subclass.
 *
 */
class AddGiftFragment : Fragment() {

    private lateinit var mAddGiftBinding: FragmentAddGiftBinding

    private lateinit var mCameraViewModel: CameraViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        mAddGiftBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_gift, container, false)

        val view = mAddGiftBinding.root

        val image = view.findViewById<ImageView>(R.id.add_gift_image)

        Glide
            .with(activity!!)
            .load(arguments?.getString(ARG_FILE_PATH))
            .into(image)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mCameraViewModel = ViewModelProviders.of(activity!!).get(CameraViewModel::class.java)
        mAddGiftBinding.cameraViewModel = mCameraViewModel
        mAddGiftBinding.setLifecycleOwner(activity!!)

        mCameraViewModel.mValidationSuccess.observe(activity!!, Observer {
            if(it == Error.ERROR_EMPTY_DESCRIPTION){
                Toast.makeText(activity!!, "", Toast.LENGTH_LONG ).show()
            }
            if(it == Error.NO_ERROR){

                // File path
                val file = File(arguments?.getString(ARG_FILE_PATH))
                val intent = Intent(MainActivity.RECEIVER_UPLOAD)
                val gift = mCameraViewModel.mGift.value

                intent.putExtra(MainActivity.EXTRA_GIFT, gift)
                intent.putExtra(MainActivity.EXTRA_FILE, file.path)

                LocalBroadcastManager.getInstance(activity!!).sendBroadcast(intent)

                activity!!.finish()
            }
        })
    }

}
