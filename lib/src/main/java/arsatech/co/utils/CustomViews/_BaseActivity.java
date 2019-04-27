package arsatech.co.utils.CustomViews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.Constructor;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class _BaseActivity extends AppCompatActivity {

	public static final String ACTIVITY_STARTED_FROM = "ActivityStartedFrom";
	private String startedFrom = null;

	public Activity getInstance() {
		return this;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getIntent().hasExtra(ACTIVITY_STARTED_FROM))
			startedFrom = getIntent().getStringExtra(ACTIVITY_STARTED_FROM);
	}

	@Override
	public void startActivity(Intent intent) {
		if (!intent.hasExtra(ACTIVITY_STARTED_FROM))
			intent.putExtra(ACTIVITY_STARTED_FROM, getClass().getName());
		super.startActivity(intent);
//		overridePendingTransition(R.anim.slide_in_from_right, R.anim.activity_fading_slide_out_to_left);
	}

	@Override
	public void finish() {
		super.finish();
//		overridePendingTransition(R.anim.activity_fading_slide_in_from_left, R.anim.slide_out_to_right);
	}

	public void startActivityStarter(@Nullable Intent intent) {
		try {
			Class<?> c = Class.forName(startedFrom);
			Constructor<?> cons = c.getConstructor(String.class);
			Object activity = cons.newInstance();
			if (intent == null)
				intent = new Intent();
			intent.setClass(this, activity.getClass());
			startActivity(intent);
		} catch (Exception ignored) {
		}
	}

	public void setActivityStarterObject(Class activityClass) {
		startedFrom = activityClass.getName();
	}

	public Class<?> getActivityStarter() {
		try {
			return Class.forName(startedFrom);
		} catch (Exception ignored) {
			return null;
		}
	}

}
