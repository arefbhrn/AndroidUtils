package com.arefdev.base.ui.adapters.recyclerView.base.layoutManagers

import android.content.Context
import android.util.AttributeSet
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class StartRowFlexboxLayoutManager : FlexboxLayoutManager {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, flexDirection: Int) : super(context, flexDirection) {
        init()
    }

    constructor(context: Context, flexDirection: Int, flexWrap: Int) : super(context, flexDirection, flexWrap) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        flexDirection = FlexDirection.ROW
        justifyContent = JustifyContent.FLEX_START
        alignItems = AlignItems.FLEX_START
    }
}