package arsatech.co.utils;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.util.Log;

import timber.log.Timber;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class TimberLogTree extends Timber.Tree {

	private static final String DEFAULT_TAG = "TimberLogTree";

	@SuppressLint("LogNotTimber")
	@Override
	protected void log(int priority, @Nullable String tag, @Nullable String message, @Nullable Throwable t) {
		if (!BuildConfig.DEBUG && (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO)) {
			return;
		}
		switch (priority) {
			case Log.VERBOSE:
				if (tag != null)
					Log.v(tag, message);
				else
					Log.v(DEFAULT_TAG, message);
				break;
			case Log.DEBUG:
				if (tag != null)
					Log.d(tag, message);
				else
					Log.d(DEFAULT_TAG, message);
				break;
			case Log.INFO:
				if (tag != null)
					Log.i(tag, message);
				else
					Log.i(DEFAULT_TAG, message);
				break;
			case Log.WARN:
				if (tag != null)
					Log.w(tag, message);
				else
					Log.w(DEFAULT_TAG, message);
				break;
			case Log.ERROR:
				if (tag != null)
					Log.e(tag, message);
				else
					Log.e(DEFAULT_TAG, message);
				break;
		}
	}

}
