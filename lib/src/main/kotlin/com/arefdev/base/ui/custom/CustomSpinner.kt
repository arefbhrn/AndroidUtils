package com.arefdev.base.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.arefdev.base.R
import com.arefdev.base.databinding.CustomSpinnerBinding
import com.arefdev.base.ui.adapters.recyclerView.base.BaseListAdapter

/**
 * Updated on 07/03/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class CustomSpinner : LinearLayout {

    class Item(var name: CharSequence, var data: Any)

    interface OnItemSelectedListener {
        fun onItemSelected(item: Item?, position: Int)
    }

    private lateinit var binding: CustomSpinnerBinding
    private val items = mutableListOf<Item>()
    private lateinit var popupMenuWindow: CustomPopupMenuWindow
    private var onItemSelectedListener: OnItemSelectedListener? = null
    var selectedItemPosition = -1
        private set
    private var alwaysSelected = false
    private var onClickListener: OnClickListener? = null

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        binding = CustomSpinnerBinding.inflate(LayoutInflater.from(context), this, true)

        popupMenuWindow = CustomPopupMenuWindow(context, emptyList(), object : BaseListAdapter.OnItemClickListener {
            override fun onClick(position: Int) {
                setSelection(position)
                onItemSelectedListener?.onItemSelected(items[position], position)
            }
        })

        setOnClickListener(null)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomSpinner)

        isEnabled = typedArray
            .getBoolean(R.styleable.CustomSpinner_android_enabled, true)

        setTitle(
            typedArray
                .getText(R.styleable.CustomSpinner_android_title)
        )

        setHint(
            typedArray
                .getText(R.styleable.CustomSpinner_android_hint)
        )

        setAlwaysSelected(
            typedArray
                .getBoolean(R.styleable.CustomSpinner_alwaysSelected, false)
        )

        if (typedArray.hasValue(R.styleable.CustomSpinner_android_entries)) {
            setItems(
                typedArray
                    .getTextArray(R.styleable.CustomSpinner_android_entries)
            )
        }

        setError(
            typedArray
                .getText(R.styleable.CustomSpinner_error)
        )

        typedArray.recycle()
    }

    fun setTitle(text: CharSequence?) {
        binding.label.text = text ?: ""
    }

    fun setHint(text: CharSequence?) {
        binding.tvText.hint = text ?: ""
    }

    fun setError(error: CharSequence?) {
        binding.error.text = error ?: ""
        binding.lytError.visibility = if (error == null) GONE else VISIBLE
        binding.lyt.setStrokeColor(ContextCompat.getColor(context, if (error != null) R.color.red else R.color.light_gray))
    }

    private fun setAlwaysSelected(enabled: Boolean) {
        alwaysSelected = enabled
        setSelection(selectedItemPosition)
    }

    fun setItems(items: Array<CharSequence>) {
        val list: List<Item> = items.map { Item(it, it) }
        setItems(list)
    }

    fun setItems(items: List<Item>) {
        this.items.clear()
        this.items.addAll(items)
        val list: List<CharSequence> = items.map { it.name }
        popupMenuWindow.setItems(list)
        setSelection(-1)
    }

    private val isSelectionInRange: Boolean
        get() = items.isNotEmpty() && selectedItemPosition >= 0 && selectedItemPosition < count

    fun setSelection(position: Int) {
        selectedItemPosition = position
        if (alwaysSelected && !isSelectionInRange) {
            selectedItemPosition = 0
            if (onItemSelectedListener != null) onItemSelectedListener!!.onItemSelected(selectedItem, selectedItemPosition)
        }
        binding.tvText.text = selectedItem?.name ?: ""
    }

    val selectedItem: Item?
        get() = if (isSelectionInRange) items[selectedItemPosition] else null
    val count: Int
        get() = items.size

    fun getItemAtPosition(position: Int): Item {
        return items[position]
    }

    fun setOnItemSelectedListener(listener: OnItemSelectedListener?) {
        onItemSelectedListener = listener
    }

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        this.onClickListener = onClickListener
        super.setOnClickListener {
            if (items.isEmpty()) return@setOnClickListener
            showDropDown()
            if (this.onClickListener != null) this.onClickListener!!.onClick(this)
        }
    }

    fun showDropDown() {
        popupMenuWindow.show(this)
    }

    fun dismissDropDown() {
        popupMenuWindow.dismiss()
    }
}