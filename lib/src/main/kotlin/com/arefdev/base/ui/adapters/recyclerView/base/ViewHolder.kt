package com.arefdev.base.ui.adapters.recyclerView.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

/**
 * Updated on 07/03/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class ViewHolder<VDB : ViewDataBinding>(val binding: VDB, lifecycleOwner: LifecycleOwner? = null) : RecyclerView.ViewHolder(binding.root) {

    init {
        if (lifecycleOwner != null)
            binding.lifecycleOwner = lifecycleOwner
    }
}