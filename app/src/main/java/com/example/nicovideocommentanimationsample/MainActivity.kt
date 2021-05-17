package com.example.nicovideocommentanimationsample

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.doOnEnd
import androidx.core.view.doOnLayout
import androidx.databinding.DataBindingUtil
import com.example.nicovideocommentanimationsample.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        _binding.apply {
            setOnClickButton {
                startAnimation(this.container)
            }
        }
    }

    private fun startAnimation(constraintLayout: ConstraintLayout) {
        val windowSize = getWindowSize()
        if (windowSize.width == 0 || windowSize.height == 0) return

        // TextViewを生成
        val textView = TextView(baseContext).apply {
            id = View.generateViewId()
            text = "Hello World!"
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        }
        textView.setTextColor(Color.BLACK)
        constraintLayout.addView(textView)

        // Y位置をランダムで設定
        val random = Random(System.currentTimeMillis())
        val transitionY = random.nextInt(windowSize.height - textView.layoutParams.height)
        textView.translationY = transitionY.toFloat()

        // 生成したTextViewに制約を設定
        ConstraintSet().also {
            it.clone(constraintLayout)
            it.connect(
                    textView.id,
                    ConstraintSet.LEFT,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.RIGHT
            )
            it.applyTo(constraintLayout)
        }

        // 右から左に移動
        textView.doOnLayout {
            val textViewWidth = it.width.toFloat()
            val objectAnimator = ObjectAnimator.ofFloat(
                    textView,
                    "translationX",
                    0f,
                    -(windowSize.width + textViewWidth)
            ).apply {
                interpolator = LinearInterpolator()
                duration = 3000L
            }.also {
                it.doOnEnd {
                    // アニメーションが終わったViewを削除
                    constraintLayout.removeView(textView)
                }
            }
            objectAnimator.start()
        }
    }

    private fun getWindowSize(): WindowSize {
        val outMetrics = DisplayMetrics()
        display?.getRealMetrics(outMetrics)
        return WindowSize(
                width = outMetrics.widthPixels,
                height = outMetrics.heightPixels
        )
    }
}