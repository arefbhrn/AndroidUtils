package arsatech.co.utils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class AnimationUtils {

	public static class Blink {

		private static AlphaAnimation anim = new AlphaAnimation(1F, 0.1F);

		public static void blink(View view) {
			blink(view, 200);
		}

		public static void blink(View view, int duration) {
			anim.setDuration(duration);
			anim.setRepeatCount(1);
			anim.setRepeatMode(Animation.REVERSE);
			view.startAnimation(anim);
		}

	}

	public static class HeartBeat {

		public static void beat(View view) {
			ScaleAnimation anim = new ScaleAnimation(
					1f, 0.8f, // Start and end values for the X axis scaling
					1f, 0.8f, // Start and end values for the Y axis scaling
					Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
					Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
			anim.setFillAfter(true); // Needed to keep the result of the animation
			anim.setRepeatMode(Animation.REVERSE);
			anim.setRepeatCount(1);
			anim.setDuration(100);
			view.startAnimation(anim);
		}

		public static void beat(View view, long duration) {
			ScaleAnimation anim = new ScaleAnimation(
					1f, 0.8f, // Start and end values for the X axis scaling
					1f, 0.8f, // Start and end values for the Y axis scaling
					Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
					Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
			anim.setFillAfter(true); // Needed to keep the result of the animation
			anim.setRepeatMode(Animation.REVERSE);
			anim.setRepeatCount(1);
			anim.setDuration(duration);
			view.startAnimation(anim);
		}

		public static void beatByColor(final View view) {
			Animation anim = new Animation() {
				@Override
				protected void applyTransformation(float interpolatedTime, Transformation t) {
					super.applyTransformation(interpolatedTime, t);
					setDuration(300);
					setRepeatMode(Animation.REVERSE);
					setRepeatCount(1);
					final float[]
							from = new float[3],
							to = new float[3];
					Color.colorToHSV(Color.parseColor("#FFFFFFFF"), from); // from white
					Color.colorToHSV(Color.parseColor("#FFFF0000"), to); // to red
					final float[] hsv = new float[3]; // transition color
					hsv[0] = from[0] + (to[0] - from[0]) * interpolatedTime;
					hsv[1] = from[1] + (to[1] - from[1]) * interpolatedTime;
					hsv[2] = from[2] + (to[2] - from[2]) * interpolatedTime;
				}
			};
			view.startAnimation(anim);
		}

		public static void beatByColor(final View view, final long duration) {
			Animation anim = new Animation() {
				@Override
				protected void applyTransformation(float interpolatedTime, Transformation t) {
					super.applyTransformation(interpolatedTime, t);
					setDuration(duration);
					setRepeatMode(Animation.REVERSE);
					setRepeatCount(1);
					final float[]
							from = new float[3],
							to = new float[3];
					Color.colorToHSV(Color.parseColor("#FFFFFFFF"), from); // from white
					Color.colorToHSV(Color.parseColor("#FFFF0000"), to); // to red
					final float[] hsv = new float[3]; // transition color
					hsv[0] = from[0] + (to[0] - from[0]) * interpolatedTime;
					hsv[1] = from[1] + (to[1] - from[1]) * interpolatedTime;
					hsv[2] = from[2] + (to[2] - from[2]) * interpolatedTime;
				}
			};
			view.startAnimation(anim);
		}

		public static void beatByColor(final ImageView view) {
			Animation anim = new Animation() {
				@Override
				protected void applyTransformation(float interpolatedTime, Transformation t) {
					super.applyTransformation(interpolatedTime, t);
					setDuration(300);
					setRepeatMode(Animation.REVERSE);
					setRepeatCount(1);
					final float[]
							from = new float[3],
							to = new float[3];
					Color.colorToHSV(Color.parseColor("#FFFFFFFF"), from); // from white
					Color.colorToHSV(Color.parseColor("#FFFF0000"), to); // to red
					final float[] hsv = new float[3]; // transition color
					hsv[0] = from[0] + (to[0] - from[0]) * interpolatedTime;
					hsv[1] = from[1] + (to[1] - from[1]) * interpolatedTime;
					hsv[2] = from[2] + (to[2] - from[2]) * interpolatedTime;
					view.setColorFilter(Color.HSVToColor(hsv), PorterDuff.Mode.SRC_IN);
				}
			};
			view.startAnimation(anim);
		}

		public static void beatByColor(final ImageView view, final long duration) {
			Animation anim = new Animation() {
				@Override
				protected void applyTransformation(float interpolatedTime, Transformation t) {
					super.applyTransformation(interpolatedTime, t);
					setDuration(duration);
					setRepeatMode(Animation.REVERSE);
					setRepeatCount(1);
					final float[]
							from = new float[3],
							to = new float[3];
					Color.colorToHSV(Color.parseColor("#FFFFFFFF"), from); // from white
					Color.colorToHSV(Color.parseColor("#FFFF0000"), to); // to red
					final float[] hsv = new float[3]; // transition color
					hsv[0] = from[0] + (to[0] - from[0]) * interpolatedTime;
					hsv[1] = from[1] + (to[1] - from[1]) * interpolatedTime;
					hsv[2] = from[2] + (to[2] - from[2]) * interpolatedTime;
					view.setColorFilter(Color.HSVToColor(hsv), PorterDuff.Mode.SRC_IN);
				}
			};
			view.startAnimation(anim);
		}

	}

	public static class BeatBackground {

		private static Handler handler = new Handler();

		public static void animate(View view, int colorFrom, int colorTo, int duration, boolean smooth) {
			if (smooth)
				animateSmoothly(view, colorFrom, colorTo, duration);
			else
				animateNormally(view, colorFrom, colorTo, duration);
		}

		public static void animate(View view, Drawable from, Drawable to, int duration, boolean smooth) {
			if (smooth)
				animateSmoothly(view, from, to, duration);
			else
				animateNormally(view, from, to, duration);
		}

		public static void animate(View view, Drawable from, Drawable to,
		                           int fillDuration, int reverseDuration, int waitDuration, boolean smooth) {
			if (smooth)
				animateSmoothly(view, from, to, fillDuration, reverseDuration, waitDuration);
			else
				animateNormally(view, from, to, waitDuration);
		}

		public static void animateSmoothly(View view, int colorFrom, int colorTo, int duration) {
			ValueAnimator anim = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
			anim.setDuration(duration);
			anim.setRepeatMode(ValueAnimator.REVERSE);
			anim.setRepeatCount(1);
			anim.addUpdateListener(animator -> view.setBackgroundColor((int) animator.getAnimatedValue()));
			anim.start();
		}

		public static void animateSmoothly(View view, Drawable from, Drawable to, int duration) {
			int fillDuration = duration / 5;
			int reverseDuration = duration / 5;
			int waitDuration = duration - reverseDuration;
			TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{from, to});
			transitionDrawable.setCrossFadeEnabled(false);
			view.setBackground(transitionDrawable);
			transitionDrawable.startTransition(fillDuration);
			handler.postDelayed(() -> transitionDrawable.reverseTransition(reverseDuration), waitDuration);
		}

		public static void animateSmoothly(View view, Drawable from, Drawable to,
		                                   int fillDuration, int reverseDuration, int waitDuration) {
			TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{from, to});
			transitionDrawable.setCrossFadeEnabled(false);
			view.setBackground(transitionDrawable);
			transitionDrawable.startTransition(fillDuration);
			handler.postDelayed(() -> transitionDrawable.reverseTransition(reverseDuration), waitDuration + fillDuration);
		}

		public static void animateNormally(View view, int colorFrom, int colorTo, int duration) {
			view.setBackgroundColor(colorTo);
			new Handler().postDelayed(() -> view.setBackgroundColor(colorFrom), duration);
		}

		public static void animateNormally(View view, Drawable from, Drawable to, int duration) {
			view.setBackground(to);
			new Handler().postDelayed(() -> view.setBackground(from), duration);
		}

	}

	public static class Resize {

		public static void resize(View view, int newWidth, int newHeight) {
			ValueAnimator animWidth = ValueAnimator.ofInt(view.getWidth(), newWidth);
			animWidth.addUpdateListener(valueAnimator -> {
				int val = (Integer) valueAnimator.getAnimatedValue();
				ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
				layoutParams.width = val;
				view.setLayoutParams(layoutParams);
			});
			animWidth.setDuration(500);
			animWidth.start();
			ValueAnimator animHeight = ValueAnimator.ofInt(view.getHeight(), newHeight);
			animHeight.addUpdateListener(valueAnimator -> {
				int val = (Integer) valueAnimator.getAnimatedValue();
				ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
				layoutParams.height = val;
				view.setLayoutParams(layoutParams);
			});
			animHeight.setDuration(500);
			animHeight.start();
		}

		public static void resize(final View view, final float fromWidth, final float fromHeight,
		                          final float toWidth, final float toHeight) {
			Animation anim = new Animation() {
				@Override
				protected void applyTransformation(float interpolatedTime, Transformation t) {
					setDuration(2000);
					float height =
							(toHeight - fromHeight) * interpolatedTime + fromHeight;
					float width = (toWidth - fromWidth) * interpolatedTime + fromWidth;
					ViewGroup.LayoutParams p = view.getLayoutParams();
					p.height = (int) height;
					p.width = (int) width;
					view.requestLayout();
				}
			};
			view.startAnimation(anim);
		}

	}

	public static class Fade {

		public static void fadeIn(View view, final int duration) {
			Animation animation = android.view.animation.AnimationUtils
					.loadAnimation(view.getContext(), android.R.anim.fade_in);
			animation.setDuration(duration);
			view.startAnimation(animation);
		}

		public static void fadeOut(final View view, int duration) {
			Animation animation = android.view.animation.AnimationUtils
					.loadAnimation(view.getContext(), android.R.anim.fade_in);
			animation.setDuration(duration);
			view.startAnimation(animation);
		}

	}

	public static class Rotate {

		public static RotateAnimation rotate() {
			RotateAnimation anim = new RotateAnimation(0, 360,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			anim.setDuration(500);
			anim.setRepeatCount(Animation.INFINITE);
			anim.setInterpolator(new LinearInterpolator());
			return anim;
		}

	}

	public static class Move {

		public static void move(View view, float fromXDelta, float toXDelta,
		                        float fromYDelta, float toYDelta, long duration) {
			TranslateAnimation anim = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
			anim.setFillAfter(true);
			anim.setDuration(duration);
			view.startAnimation(anim);
		}

	}

	public static class Vibrate {

		public static void vibrate(View view) {
			TranslateAnimation anim = new TranslateAnimation(0, 10, 0, 0);
			anim.setInterpolator(new CycleInterpolator(3));
			anim.setDuration(500);
			view.startAnimation(anim);
		}

	}

}
