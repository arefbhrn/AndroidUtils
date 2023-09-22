package com.arefdev.base.ui.dialogs.loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.arefdev.base.databinding.DialogLoadingBinding
import com.arefdev.base.ui.base.BaseDialog

/**
 * Updated on 15/06/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class LoadingDialog : BaseDialog<DialogLoadingBinding>() {

    companion object {

        fun getInstance(): LoadingDialog {
            return LoadingDialog()
        }
    }


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> DialogLoadingBinding = DialogLoadingBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }
}