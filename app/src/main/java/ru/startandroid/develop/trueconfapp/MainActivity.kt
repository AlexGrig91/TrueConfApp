package ru.startandroid.develop.trueconfapp

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnRepeat
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var textView: TextView
    lateinit var frameLayout: FrameLayout
    lateinit var animator: ValueAnimator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onResume() {
        super.onResume()
        textView = findViewById(R.id.text)
        frameLayout = findViewById(R.id.layout)

        frameLayout.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                setTextPosition(textView, motionEvent.x, motionEvent.y)
                setTextColor(textView, getLocale(this@MainActivity))
                startAnimation()
                true
            }
            false
        }
    }

    private fun startAnimation() {
        if (::animator.isInitialized) animator.cancel()
        animator = ValueAnimator.ofInt(textView.marginTop, frameLayout.height).apply {
            startDelay = 5000L
            duration = 3000L
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            doOnRepeat {
                (it as ValueAnimator).setIntValues(0, frameLayout.height)
            }
            addUpdateListener {
                textView.setPosition(textView.marginLeft, it.animatedValue as Int)
            }
            start()
        }
    }

    private fun setTextPosition(view: View, x: Float, y: Float) {
        val dx = view.width / 2
        val dy = view.height / 2
        view.setPosition(x.toInt() - dx, y.toInt() - dy)
    }

    private fun setTextColor(textView: TextView, locale: Locale) {
        val color = when (locale.language) {
            "ru" -> Color.BLUE
            "en" -> Color.RED
            else -> Color.BLACK
        }
        textView.setTextColor(color)
    }

    private fun getLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            context.resources.configuration.locales[0]
        else
            context.resources.configuration.locale
    }

    fun View.setPosition(x: Int, y: Int) {
        if (layoutParams is ViewGroup.MarginLayoutParams) {
            val marginLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
            marginLayoutParams.setMargins(x, y, 0, 0)
            requestLayout()
        }
    }
}