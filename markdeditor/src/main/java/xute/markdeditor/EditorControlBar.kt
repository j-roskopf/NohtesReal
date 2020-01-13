package xute.markdeditor

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import xute.markdeditor.MarkDEditor.EditorFocusReporter
import xute.markdeditor.Styles.TextComponentStyle
import xute.markdeditor.components.TextComponentItem


class EditorControlBar : FrameLayout, EditorFocusReporter {
    private var mContext: Context? = null
    private var mEditor: MarkDEditor? = null
    private lateinit var normalTextBtn: TextView
    private lateinit var headingBtn: TextView
    private lateinit var headingNumberBtn: TextView
    private lateinit var bulletBtn: ImageView
    private lateinit var blockQuoteBtn: ImageView
    private lateinit var linkBtn: ImageView
    private lateinit var hrBtn: ImageView
    private lateinit var imageBtn: ImageView
    private var enabledColor = 0
    private var disabledColor = 0
    private var currentHeading = 1
    private var olEnabled = false
    private var ulEnabled = false
    private var blockquoteEnabled = false
    private var editorControlListener: EditorControlListener? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    private fun init(context: Context) {
        mContext = context
        val view = LayoutInflater.from(context)
            .inflate(R.layout.editor_control_bar, this)
        normalTextBtn =
            view.findViewById(R.id.normalTextBtn)
        headingBtn = view.findViewById(R.id.headingBtn)
        headingNumberBtn =
            view.findViewById(R.id.headingNumberBtn)
        bulletBtn = view.findViewById(R.id.bulletBtn)
        blockQuoteBtn =
            view.findViewById(R.id.blockquoteBtn)
        linkBtn = view.findViewById(R.id.insertLinkBtn)
        hrBtn = view.findViewById(R.id.insertHrBtn)
        imageBtn = view.findViewById(R.id.insertImageBtn)
        enabledColor = ContextCompat.getColor(context, R.color.enabledEditorIconColor)
        disabledColor = ContextCompat.getColor(context, R.color.disabledEditorIconColor)
        normalTextBtn.setTextColor(enabledColor)
        headingBtn.setTextColor(disabledColor)
        headingNumberBtn.setTextColor(disabledColor)
        bulletBtn.setColorFilter(disabledColor)
        blockQuoteBtn.setColorFilter(disabledColor)
        linkBtn.setColorFilter(disabledColor)
        hrBtn.setColorFilter(disabledColor)
        imageBtn.setColorFilter(disabledColor)
        attachListeners()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init(context)
    }

    fun setEditor(editor: MarkDEditor?) {
        mEditor = editor
        subscribeForStyles()
    }

    private fun subscribeForStyles() {
        if (mEditor != null) {
            mEditor!!.setEditorFocusReporter(this)
        }
    }

    private fun attachListeners() {
        normalTextBtn.setOnClickListener {
            mEditor!!.setHeading(TextComponentStyle.NORMAL)
            invalidateStates(TextComponentItem.MODE_PLAIN, TextComponentStyle.NORMAL)
        }
        headingBtn.setOnClickListener {
            if (currentHeading == MAX_HEADING) {
                currentHeading = 1
                mEditor!!.setHeading(currentHeading)
            } else {
                mEditor!!.setHeading(++currentHeading)
            }
            invalidateStates(TextComponentItem.MODE_PLAIN, currentHeading)
        }
        bulletBtn.setOnClickListener {
            if (olEnabled) { //switch to normal
                mEditor!!.setHeading(TextComponentStyle.NORMAL)
                invalidateStates(TextComponentItem.MODE_PLAIN, TextComponentStyle.NORMAL)
                olEnabled = false
                ulEnabled = false
            } else if (ulEnabled) { // switch to ol mode
                mEditor!!.changeToOLMode()
                invalidateStates(TextComponentItem.MODE_OL, TextComponentStyle.NORMAL)
                olEnabled = true
                ulEnabled = false
            } else if (!olEnabled && !ulEnabled) { // switch to ul mode
                mEditor!!.changeToULMode()
                invalidateStates(TextComponentItem.MODE_UL, TextComponentStyle.NORMAL)
                ulEnabled = true
                olEnabled = false
            }
        }
        blockQuoteBtn.setOnClickListener {
            if (blockquoteEnabled) { //switch to normal
                mEditor!!.setHeading(TextComponentStyle.NORMAL)
                invalidateStates(TextComponentItem.MODE_PLAIN, TextComponentStyle.NORMAL)
            } else { // block quote
                mEditor!!.changeToBlockquote()
                invalidateStates(TextComponentItem.MODE_PLAIN, TextComponentStyle.BLOCKQUOTE)
            }
        }
        hrBtn.setOnClickListener { mEditor!!.insertHorizontalDivider() }
        linkBtn.setOnClickListener {
            if (editorControlListener != null) {
                editorControlListener!!.onInserLinkClicked()
            }
        }
        imageBtn.setOnClickListener {
            if (editorControlListener != null) {
                editorControlListener!!.onInsertImageClicked()
            }
        }
    }

    private fun enableNormalText(enabled: Boolean) {
        if (enabled) {
            normalTextBtn.setTextColor(enabledColor)
        } else {
            normalTextBtn.setTextColor(disabledColor)
        }
    }

    private fun enableHeading(enabled: Boolean, headingNumber: Int) {
        if (enabled) {
            currentHeading = headingNumber
            headingBtn.setTextColor(enabledColor)
            headingNumberBtn.setTextColor(enabledColor)
            headingNumberBtn.text = headingNumber.toString()
        } else {
            currentHeading = 0
            headingBtn.setTextColor(disabledColor)
            headingNumberBtn.setTextColor(disabledColor)
            headingNumberBtn.text = "1"
        }
    }

    private fun enableBullet(enable: Boolean, isOrdered: Boolean) {
        if (enable) {
            if (isOrdered) {
                olEnabled = true
                ulEnabled = false
                bulletBtn.setImageResource(R.drawable.ol)
            } else {
                ulEnabled = true
                olEnabled = false
                bulletBtn.setImageResource(R.drawable.ul)
            }
            bulletBtn.setColorFilter(enabledColor)
        } else {
            ulEnabled = false
            olEnabled = false
            bulletBtn.setImageResource(R.drawable.ul)
            bulletBtn.setColorFilter(disabledColor)
        }
    }

    private fun enableBlockquote(enable: Boolean) {
        blockquoteEnabled = enable
        if (enable) {
            blockQuoteBtn.setColorFilter(enabledColor)
        } else {
            blockQuoteBtn.setColorFilter(disabledColor)
        }
    }

    private fun invalidateStates(mode: Int, textComponentStyle: Int) {
        if (mode == TextComponentItem.MODE_OL) {
            enableBlockquote(false)
            enableHeading(false, 1)
            enableNormalText(false)
            enableBullet(true, true)
        } else if (mode == TextComponentItem.MODE_UL) {
            enableBlockquote(false)
            enableHeading(false, 1)
            enableNormalText(false)
            enableBullet(true, false)
        } else if (mode == TextComponentItem.MODE_PLAIN) {
            when (textComponentStyle) {
                TextComponentStyle.H1 -> {
                    enableBlockquote(false)
                    enableHeading(true, 1)
                    enableNormalText(false)
                    enableBullet(false, false)
                }
                TextComponentStyle.H2 -> {
                    enableBlockquote(false)
                    enableHeading(true, 2)
                    enableNormalText(false)
                    enableBullet(false, false)
                }
                TextComponentStyle.H3 -> {
                    enableBlockquote(false)
                    enableHeading(true, 3)
                    enableNormalText(false)
                    enableBullet(false, false)
                }
                TextComponentStyle.H4 -> {
                    enableBlockquote(false)
                    enableHeading(true, 4)
                    enableNormalText(false)
                    enableBullet(false, false)
                }
                TextComponentStyle.H5 -> {
                    enableBlockquote(false)
                    enableHeading(true, 5)
                    enableNormalText(false)
                    enableBullet(false, false)
                }
                TextComponentStyle.BLOCKQUOTE -> {
                    enableBlockquote(true)
                    enableHeading(false, 1)
                    enableNormalText(false)
                    enableBullet(false, false)
                }
                TextComponentStyle.NORMAL -> {
                    enableBlockquote(false)
                    enableHeading(false, 1)
                    enableNormalText(true)
                    enableBullet(false, false)
                }
            }
        }
    }

    override fun onFocusedViewHas(mode: Int, textComponentStyle: Int) {
        invalidateStates(mode, textComponentStyle)
    }

    fun setEditorControlListener(editorControlListener: EditorControlListener?) {
        this.editorControlListener = editorControlListener
    }

    interface EditorControlListener {
        fun onInsertImageClicked()
        fun onInserLinkClicked()
    }

    companion object {
        const val MAX_HEADING = 5
    }
}