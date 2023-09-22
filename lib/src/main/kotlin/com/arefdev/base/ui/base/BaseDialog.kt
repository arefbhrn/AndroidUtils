package com.arefdev.base.ui.base

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
abstract class BaseDialog<VDB : ViewDataBinding> : AppCompatDialogFragment() {

    interface OnViewClickListener {
        fun onClick(dialog: DialogFragment)
    }

    lateinit var binding: VDB
        private set
    protected abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VDB
    private val animate = false

    private var onDismissListener: DialogInterface.OnDismissListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = bindingInflater.invoke(layoutInflater, container, false).also { it.lifecycleOwner = this }

        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
//        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
//        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    val instance: DialogFragment
        get() = this

    fun show(manager: FragmentManager) {
        super.show(manager, null)
    }

    fun show(activity: FragmentActivity) {
        show(activity.supportFragmentManager, null)
    }

    fun show(context: Context) {
        if (context is FragmentActivity) show(context)
    }

    fun show(fragment: Fragment) {
        show(fragment.childFragmentManager, null)
    }

    fun setWidth(width: Int) {
        if (dialog == null) return
        val params = dialog!!.window!!.attributes
        params.width = width
        dialog!!.window!!.attributes = params
    }

    fun setHeight(height: Int) {
        if (dialog == null) return
        val params = dialog!!.window!!.attributes
        params.height = height
        dialog!!.window!!.attributes = params
    }

    fun setDimensions(width: Int, height: Int) {
        if (dialog == null) return
        val params = dialog!!.window!!.attributes
        params.width = width
        params.height = height
        dialog!!.window!!.attributes = params
    }

    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener?): BaseDialog<VDB> {
        this.onDismissListener = onDismissListener
        return this
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (onDismissListener != null) onDismissListener!!.onDismiss(dialog)
    }
}