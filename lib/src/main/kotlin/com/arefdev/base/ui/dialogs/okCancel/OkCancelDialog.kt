package com.arefdev.base.ui.dialogs.okCancel

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.arefdev.base.R
import com.arefdev.base.databinding.DialogOkCancelBinding
import com.arefdev.base.extensions.sendValue
import com.arefdev.base.ui.base.BaseDialog

/**
 * Updated on 15/06/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class OkCancelDialog private constructor() : BaseDialog<DialogOkCancelBinding>() {

    companion object {

        fun getInstance(context: Context): OkCancelDialog {
            return OkCancelDialog().apply {
                setTitleTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))

                setPositiveText(context.getText(R.string.ok))
                setPositiveTextColor(ContextCompat.getColor(context, R.color.bg))
                setPositiveBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))

                setNegativeText(context.getText(R.string.cancel))
                setNegativeTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                setNegativeBackgroundColor(Color.TRANSPARENT)

                setOnNegativeClickListener { dismiss() }
            }
        }

        fun show(context: Context): OkCancelDialog {
            val dialog = getInstance(context)
            dialog.show(context)
            return dialog
        }
    }


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> DialogOkCancelBinding = DialogOkCancelBinding::inflate
    private val viewModel: OkCancelDialogViewModel by viewModels()

    private val titleText = MutableLiveData<CharSequence>()
    private val titleTextColor = MutableLiveData<Int>()
    private val bodyText = MutableLiveData<CharSequence>()
    private val btnPositiveText = MutableLiveData<CharSequence>()
    private val btnPositiveTextColor = MutableLiveData<Int>()
    private val btnPositiveBackgroundColor = MutableLiveData<Int>()
    private val btnPositiveClickListener = MutableLiveData<OnViewClickListener>()
    private val btnNegativeText = MutableLiveData<CharSequence>()
    private val btnNegativeTextColor = MutableLiveData<Int>()
    private val btnNegativeBackgroundColor = MutableLiveData<Int>()
    private val btnNegativeClickListener = MutableLiveData<OnViewClickListener>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        titleText.observe(instance, viewModel.titleText::sendValue)
        titleTextColor.observe(instance, viewModel.titleTextColor::sendValue)
        bodyText.observe(instance, viewModel.bodyText::sendValue)
        btnPositiveText.observe(instance, viewModel.btnPositiveText::sendValue)
        btnPositiveTextColor.observe(instance, viewModel.btnPositiveTextColor::sendValue)
        btnPositiveBackgroundColor.observe(instance, viewModel.btnPositiveBackgroundColor::sendValue)
        btnPositiveClickListener.observe(instance) { listener ->
            viewModel.btnPositiveClickListener.sendValue(View.OnClickListener { listener.onClick(instance) })
        }
        btnNegativeText.observe(instance, viewModel.btnNegativeText::sendValue)
        btnNegativeTextColor.observe(instance, viewModel.btnNegativeTextColor::sendValue)
        btnNegativeBackgroundColor.observe(instance, viewModel.btnNegativeBackgroundColor::sendValue)
        btnNegativeClickListener.observe(instance) { listener ->
            viewModel.btnNegativeClickListener.sendValue(View.OnClickListener { listener.onClick(instance) })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
    }

    fun setTitle(title: CharSequence?): OkCancelDialog {
        titleText.sendValue(title)
        return this
    }

    fun setTitleTextColor(color: Int): OkCancelDialog {
        titleTextColor.sendValue(color)
        return this
    }

    fun setBody(body: CharSequence?): OkCancelDialog {
        bodyText.sendValue(body)
        return this
    }

    fun setPositiveText(text: CharSequence?): OkCancelDialog {
        btnPositiveText.sendValue(text)
        return this
    }

    fun setNegativeText(text: CharSequence?): OkCancelDialog {
        btnNegativeText.sendValue(text)
        return this
    }

    fun setPositiveTextColor(color: Int): OkCancelDialog {
        btnPositiveTextColor.sendValue(color)
        return this
    }

    fun setNegativeTextColor(color: Int): OkCancelDialog {
        btnNegativeTextColor.sendValue(color)
        return this
    }

    fun setPositiveBackgroundColor(color: Int): OkCancelDialog {
        btnPositiveBackgroundColor.sendValue(color)
        return this
    }

    fun setNegativeBackgroundColor(color: Int): OkCancelDialog {
        btnNegativeBackgroundColor.sendValue(color)
        return this
    }

    fun setOnPositiveClickListener(listener: () -> Unit): OkCancelDialog {
        return setOnPositiveClickListener(object : OnViewClickListener {
            override fun onClick(dialog: DialogFragment) {
                listener.invoke()
            }
        })
    }

    fun setOnPositiveClickListener(listener: OnViewClickListener?): OkCancelDialog {
        btnPositiveClickListener.sendValue(listener)
        return this
    }

    fun setOnNegativeClickListener(listener: () -> Unit): OkCancelDialog {
        return setOnNegativeClickListener(object : OnViewClickListener {
            override fun onClick(dialog: DialogFragment) {
                listener.invoke()
            }
        })
    }

    fun setOnNegativeClickListener(listener: OnViewClickListener?): OkCancelDialog {
        btnNegativeClickListener.sendValue(listener)
        return this
    }

    fun setErrorStyle(): OkCancelDialog {
        setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        setPositiveTextColor(ContextCompat.getColor(requireContext(), R.color.bg))
        setPositiveBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
        setNegativeTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        setNegativeBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg))
        return this
    }
}