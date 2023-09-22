package com.arefdev.base.ui.dialogs.notice

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.arefdev.base.ui.base.BaseViewModel

/**
 * Updated on 15/06/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class NoticeDialogViewModel : BaseViewModel() {

    val titleText = MutableLiveData<CharSequence>(null)
    val bodyText = MutableLiveData<CharSequence>(null)
    val btnConfirmText = MutableLiveData<CharSequence>()
    val btnConfirmTextColor = MutableLiveData<Int>()
    val btnConfirmBackgroundColor = MutableLiveData<Int>()
    val btnConfirmClickListener = MutableLiveData<View.OnClickListener>()
    val btnSecondText = MutableLiveData<CharSequence>()
    val btnSecondTextColor = MutableLiveData<Int>()
    val btnSecondBackgroundColor = MutableLiveData<Int>()
    val btnSecondClickListener = MutableLiveData<View.OnClickListener>()
    val layoutTwoStateVisibility = MutableLiveData(false)
}