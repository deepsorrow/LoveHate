package com.kropotov.lovehate.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.items.MediaListItem
import com.kropotov.lovehate.databinding.DialogNewOpinionBinding
import com.kropotov.lovehate.ui.adapters.lists.MediaListAdapter
import com.kropotov.lovehate.ui.screens.topicpage.fragments.TopicPageFragment.Companion.TOPIC_PAGE_ID
import com.kropotov.lovehate.ui.utilities.autoCleared
import com.kropotov.lovehate.ui.utilities.withArgs
import com.kropotov.lovehate.ui.screens.topicpage.NewOpinionViewModel
import com.kropotov.lovehate.ui.utilities.getColorAttr
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.launch
import javax.inject.Inject


class NewOpinionDialog : BottomSheetDialogFragment(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModel: NewOpinionViewModel
    private var binding by autoCleared<DialogNewOpinionBinding>()

    private var adapter: MediaListAdapter? = null

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun getTheme(): Int = R.style.BottomSheetDialogThemeNoFloating

//    var launcher = registerForActivityResult<PickVisualMediaRequest, Uri>(
//        ActivityResultContracts.PickVisualMedia()
//    ) {
//        Glide.with(requireActivity().applicationContext).load(it).into<Target<Drawable>>(imageView)
//    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapter = MediaListAdapter(childFragmentManager)
        binding = DialogNewOpinionBinding.inflate(inflater, container, false).apply {
            viewModel = this@NewOpinionDialog.viewModel
            mediaList.adapter = adapter
            textField.requestKeyboardFocus()
        }

        lifecycleScope.launch {
            viewModel.dismissDialog.collect {
                dismiss()
            }
        }

        lifecycleScope.launch {
            viewModel.opinionState.collect { opinionType ->
                val colorInt = requireContext().getColorAttr(opinionType.color)
                binding.button.setBackgroundColor(colorInt)
            }
        }

        childFragmentManager.setFragmentResultListener(
            MEDIA_PICKED_EVENT,
            this
        ) { _, result ->
            result.getString(MEDIA_PICKED_EVENT)?.let {
                viewModel.mediaPaths.add(it)
                adapter?.addMedia(MediaListItem(it))
            }
        }

        return binding.root
    }

    companion object {
        const val MEDIA_PICKED_EVENT = "media_picked_event"
        fun newInstance(topicId: Int) = NewOpinionDialog().withArgs {
            putInt(TOPIC_PAGE_ID, topicId)
        }
    }
}
