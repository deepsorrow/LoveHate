package com.kropotov.lovehate.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.kropotov.lovehate.R
import com.kropotov.lovehate.databinding.LoveHateTextFieldBinding
import com.kropotov.lovehate.ui.utilities.getColorAttr
import com.kropotov.lovehate.ui.utilities.showKeyboard

class LoveHateTextField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var opinionStateListener: OpinionStateListener? = null

    private var path: Path? = null

    private val binding: LoveHateTextFieldBinding =
        LoveHateTextFieldBinding.inflate(LayoutInflater.from(context), this)

    private val loveButton
        get() = binding.loveToggleButton

    private val neutralButton
        get() = binding.neutralToggleButton

    private val hateButton
        get() = binding.hateToggleButton

    val editText
        get() = binding.editText

    var text: String = ""
        get() = binding.editText.text.toString()
        set(value) {
            binding.editText.setText(value)
            field = value
        }

    private var maxLength = 70

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoveHateTextField,
            0,
            0
        ).apply {
            try {
                val hint = getString(R.styleable.LoveHateTextField_hint)
                maxLength = getInt(R.styleable.LoveHateTextField_maxLength, 70)

                binding.editText.apply {
                    this.hint = hint
                    filters = arrayOf(InputFilter.LengthFilter(maxLength))
                }
            } finally {
                recycle()
            }
        }

        setOnCheckedListeners()
        subscribeToCharacterCountdown()
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        val cornerRadius = resources.getDimensionPixelSize(R.dimen.default_corner_radius).toFloat()
        // Only top corners will be rounded
        val corners = floatArrayOf(
            cornerRadius, cornerRadius,
            cornerRadius, cornerRadius,
            0f, 0f,
            0f, 0f
        )

        path = Path()
        path!!.addRoundRect(
            RectF(0F, 0F, width.toFloat(), height.toFloat()),
            corners,
            Path.Direction.CW
        )
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (path != null) {
            canvas.clipPath(path!!)
        }
        super.dispatchDraw(canvas)
    }

    fun setOpinionStateListener(opinionStateListener: OpinionStateListener) {
        this.opinionStateListener = opinionStateListener
    }

    private fun setOnCheckedListeners() {
        loveButton.setOnCheckedListener()
        neutralButton.setOnCheckedListener()
        hateButton.setOnCheckedListener()
    }

    private fun CompoundButton.setOnCheckedListener() =
        setOnCheckedChangeListener { compoundButton, _ ->
            if (!loveButton.isChecked && !neutralButton.isChecked && !hateButton.isChecked) {
                // Prevent self-uncheck
                compoundButton.isChecked = !compoundButton.isChecked
            } else if (compoundButton.isChecked) {

                // Uncheck other buttons and highlight editText with colorful stroke
                val strokeColorTintAttr = when (compoundButton) {
                    loveButton -> {
                        opinionStateListener?.onLoveClicked()

                        neutralButton.isChecked = false
                        hateButton.isChecked = false
                        R.attr.love_container_color
                    }
                    hateButton -> {
                        opinionStateListener?.onHateClicked()

                        loveButton.isChecked = false
                        neutralButton.isChecked = false
                        R.attr.hate_container_color
                    }
                    else -> {
                        opinionStateListener?.onNeutralClicked()

                        loveButton.isChecked = false
                        hateButton.isChecked = false
                        R.attr.neutral_container_color
                    }
                }
                val colorTint = context.getColorAttr(strokeColorTintAttr)
                val widthPx = resources.getDimension(R.dimen.one_dp).toInt()
                (binding.editText.background as GradientDrawable).setStroke(widthPx, colorTint)
            }
        }

    private fun subscribeToCharacterCountdown() {
        binding.titleCountdown.text = "$maxLength"
        binding.editText.addTextChangedListener {
            if (it != null) {
                binding.titleCountdown.text = (maxLength - it.length).toString()
            }
        }
    }

    interface OpinionStateListener {
        fun onLoveClicked()

        fun onNeutralClicked()

        fun onHateClicked()
    }

    companion object {
        @BindingAdapter("inputTextAttrChanged")
        @JvmStatic
        fun setListener(textField: LoveHateTextField, listener: InverseBindingListener) {
            textField.editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    listener.onChange()
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })
        }

        @BindingAdapter("inputText")
        @JvmStatic
        fun setTextValue(textField: LoveHateTextField, value: String) {
             if (value != textField.text) {
                 textField.text = value
             }
        }

        @InverseBindingAdapter(attribute = "inputText")
        @JvmStatic
        fun getTextValue(textField: LoveHateTextField) = textField.text
    }
}
