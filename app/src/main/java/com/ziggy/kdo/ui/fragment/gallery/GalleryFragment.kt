package com.ziggy.kdo.ui.fragment.gallery


import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.AbsListView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ziggy.kdo.R
import com.ziggy.kdo.listener.CustomOnItemClickListener
import com.ziggy.kdo.ui.adapter.GridImageAdapter
import com.ziggy.kdo.utils.SpacesItemDecoration
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 *
 */
class GalleryFragment : Fragment(), CustomOnItemClickListener {

    private val NUM_GRID_COLUMNS = 4

    private lateinit var mGridView: RecyclerView

    private lateinit var mViewAdapter: RecyclerView.Adapter<*>

    private lateinit var mViewManager: RecyclerView.LayoutManager

    private lateinit var mLinearLayoutManager: GridLayoutManager

    private lateinit var mImageSelectGallery: ImageView

    private lateinit var mAdapter: GridImageAdapter

    private var mVisibleItemCount: Int = 0

    private var mTotalItemCount: Int = 0

    private var mFirstVisibleItem: Int = 0

    private var isScrolling: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)
        viewCompanion = view
        mGridView = view.findViewById(R.id.gallery_gridview)
        mImageSelectGallery = view.findViewById(R.id.gallery_image)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupGridView()
    }

    private fun setupGridView() {

        mGridView.apply {

            val gridWitdh = resources.displayMetrics.widthPixels

            val imageWidth = gridWitdh / NUM_GRID_COLUMNS

            val spanCount = 4 // 3 columns

            val spacing = 3 // 50px

            val includeEdge = false

            val dividerItemDecoration = SpacesItemDecoration(spanCount, spacing, includeEdge)

            setHasFixedSize(true)

            mLinearLayoutManager = GridLayoutManager(context, 4)

            mViewManager = mLinearLayoutManager

            layoutManager = mViewManager

            addItemDecoration(dividerItemDecoration)

            activity?.also { theActivity ->
                getdirectoryPaths(30, 0)?.also { theDirectoryPath ->

                    mAdapter =
                        GridImageAdapter(theDirectoryPath, imageWidth, customOnItemClick = this@GalleryFragment)

                    mGridView.adapter = mAdapter

                    Glide
                        .with(mImageSelectGallery)
                        .load(theDirectoryPath[0])
                        .into(mImageSelectGallery)

                    mUrlPath = theDirectoryPath[0]
                }
            }

            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isScrolling = true
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    mVisibleItemCount = childCount
                    mTotalItemCount = layoutManager!!.itemCount
                    mFirstVisibleItem =
                            (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                    if (isScrolling && (mTotalItemCount - mVisibleItemCount) <= (mFirstVisibleItem + mVisibleItemCount)) {
                        isScrolling = false
                        GlobalScope.launch {
                            delay(500)

                            mAdapter.addImage(getdirectoryPaths(30, mAdapter.itemCount) as List<String>)
                        }
                    }
                }
            })

        }
    }

    private fun getdirectoryPaths(limit: Int, offset: Int): MutableList<String>? {

        activity?.let { theActivity ->
            val uri: Uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val cursor: Cursor?
            val columnIndexData: Int?
            val listOfAllImages = ArrayList<String>()
            var absolutePathOfImage: String?

            //select format type
            val mimeTypePng = MimeTypeMap.getSingleton().getMimeTypeFromExtension("png")
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpeg")
            val mimeTypeJpg = MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg")

            //Projection
            val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val selectionArgs = arrayOf(mimeType, mimeTypeJpg, mimeTypePng)

            //Request
            val selectionMimeType = (MediaStore.Images.Media.MIME_TYPE + "=? or "
                    + MediaStore.Images.Media.MIME_TYPE + "=? or "
                    + MediaStore.Images.Media.MIME_TYPE + "=?")

            //final request in database
            cursor = theActivity.contentResolver.query(
                uri,
                projection,
                selectionMimeType,
                selectionArgs,
                MediaStore.MediaColumns.DATE_MODIFIED + " DESC limit $limit offset $offset"
            )


            columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(columnIndexData)

                listOfAllImages.add(absolutePathOfImage)
            }
            return listOfAllImages
        }
        return null
    }

    override fun <T> onItemClick(view: View?, position: Int?, url: String?, varObject: T?) {
        Glide
            .with(mImageSelectGallery)
            .load(url)
            .into(mImageSelectGallery)

        mUrlPath = url
    }

    companion object {

        var mUrlPath: String? = null

        @SuppressLint("StaticFieldLeak")
        var viewCompanion: View? = null

        fun toCrop() {
            val args = Bundle()
            args.putString(CropFragment.EXTRA_IMG_CROP, mUrlPath)
            Navigation.findNavController(viewCompanion!!).navigate(R.id.action_galleryFragment_to_cropFragment, args)
        }
    }
}
