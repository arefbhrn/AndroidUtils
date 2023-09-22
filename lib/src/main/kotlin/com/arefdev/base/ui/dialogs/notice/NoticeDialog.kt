package com.arefdev.base.ui.dialogs.notice

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.arefdev.base.R
import com.arefdev.base.databinding.DialogNoticeBinding
import com.arefdev.base.extensions.sendValue
import com.arefdev.base.ui.base.BaseDialog

/**
 * Updated on 15/06/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class NoticeDialog private constructor() : BaseDialog<DialogNoticeBinding>() {

    companion object {

        fun getInstance(context: Context): NoticeDialog {
            return NoticeDialog().apply {
                setButtonText(context.getText(R.string.ok))
                setButtonTextColor(ContextCompat.getColor(context, R.color.bg))
                setButtonBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                setOnConfirmClickListener { dismiss() }
            }
        }

        fun show(context: Context): NoticeDialog {
            val dialog = getInstance(context)
            dialog.show(context)
            return dialog
        }
    }


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> DialogNoticeBinding = DialogNoticeBinding::inflate
    private val viewModel: NoticeDialogViewModel by viewModels()

    private val titleText = MutableLiveData<CharSequence>()
    private val bodyText = MutableLiveData<CharSequence>()
    private val btnConfirmText = MutableLiveData<CharSequence>()
    private val btnConfirmTextColor = MutableLiveData<Int>()
    private val btnConfirmBackgroundColor = MutableLiveData<Int>()
    private val btnConfirmClickListener = MutableLiveData<View.OnClickListener>()
    private val btnSecondText = MutableLiveData<CharSequence>()
    private val btnSecondTextColor = MutableLiveData<Int>()
    private val btnSecondBackgroundColor = MutableLiveData<Int>()
    private val btnSecondClickListener = MutableLiveData<View.OnClickListener>()
    private val layoutTwoStateVisibility = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        titleText.observe(instance, viewModel.titleText::sendValue)
        bodyText.observe(instance, viewModel.bodyText::sendValue)
        btnConfirmText.observe(instance, viewModel.btnConfirmText::sendValue)
        btnConfirmTextColor.observe(instance, viewModel.btnConfirmTextColor::sendValue)
        btnConfirmBackgroundColor.observe(instance, viewModel.btnConfirmBackgroundColor::sendValue)
        btnConfirmClickListener.observe(instance, viewModel.btnConfirmClickListener::sendValue)
        btnSecondText.observe(instance, viewModel.btnSecondText::sendValue)
        btnSecondTextColor.observe(instance, viewModel.btnSecondTextColor::sendValue)
        btnSecondBackgroundColor.observe(instance, viewModel.btnSecondBackgroundColor::sendValue)
        btnSecondClickListener.observe(instance, viewModel.btnSecondClickListener::sendValue)
        btnSecondClickListener.observe(instance, viewModel.btnSecondClickListener::sendValue)
        layoutTwoStateVisibility.observe(instance, viewModel.layoutTwoStateVisibility::sendValue)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
    }

    fun setTitle(body: CharSequence?): NoticeDialog {
        titleText.sendValue(body)
        return this
    }

    fun setBody(body: CharSequence?): NoticeDialog {
        bodyText.sendValue(body)
        return this
    }

    fun setButtonText(text: CharSequence?): NoticeDialog {
        btnConfirmText.sendValue(text)
        return this
    }

    fun setButtonTextColor(color: Int): NoticeDialog {
        btnConfirmTextColor.sendValue(color)
        return this
    }

    fun setButtonBackgroundColor(color: Int): NoticeDialog {
        btnConfirmBackgroundColor.sendValue(color)
        return this
    }

    fun setOnConfirmClickListener(onClickListener: View.OnClickListener?): NoticeDialog {
        btnConfirmClickListener.sendValue(onClickListener)
        return this
    }

    fun setSecondButtonText(text: CharSequence?): NoticeDialog {
        btnSecondText.sendValue(text)
        return this
    }

    fun setSecondButtonTextColor(color: Int): NoticeDialog {
        btnSecondTextColor.sendValue(color)
        return this
    }

    fun setSecondButtonBackgroundColor(color: Int): NoticeDialog {
        btnSecondBackgroundColor.sendValue(color)
        return this
    }

    fun setOnSecondButtonClickListener(onClickListener: View.OnClickListener?): NoticeDialog {
        btnSecondClickListener.sendValue(onClickListener)
        return this
    }

    fun isTwoStateDialog(boolean: Boolean): NoticeDialog {
        layoutTwoStateVisibility.sendValue(boolean)
        return this
    }
}