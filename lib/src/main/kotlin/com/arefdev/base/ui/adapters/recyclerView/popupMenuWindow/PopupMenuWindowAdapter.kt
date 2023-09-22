package com.arefdev.base.ui.adapters.recyclerView.popupMenuWindow

import android.content.Context
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.arefdev.base.databinding.ListItemPopupWindowBinding
import com.arefdev.base.ui.adapters.recyclerView.base.BaseListAdapter
import com.arefdev.base.ui.adapters.recyclerView.base.ViewHolder

/**
 * Updated on 07/03/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class PopupMenuWindowAdapter : BaseListAdapter<CharSequence, ListItemPopupWindowBinding> {

    override val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> ListItemPopupWindowBinding = ListItemPopupWindowBinding::inflate

    private var menu: Menu?
    private val onItemClickListener: OnItemClickListener

    constructor(context: Context, list: List<CharSequence>, onItemClickListener: OnItemClickListener) : super(context, list) {
        this.onItemClickListener = onItemClickListener
        menu = null
    }

    constructor(context: Context, menu: Menu, onItemClickListener: OnItemClickListener) : super(context, emptyList()) {
        this.menu = menu
        this.onItemClickListener = onItemClickListener
    }

    override fun onBindViewHolder(holder: ViewHolder<ListItemPopupWindowBinding>, item: CharSequence) {
        if (menu == null) {
            holder.binding.tv.text = item
            holder.itemView.setOnClickListener { onItemClickListener.onClick(holder.bindingAdapterPosition) }
        } else {
            val menuItem = menu!!.getItem(holder.bindingAdapterPosition)
            holder.binding.tv.text = menuItem.title.toString()
            holder.itemView.setOnClickListener { onItemClickListener.onClick(menuItem.itemId) }
        }
        holder.binding.divider.visibility = if (holder.bindingAdapterPosition == itemCount - 1) View.GONE else View.VISIBLE
    }

    override fun onBindingViewHolder(holder: ViewHolder<ListItemPopupWindowBinding>, position: Int) {
        TODO("Not yet implemented")
    }

    override fun setList(list: List<CharSequence>) {
        menu = null
        super.setList(list)
    }

    fun setMenu(menu: Menu?) {
        this.menu = menu
        super.setList(emptyList())
    }

    override fun getItemCount(): Int {
        return menu?.size() ?: super.getItemCount()
    }
}