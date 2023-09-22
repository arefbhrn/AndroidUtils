package com.arefdev.base.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.arefdev.base.databinding.WaitableRoundedButtonBinding
import com.arefdev.base.extensions.sendValue

/**
 * Updated on 30/06/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class WaitableRoundedButton : RoundedButton {

    companion object {
        @BindingAdapter("app:text")
        fun setText(button: WaitableRoundedButton, text: CharSequence?) {
            button.setTextt(text)
        }
    }

    private val text = MutableLiveData<CharSequence>()
    private val loading = MutableLiveData<Boolean>()

    private lateinit var binding: WaitableRoundedButtonBinding

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        binding = WaitableRoundedButtonBinding.inflate(LayoutInflater.from(context), this, true)

        text.observeForever { binding.text = it }
        loading.observeForever { binding.loading = it }
    }

    fun setLoading(loading: Boolean) {
        this.loading.sendValue(loading)
    }

    private fun setTextt(text: CharSequence?) {
        this.text.sendValue(text)
    }

    private fun getTextt(): CharSequence? {
        return this.text.value
    }
}