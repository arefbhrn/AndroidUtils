package com.arefdev.base.ui.adapters.dropdown

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.arefdev.base.R
import com.arefdev.base.databinding.ListItemSimpleSpinnerBinding

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class SimpleSpinnerAdapter<T>(context: Context, items: List<T>?) : ArrayAdapter<T>(context, 0, items!!) {

    constructor(context: Context, items: Array<T>) : this(context, listOf(*items))

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder = ViewHolder.getCustomView(context, convertView)
        val item = getItem(position)
        val tv: TextView = holder.binding.tv
        val divider = holder.binding.divider
        tv.text = item.toString()
        tv.setTextColor(Color.BLACK)
        divider.visibility = View.GONE
        return holder.binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder = ViewHolder.getCustomView(context, convertView)
        val item = getItem(position)
        val tv: TextView = holder.binding.tv
        tv.text = item.toString()
        tv.setBackgroundResource(R.drawable.ripple_bg)
        return holder.binding.root
    }

    private class ViewHolder private constructor(context: Context) {

        val binding: ListItemSimpleSpinnerBinding = ListItemSimpleSpinnerBinding.inflate(LayoutInflater.from(context))

        companion object {

            fun getCustomView(context: Context, convertView: View?): ViewHolder {
                var mConvertView = convertView
                val holder: ViewHolder
                if (mConvertView == null) {
                    holder = ViewHolder(context)
                    mConvertView = holder.binding.root
                    mConvertView.tag = holder
                } else {
                    holder = mConvertView.tag as ViewHolder
                }
                return holder
            }
        }

    }
}