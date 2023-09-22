package com.arefdev.base.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.arefdev.base.R

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class SwipeToDeleteListItemTouchHelper(private val recyclerView: RecyclerView, onSwipedListener: ItemSwipedListener) {

    companion object {
        operator fun set(recyclerView: RecyclerView, onSwipedListener: ItemSwipedListener) {
            SwipeToDeleteListItemTouchHelper(recyclerView, onSwipedListener)
        }
    }

    interface ItemSwipedListener {
        fun onSwiped(position: Int)
    }

    private val itemTouchHelper: ItemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(onSwipedListener))

    init {
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun setEnabled(enabled: Boolean) {
        itemTouchHelper.attachToRecyclerView(if (enabled) recyclerView else null)
    }

    private inner class SwipeToDeleteCallback(private val onSwipedListener: ItemSwipedListener) :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        private val icon: Drawable? = ContextCompat.getDrawable(recyclerView.context, R.drawable.ic_trash_bin)
        private val background = ColorDrawable(Color.RED)

        init {
            icon?.setTint(Color.WHITE)
        }

        override fun onMove(
            recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            // used for up and down movements
            return false
        }

        override fun onSwiped(holder: RecyclerView.ViewHolder, direction: Int) {
            onSwipedListener.onSwiped(holder.bindingAdapterPosition)
        }

        override fun onChildDraw(
            c: Canvas, recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            val itemView = viewHolder.itemView
            val backgroundCornerOffset = 20 //so background is behind the rounded corners of itemView
            val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
            val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
            val iconBottom = iconTop + icon.intrinsicHeight
            when {
                dX > 0 -> { // Swiping to the right
                    val iconLeft = itemView.left + iconMargin + icon.intrinsicWidth
                    val iconRight = itemView.left + iconMargin
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    background.setBounds(
                        itemView.left, itemView.top,
                        itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom
                    )
                }

                dX < 0 -> { // Swiping to the left
                    val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                    val iconRight = itemView.right - iconMargin
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    background.setBounds(
                        itemView.right + dX.toInt() - backgroundCornerOffset,
                        itemView.top, itemView.right, itemView.bottom
                    )
                }

                else -> { // view is unSwiped
                    background.setBounds(0, 0, 0, 0)
                }
            }
            background.draw(c)
            icon.draw(c)
        }
    }
}