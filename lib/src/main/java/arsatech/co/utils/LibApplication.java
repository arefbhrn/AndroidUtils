package arsatech.co.utils;

import android.app.Application;
import android.content.Context;

import timber.log.Timber;

public class LibApplication extends Application {

	private static Context context;

	public static Context getContext() {
		return context;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		context = this;

		Timber.plant(new TimberLogTree());
	}

}