package com.arefdev.base.ui.dialogs.okCancel

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.arefdev.base.ui.base.BaseViewModel

/**
 * Updated on 15/06/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class OkCancelDialogViewModel : BaseViewModel() {

    val titleText = MutableLiveData<CharSequence>()
    val titleTextColor = MutableLiveData<Int>()
    val bodyText = MutableLiveData<CharSequence>()
    val btnPositiveText = MutableLiveData<CharSequence>()
    val btnPositiveTextColor = MutableLiveData<Int>()
    val btnPositiveBackgroundColor = MutableLiveData<Int>()
    val btnPositiveClickListener = MutableLiveData<View.OnClickListener>()
    val btnNegativeText = MutableLiveData<CharSequence>()
    val btnNegativeTextColor = MutableLiveData<Int>()
    val btnNegativeBackgroundColor = MutableLiveData<Int>()
    val btnNegativeClickListener = MutableLiveData<View.OnClickListener>()
}