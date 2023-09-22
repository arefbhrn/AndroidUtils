package com.arefdev.base.ui.custom

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DefaultItemAnimator
import com.arefdev.base.databinding.CustomPopupWindowBinding
import com.arefdev.base.enums.SDK_CODES
import com.arefdev.base.ui.adapters.recyclerView.base.BaseListAdapter
import com.arefdev.base.ui.adapters.recyclerView.popupMenuWindow.PopupMenuWindowAdapter
import com.arefdev.base.utils.dpToPx
import com.arefdev.base.utils.isOsAtLeast

/**
 * Updated on 07/03/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class CustomPopupMenuWindow {

    private lateinit var context: Context
    private lateinit var binding: CustomPopupWindowBinding
    private lateinit var popupWindow: PopupWindow
    private var onItemClickListener: BaseListAdapter.OnItemClickListener? = null
    private val internalItemClickListener: BaseListAdapter.OnItemClickListener = object : BaseListAdapter.OnItemClickListener {
        override fun onClick(position: Int) {
            onItemClickListener?.onClick(position)
            dismiss()
        }
    }

    constructor(context: Context, menuItems: List<CharSequence>, onItemClickListener: BaseListAdapter.OnItemClickListener) {
        init(context, onItemClickListener)
        setupRecyclerView(menuItems)
    }

    constructor(context: Context, resMenu: Int, onItemClickListener: BaseListAdapter.OnItemClickListener) {
        init(context, onItemClickListener)
        setupRecyclerView(resMenu)
    }

    private fun init(context: Context, onItemClickListener: BaseListAdapter.OnItemClickListener) {
        this.context = context

        // inflate the layout of the popup window
        binding = CustomPopupWindowBinding.inflate(LayoutInflater.from(context), null, false)

        // create the popup window
        popupWindow = PopupWindow(
            binding.root, ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

//        val size = Point();
//        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        wm.defaultDisplay.getSize(size)
//        val width = size.x
//        val height = size.y
//        popupWindow.width = width * 3 / 6
//        popupWindow.height = height * 3 / 8
        popupWindow.isFocusable = true // lets taps outside the popup also dismiss it
        popupWindow.isOutsideTouchable = true
        if (isOsAtLeast(SDK_CODES.LOLLIPOP)) {
            popupWindow.elevation = dpToPx(8f)
        }

        this.onItemClickListener = onItemClickListener
    }

    private fun setupRecyclerView(list: List<CharSequence>) {
        val adapter = PopupMenuWindowAdapter(context, list, internalItemClickListener)
        setupRecyclerView(adapter)
        binding.root.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
    }

    private fun setupRecyclerView(resMenu: Int) {
        val menu = PopupMenu(context, binding.root).menu
        MenuInflater(context).inflate(resMenu, menu)
        val adapter = PopupMenuWindowAdapter(context, menu, internalItemClickListener)
        setupRecyclerView(adapter)
        binding.root.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
    }

    private fun setupRecyclerView(adapter: PopupMenuWindowAdapter) {
        binding.rcvMenu.itemAnimator = DefaultItemAnimator()
        binding.rcvMenu.adapter = adapter
        binding.rcvMenu.isNestedScrollingEnabled = false
    }

    fun setItems(items: List<CharSequence>) {
        setupRecyclerView(items)
    }

    fun show(anchor: View) {
        popupWindow.showAsDropDown(
            anchor,
            0,
            dpToPx(-10f).toInt(),
            Gravity.CENTER
        )
        binding.root.post {
            var lp = binding.root.layoutParams
            if (lp == null) {
                lp = ViewGroup.MarginLayoutParams(anchor.width, binding.root.height)
            } else {
                lp.width = anchor.measuredWidth
                lp.height = binding.root.height
            }
            binding.root.layoutParams = lp
        }
    }

    fun dismiss() {
        popupWindow.dismiss()
    }

    val isShowing: Boolean
        get() = popupWindow.isShowing
}