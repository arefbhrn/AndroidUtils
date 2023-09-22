package com.arefdev.base.utils

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.CycleInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.view.animation.Transformation
import android.view.animation.TranslateAnimation
import android.widget.ImageView

/**
 * Updated on 21/09/2023
 *
 * @author [Aref Bahreini](https://github.com/arefbhrn)
 */
object AnimationUtils {

    /**
     * Loads animation resource as [Animation] object
     *
     * @param context Context
     * @param animRes animation resource
     * @return [Animation] object
     */
    fun loadAnimation(context: Context, animRes: Int): Animation {
        return AnimationUtils.loadAnimation(context, animRes)
    }

    object Blink {
        private val anim = AlphaAnimation(1f, 0.1f)

        @JvmOverloads
        fun blink(view: View, duration: Int = 200) {
            anim.duration = duration.toLong()
            anim.repeatCount = 1
            anim.repeatMode = Animation.REVERSE
            view.startAnimation(anim)
        }
    }

    object HeartBeat {
        @JvmOverloads
        fun beat(view: View, duration: Int = 100) {
            val anim = ScaleAnimation(
                1f, 0.8f,  // Start and end values for the X axis scaling
                1f, 0.8f,  // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f,  // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f
            ) // Pivot point of Y scaling
            anim.fillAfter = true // Needed to keep the result of the animation
            anim.repeatMode = Animation.REVERSE
            anim.repeatCount = 1
            anim.duration = duration.toLong()
            view.startAnimation(anim)
        }

        @JvmOverloads
        fun beatByColor(view: View, duration: Int? = null) {
            val anim: Animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    super.applyTransformation(interpolatedTime, t)
                    duration?.also { setDuration(it.toLong()) }
                    repeatMode = REVERSE
                    repeatCount = 1
                    val from = FloatArray(3)
                    val to = FloatArray(3)
                    Color.colorToHSV(Color.parseColor("#FFFFFFFF"), from) // from white
                    Color.colorToHSV(Color.parseColor("#FFFF0000"), to) // to red
                    val hsv = FloatArray(3) // transition color
                    hsv[0] = from[0] + (to[0] - from[0]) * interpolatedTime
                    hsv[1] = from[1] + (to[1] - from[1]) * interpolatedTime
                    hsv[2] = from[2] + (to[2] - from[2]) * interpolatedTime
                }
            }
            view.startAnimation(anim)
        }

        @JvmOverloads
        fun beatByColor(view: ImageView, duration: Int? = null) {
            val anim: Animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    super.applyTransformation(interpolatedTime, t)
                    duration?.also { setDuration(it.toLong()) }
                    repeatMode = REVERSE
                    repeatCount = 1
                    val from = FloatArray(3)
                    val to = FloatArray(3)
                    Color.colorToHSV(Color.parseColor("#FFFFFFFF"), from) // from white
                    Color.colorToHSV(Color.parseColor("#FFFF0000"), to) // to red
                    val hsv = FloatArray(3) // transition color
                    hsv[0] = from[0] + (to[0] - from[0]) * interpolatedTime
                    hsv[1] = from[1] + (to[1] - from[1]) * interpolatedTime
                    hsv[2] = from[2] + (to[2] - from[2]) * interpolatedTime
                    view.setColorFilter(Color.HSVToColor(hsv), PorterDuff.Mode.SRC_IN)
                }
            }
            view.startAnimation(anim)
        }
    }

    object BeatBackground {
        private val handler = Handler(Looper.getMainLooper())

        fun animate(view: View, colorFrom: Int, colorTo: Int, duration: Int, smooth: Boolean) {
            if (smooth)
                animateSmoothly(view, colorFrom, colorTo, duration)
            else
                animateNormally(view, colorFrom, colorTo, duration)
        }

        fun animate(view: View, from: Drawable, to: Drawable, duration: Int, smooth: Boolean) {
            if (smooth)
                animateSmoothly(view, from, to, duration)
            else
                animateNormally(view, from, to, duration)
        }

        fun animate(view: View, from: Drawable, to: Drawable, fillDuration: Int, reverseDuration: Int, waitDuration: Int, smooth: Boolean) {
            if (smooth)
                animateSmoothly(view, from, to, fillDuration, reverseDuration, waitDuration)
            else
                animateNormally(view, from, to, waitDuration)
        }

        fun animateSmoothly(view: View, colorFrom: Int, colorTo: Int, duration: Int) {
            val anim = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
            anim.setDuration(duration.toLong())
            anim.repeatMode = ValueAnimator.REVERSE
            anim.repeatCount = 1
            anim.addUpdateListener { animator: ValueAnimator -> view.setBackgroundColor(animator.animatedValue as Int) }
            anim.start()
        }

        fun animateSmoothly(view: View, from: Drawable, to: Drawable, duration: Int) {
            val fillDuration = duration / 5
            val reverseDuration = duration / 5
            val waitDuration = duration - reverseDuration
            val transitionDrawable = TransitionDrawable(arrayOf(from, to))
            transitionDrawable.isCrossFadeEnabled = false
            view.background = transitionDrawable
            transitionDrawable.startTransition(fillDuration)
            handler.postDelayed({ transitionDrawable.reverseTransition(reverseDuration) }, waitDuration.toLong())
        }

        fun animateSmoothly(view: View, from: Drawable, to: Drawable, fillDuration: Int, reverseDuration: Int, waitDuration: Int) {
            val transitionDrawable = TransitionDrawable(arrayOf(from, to))
            transitionDrawable.isCrossFadeEnabled = false
            view.background = transitionDrawable
            transitionDrawable.startTransition(fillDuration)
            handler.postDelayed({ transitionDrawable.reverseTransition(reverseDuration) }, (waitDuration + fillDuration).toLong())
        }

        fun animateNormally(view: View, colorFrom: Int, colorTo: Int, duration: Int) {
            view.setBackgroundColor(colorTo)
            Handler(Looper.getMainLooper()).postDelayed({ view.setBackgroundColor(colorFrom) }, duration.toLong())
        }

        fun animateNormally(view: View, from: Drawable, to: Drawable, duration: Int) {
            view.background = to
            Handler(Looper.getMainLooper()).postDelayed({ view.background = from }, duration.toLong())
        }
    }

    object Fade {
        private fun initFadeIn(mView: View) {
            mView.visibility = View.VISIBLE
            mView.animate()
                .alpha(0f)
                .setDuration(0)
                .start()
        }

        private fun initFadeOut(mView: View) {
            mView.visibility = View.VISIBLE
            mView.animate()
                .alpha(1f)
                .setDuration(0)
                .start()
        }

        fun fadeIn(mView: View, duration: Int) {
            initFadeIn(mView)
            mView.animate()
                .alpha(1f)
                .setDuration(duration.toLong())
                .setInterpolator(DecelerateInterpolator())
                .start()
        }

        fun fadeOut(mView: View, duration: Int) {
            initFadeOut(mView)
            mView.animate()
                .alpha(0f)
                .setDuration(duration.toLong())
                .setInterpolator(DecelerateInterpolator())
                .withEndAction { mView.visibility = View.GONE }
                .start()
        }

        fun justFadeIn(mView: View, duration: Int) {
            mView.animate()
                .alpha(1f)
                .setDuration(duration.toLong())
                .setInterpolator(DecelerateInterpolator())
                .start()
        }

        fun justFadeOut(mView: View, duration: Int) {
            mView.animate()
                .alpha(0f)
                .setDuration(duration.toLong())
                .setInterpolator(DecelerateInterpolator())
                .start()
        }

        fun fadeInAnim(mView: View, duration: Int): ViewPropertyAnimator {
            initFadeIn(mView)
            return mView.animate()
                .alpha(1f)
                .setDuration(duration.toLong())
                .setInterpolator(DecelerateInterpolator())
        }

        fun fadeOutAnim(mView: View, duration: Int): ViewPropertyAnimator {
            initFadeOut(mView)
            return mView.animate()
                .alpha(0f)
                .setDuration(duration.toLong())
                .setInterpolator(DecelerateInterpolator())
                .withEndAction { mView.visibility = View.GONE }
        }

        fun justFadeInAnim(mView: View, duration: Int): ViewPropertyAnimator {
            return mView.animate()
                .alpha(1f)
                .setDuration(duration.toLong())
                .setInterpolator(DecelerateInterpolator())
        }

        fun justFadeOutAnim(mView: View, duration: Int): ViewPropertyAnimator {
            return mView.animate()
                .alpha(0f)
                .setDuration(duration.toLong())
                .setInterpolator(DecelerateInterpolator())
        }

        fun replace(fadeOutTarget: View, fadeInTarget: View, duration: Int) {
            initFadeOut(fadeOutTarget)
            fadeOutTarget.animate()
                .alpha(0f)
                .setDuration((duration / 2).toLong())
                .setInterpolator(DecelerateInterpolator())
                .withEndAction {
                    fadeOutTarget.visibility = View.GONE
                    initFadeIn(fadeInTarget)
                    fadeIn(fadeInTarget, duration / 2)
                }
                .start()
        }

        fun replace2(fadeOutTarget: View, fadeInTarget: View, duration: Int) {
            val mAnimationSet = AnimatorSet()
            val fadeOut = ObjectAnimator.ofFloat(fadeOutTarget, View.ALPHA, 1f, 0f)
            fadeOut.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    fadeOutTarget.visibility = View.GONE
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            fadeOut.interpolator = LinearInterpolator()
            val fadeIn = ObjectAnimator.ofFloat(fadeInTarget, View.ALPHA, 0f, 1f)
            fadeIn.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    fadeInTarget.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            fadeIn.interpolator = LinearInterpolator()
            mAnimationSet.setDuration(duration.toLong())
            mAnimationSet.play(fadeIn).after(fadeOut)
            mAnimationSet.start()
        }

        fun crossFadeAnimation(fadeOutTarget: View, fadeInTarget: View, duration: Long) {
            val mAnimationSet = AnimatorSet()
            val fadeOut = ObjectAnimator.ofFloat(fadeOutTarget, View.ALPHA, 1f, 0f)
            fadeOut.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    fadeOutTarget.visibility = View.GONE
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            fadeOut.interpolator = LinearInterpolator()
            val fadeIn = ObjectAnimator.ofFloat(fadeInTarget, View.ALPHA, 0f, 1f)
            fadeIn.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    fadeInTarget.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            fadeIn.interpolator = LinearInterpolator()
            mAnimationSet.setDuration(duration)
            mAnimationSet.playTogether(fadeOut, fadeIn)
            mAnimationSet.start()
        }
    }

    /**
     * Class to Move view
     */
    object Move {
        /**
         * Move the view from current position to new ones in custom duration
         *
         * @param view     View to animate
         * @param newX     New position on X axis in pixels
         * @param newY     New position on YX axis in pixels
         * @param duration Animation duration in milliseconds
         * @param callback [Animator.AnimatorListener] callback
         */
        @JvmOverloads
        fun move(view: View, newX: Int, newY: Int, duration: Int = 500, callback: Animator.AnimatorListener? = null) {
            val animX = ValueAnimator.ofInt(view.x.toInt(), newX)
            animX.addUpdateListener { valueAnimator: ValueAnimator ->
                val value = valueAnimator.animatedValue as Int
                view.x = value.toFloat()
            }
            animX.setDuration(duration.toLong())
            if (callback != null)
                animX.addListener(callback)
            animX.start()
            val animY = ValueAnimator.ofInt(view.y.toInt(), newY)
            animY.addUpdateListener { valueAnimator: ValueAnimator ->
                val value = valueAnimator.animatedValue as Int
                view.y = value.toFloat()
            }
            animY.setDuration(duration.toLong())
            if (callback != null)
                animY.addListener(callback)
            animY.start()
        }

        fun move(view: View, fromXDelta: Float, toXDelta: Float, fromYDelta: Float, toYDelta: Float, duration: Long) {
            val anim = TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta)
            anim.fillAfter = true
            anim.duration = duration
            view.startAnimation(anim)
        }
    }

    /**
     * Class to resize view dimensions
     */
    object Resize {
        /**
         * Resize the view from current dimensions to new ones in custom duration
         *
         * @param view      View to animate
         * @param newWidth  New width in pixels
         * @param newHeight New height in pixels
         * @param duration  Animation duration in milliseconds
         * @param callback  [Animator.AnimatorListener] callback
         */
        @JvmOverloads
        fun resize(view: View, newWidth: Int, newHeight: Int, duration: Int = 500, callback: Animator.AnimatorListener? = null) {
            resize(view, view.width, newWidth, view.height, newHeight, duration, callback)
        }

        /**
         * Resize the view from current dimensions to new ones in custom duration
         *
         * @param view       View to animate
         * @param fromWidth  Stating width
         * @param toWidth    Goal width
         * @param fromHeight Starting height
         * @param toHeight   Goal width
         * @param duration   Animation duration in milliseconds
         * @param callback   [Animator.AnimatorListener] callback
         */
        @JvmOverloads
        fun resize(
            view: View, fromWidth: Int, toWidth: Int, fromHeight: Int, toHeight: Int, duration: Int = 500,
            callback: Animator.AnimatorListener? = null
        ) {
            val animWidth = ValueAnimator.ofInt(fromWidth, toWidth)
            animWidth.addUpdateListener { valueAnimator: ValueAnimator ->
                val value = valueAnimator.animatedValue as Int
                val layoutParams = view.layoutParams
                layoutParams.width = value
                view.layoutParams = layoutParams
            }
            animWidth.setDuration(duration.toLong())
            if (callback != null)
                animWidth.addListener(callback)
            animWidth.start()
            val animHeight = ValueAnimator.ofInt(fromHeight, toHeight)
            animHeight.addUpdateListener { valueAnimator: ValueAnimator ->
                val value = valueAnimator.animatedValue as Int
                val layoutParams = view.layoutParams
                layoutParams.height = value
                view.layoutParams = layoutParams
            }
            animHeight.setDuration(duration.toLong())
            if (callback != null)
                animHeight.addListener(callback)
            animHeight.start()
        }

        fun resize(view: View, fromWidth: Float, fromHeight: Float, toWidth: Float, toHeight: Float, duration: Int) {
            val anim: Animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    val height = (toHeight - fromHeight) * interpolatedTime + fromHeight
                    val width = (toWidth - fromWidth) * interpolatedTime + fromWidth
                    val p = view.layoutParams
                    p.height = height.toInt()
                    p.width = width.toInt()
                    view.requestLayout()
                }
            }
            anim.duration = duration.toLong()
            view.startAnimation(anim)
        }
    }

    object Rotate {
        fun rotate(): RotateAnimation {
            val anim = RotateAnimation(
                0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            )
            anim.duration = 500
            anim.repeatCount = Animation.INFINITE
            anim.interpolator = LinearInterpolator()
            return anim
        }
    }

    /**
     * Class to vibrate/shake a view
     */
    object Vibrate {
        /**
         * Vibrate/Shake a view in custom duration
         *
         * @param view     View to animate
         * @param duration Animation duration in milliseconds
         */
        @JvmOverloads
        fun vibrate(view: View, duration: Int = 500) {
            val anim = TranslateAnimation(0f, 10f, 0f, 0f)
            anim.interpolator = CycleInterpolator(3f)
            anim.duration = duration.toLong()
            view.startAnimation(anim)
        }
    }
}
