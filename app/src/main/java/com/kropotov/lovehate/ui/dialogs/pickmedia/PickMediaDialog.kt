package com.kropotov.lovehate.ui.dialogs.pickmedia

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.items.MediaListItem
import com.kropotov.lovehate.databinding.DialogPickMediaBinding
import com.kropotov.lovehate.ui.adapters.lists.PickMediaListAdapter
import com.kropotov.lovehate.ui.base.BaseBottomSheetDialogFragment
import com.kropotov.lovehate.ui.utilities.GridSpaceItemDecoration
import okio.FileNotFoundException

class PickMediaDialog : BaseBottomSheetDialogFragment<PickMediaViewModel, DialogPickMediaBinding>(
    R.layout.dialog_pick_media
) {

    override val vmClass = PickMediaViewModel::class.java

    private var adapter: PickMediaListAdapter? = null

    private val requestPermissionLauncher = registerForActivityResult(
        RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            fetchImages()
        } else {
            dismiss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PickMediaListAdapter { selectedItem ->
            selectedItem.filePath = getFilePath(selectedItem)
            val bundle = bundleOf(MEDIA_PICKED_KEY to selectedItem)
            parentFragmentManager.setFragmentResult(MEDIA_PICKED_KEY, bundle)
            dismiss()
        }
        checkPermissionAndFetchImages()

        binding.apply {
            list.adapter = adapter

            val spacing = resources.getDimension(R.dimen.very_small_offset).toInt()
            list.addItemDecoration(GridSpaceItemDecoration(GRID_SPAN_COUNT, spacing, true))
            list.layoutManager =
                GridLayoutManager(
                    requireContext(),
                    GRID_SPAN_COUNT,
                    GridLayoutManager.VERTICAL,
                    false
                )
        }
    }

    private fun checkPermissionAndFetchImages() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            fetchImages()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchImages() {
        val galleryImageUrls = mutableListOf<MediaListItem>()
        val columns = arrayOf(MediaStore.Images.Media._ID)
        val orderBy = MediaStore.Images.Media.DATE_TAKEN

        requireContext().applicationContext.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
            null, null, "$orderBy DESC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val item = MediaListItem(
                    uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    ),
                    id = id
                )
                galleryImageUrls.add(item)
            }
        }

        adapter?.items = galleryImageUrls
        adapter?.notifyDataSetChanged()
    }

    private fun getFilePath(mediaListItem: MediaListItem): String {
        requireContext().applicationContext.contentResolver.query(
            mediaListItem.uri!!, arrayOf(MediaStore.Images.Media.DATA),
            null, null, null
        )?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst()
            return cursor.getString(columnIndex).also {
                cursor.close()
            }
        }
        throw FileNotFoundException("File path was not found by uri!")
    }

    companion object {
        const val MEDIA_PICKED_KEY = "media_picked_event"

        private const val GRID_SPAN_COUNT = 3
    }
}
