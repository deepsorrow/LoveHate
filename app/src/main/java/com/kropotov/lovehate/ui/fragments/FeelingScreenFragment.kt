package com.kropotov.lovehate.ui.fragments


import android.os.Bundle
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.FeelingType
import com.kropotov.lovehate.data.FeelingType.UNION
import com.kropotov.lovehate.databinding.FragmentFeelingScreenBinding
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utils.serializable
import com.kropotov.lovehate.ui.vm.FeelingScreenVm

class FeelingScreenFragment : BaseFragment<FeelingScreenVm, FragmentFeelingScreenBinding>(R.layout.fragment_feeling_screen) {

    override val vmClass = FeelingScreenVm::class.java

    val feelingType: FeelingType by lazy {
        arguments?.serializable(FEELING_TYPE_ARG, FeelingType::class.java) ?: UNION
    }


    companion object {

        const val FEELING_TYPE_ARG = "arg_feeling_type"

        fun newInstance(type: FeelingType) =
            FeelingScreenFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(FEELING_TYPE_ARG, type)
                }
            }

    }
}