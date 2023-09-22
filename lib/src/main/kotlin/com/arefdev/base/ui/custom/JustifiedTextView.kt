package com.arefdev.base.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Align
import android.text.Html
import android.text.Spanned
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.arefdev.base.enums.SDK_CODES
import com.arefdev.base.utils.isOsAtLeast

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class JustifiedTextView : AppCompatTextView {

    private val paint = Paint()
    private var wrapEnabled = true
    private var align = Align.RIGHT

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    init {
        setPadding(10, 10, 10, 10)
    }

    fun setText(st: CharSequence?, wrap: Boolean) {
        wrapEnabled = wrap
        super.setText(st)
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        //at least 10px padding bottom
        super.setPadding(left + 10, top + 10, right + 10, bottom + 10)
    }

    fun setTextAlign(align: Align) {
        this.align = align
        super.setText(text)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        // If wrap is disabled then request original onDraw
        if (wrapEnabled.not()) {
            super.onDraw(canvas)
            return
        }
        // Pull widget properties
        paint.color = currentTextColor
        paint.setTypeface(typeface)
        paint.textSize = textSize
        paint.textAlign = align
        paint.flags = Paint.ANTI_ALIAS_FLAG
        // minus out the paddings pixel
        val dirtyRegionWidth = (width - paddingLeft - paddingRight).toFloat()
        var maxLines = Int.MAX_VALUE
        if (isOsAtLeast(SDK_CODES.JELLY_BEAN)) {
            maxLines = getMaxLines()
        }
        var lines = 1
        val blocks = fromHtml(text.toString()).split("((?<=\n)|(?=\n))".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val horizontalFontOffset: Float = lineHeight - 0.5f
        var verticalOffset = horizontalFontOffset
        val spaceOffset = paint.measureText(" ")
        var i = 0
        while (i < blocks.size && lines <= maxLines) {
            var block = blocks[i]
            var horizontalOffset = 0f
            if (block.isEmpty()) {
                i++
                continue
            } else if (block == "\n") {
                verticalOffset += horizontalFontOffset
                i++
                continue
            }
            block = block.trim { it <= ' ' }
            if (block.isEmpty()) {
                i++
                continue
            }
            val wrappedObj = createWrappedLine(block, paint, spaceOffset, dirtyRegionWidth)
            val wrappedLine = wrappedObj[0] as String
            val wrappedEdgeSpace = wrappedObj[1] as Float
            val lineAsWords = wrappedLine.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val stretchOffset: Float = if (wrappedEdgeSpace != Float.MIN_VALUE) wrappedEdgeSpace / (lineAsWords.size - 1) else 0F
            for (j in lineAsWords.indices) {
                val word = lineAsWords[j]
                if (lines == maxLines && j == lineAsWords.size - 1) {
                    canvas.drawText("...", horizontalOffset, verticalOffset, paint)
                } else if (j == 0) {
                    // if it is the first word of the line, text will be drawn starting from right edge of textview
                    horizontalOffset += if (align == Align.RIGHT) {
                        canvas.drawText(word, (width - paddingRight).toFloat(), verticalOffset, paint)
                        // add in the paddings to the horizontalOffset
                        (width - paddingRight).toFloat()
                    } else {
                        canvas.drawText(word, paddingLeft.toFloat(), verticalOffset, paint)
                        paddingLeft.toFloat()
                    }
                } else {
                    canvas.drawText(word, horizontalOffset, verticalOffset, paint)
                }
                if (align == Align.RIGHT) horizontalOffset -= paint.measureText(word) + spaceOffset + stretchOffset else horizontalOffset += paint.measureText(
                    word
                ) + spaceOffset + stretchOffset
            }
            lines++
            if (blocks[i].isNotEmpty()) {
                blocks[i] = blocks[i].substring(wrappedLine.length)
                verticalOffset += if (blocks[i].isNotEmpty()) horizontalFontOffset else 0F
                i--
            }
            i++
        }
    }

    private fun fromHtml(html: String): String {
        val result: Spanned =
            if (isOsAtLeast(SDK_CODES.N))
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            else
                Html.fromHtml(html)
        return result.toString()
    }

    companion object {
        fun createWrappedLine(block: String, paint: Paint, spaceOffset: Float, maxWidth: Float): Array<Any> {
            var mMaxWidth = maxWidth
            var cacheWidth: Float
            val line = StringBuilder()
            for (word in block.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                cacheWidth = paint.measureText(word)
                mMaxWidth -= cacheWidth
                if (mMaxWidth <= 0) {
                    return arrayOf(line.toString(), mMaxWidth + cacheWidth + spaceOffset)
                }
                line.append(word).append(" ")
                mMaxWidth -= spaceOffset
            }
            return if (paint.measureText(block) <= maxWidth)
                arrayOf(block, Float.MIN_VALUE)
            else
                arrayOf(line.toString(), mMaxWidth)
        }
    }
}
