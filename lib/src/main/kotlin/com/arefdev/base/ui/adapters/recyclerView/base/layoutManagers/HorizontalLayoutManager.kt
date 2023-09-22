package com.arefdev.base.ui.adapters.recyclerView.base.layoutManagers

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class HorizontalLayoutManager : LinearLayoutManager {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, reverseLayout: Boolean) : super(context, HORIZONTAL, reverseLayout)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        super.setOrientation(HORIZONTAL)
    }

    @Deprecated("")
    override fun setOrientation(orientation: Int) {
        super.setOrientation(HORIZONTAL)
    }
}